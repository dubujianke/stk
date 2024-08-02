package com.alading.tool.stock.cyq;

import com.alading.tool.stock.KLineUtil;
import com.alading.tool.stock.Kline;
import com.alading.util.ArrayUtil;
import com.alading.util.StringUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alading.model.Pair;

public class CYQCalculator {
    public CYQCalculator(List<Kline> klines, double decendFrac, Integer accuracyFactor, Integer range) {
        this.klinedata = klines;
        this.decendFrac = decendFrac;
        this.fator = accuracyFactor != null ? accuracyFactor : 150;
        this.range = range;
    }

    double decendFrac;

    /**
     * K图数据[time,open,close,high,low,volume,amount,amplitude,turnoverRate]
     */
    List<Kline> klinedata;

    /**
     * 精度因子(纵轴刻度数)
     */
    public int fator = 150;

    /**
     * 计算K线条数
     */
    public Integer range;

    /**
     * 计算分布及相关指标
     *
     * @param {number} index 当前选中的K线的索引
     * @return {{x: Array.<number>, y: Array.<number>}}
     */
    public CYQData calc(int index) throws Exception {
        double maxprice = 0;
        double minprice = 0;
        int factor = this.fator;
        int start = this.range != null ? Math.max(0, index - this.range + 1) : 0;
        List<Kline> kdata = ArrayUtil.slice(klinedata, start, Math.max(1, index + 1));
        if (kdata.size() == 0) {
            throw new Exception("invaild index");
        }


        for (int i = 0; i < kdata.size(); i++) {
            Kline elements = kdata.get(i);
            maxprice = maxprice == 0 ? elements.max : Math.max(maxprice, elements.max);
            minprice = minprice == 0 ? elements.min : Math.min(minprice, elements.min);
        }

        // 精度不小于0.01 产品逻辑
        double accuracy = Math.max(0.01f, (maxprice - minprice) / (factor - 1));
        /**
         * 值域
         */
        List<Double> yrange = new ArrayList();
        for (int i = 0; i < factor; i++) {
            double tmp = StringUtil.toFix(minprice + accuracy * i) / 1.0;
            yrange.add(tmp);
        }
        /**
         * 横轴数据
         */
        double[] xdata = createNumberArray(factor);
        for (int i = 0; i < kdata.size(); i++) {
            Kline eles = kdata.get(i);
            double open = eles.open;
            double close = eles.close;
            double high = eles.max;
            double low = eles.min;
            double avg = (open + close + high + low) / 4;
            double realHand = eles.hand / 100 * this.decendFrac;
            double turnoverRate = Math.min(1, realHand);

            int H = (int) Math.floor((high - minprice) / accuracy);
            int L = (int) Math.ceil((low - minprice) / accuracy);
            // G点坐标, 一字板时, X为进度因子
            double[] GPoint = new double[]{high == low ? factor - 1 : (2 / (high - low)), Math.floor((avg - minprice) / accuracy)};
            // 衰减
            for (int n = 0; n < xdata.length; n++) {
                xdata[n] *= (1 - turnoverRate);
            }
            if (high == low) {
                // 一字板时，画矩形面积是三角形的2倍
                xdata[(int) GPoint[1]] += GPoint[0] * turnoverRate / 2;
            } else {
                for (int j = L; j <= H; j++) {
                    double curprice = minprice + accuracy * j;
                    if (curprice <= avg) {
                        // 上半三角叠加分布分布
                        if (Math.abs(avg - low) < 1e-8) {
                            xdata[j] += GPoint[0] * turnoverRate;
                        } else {
                            xdata[j] += (curprice - low) / (avg - low) * GPoint[0] * turnoverRate;
                        }
                    } else {
                        // 下半三角叠加分布分布
                        if (Math.abs(high - avg) < 1e-8) {
                            xdata[j] += GPoint[0] * turnoverRate;
                        } else {
                            xdata[j] += (high - curprice) / (high - avg) * GPoint[0] * turnoverRate;
                        }
                    }
                }
            }
        }
        double currentprice = this.klinedata.get(index).close;
        double totalChips = 0;
        for (int i = 0; i < factor; i++) {
            double x = toPrecision(xdata[i], 12) / 1;
            totalChips += x;
        }
        Param param = new Param(factor, xdata, minprice, accuracy, totalChips);

        Kline cur = kdata.get(kdata.size() - 1);
        double ztPrice = cur.close * 1.1;
        double curPrice = cur.close;
        double dtPrice = cur.close * (1 - 0.1);
        double fracZt = getS(curPrice, ztPrice, yrange, xdata);
        double fracZtUp = getS(ztPrice, cur.close * 1.21, yrange, xdata);
        double fracDt = getS(dtPrice, curPrice, yrange, xdata);
        Map minLine = getMinLine(curPrice, curPrice * (1.1 - 0.04), curPrice * (1.1 + 0.04), yrange, xdata);

        double fracTrapInProfit = fracZt / fracDt * 100;
        double fracTrapInZtUp = fracZt / fracZtUp * 100;

        CYQData result = new CYQData();
        result.minLine = minLine;
        result.fracZt = StringUtil.toFix(fracZt);
        result.fracZt2 = StringUtil.toFix(fracZtUp);
        result.fracProfit = StringUtil.toFix(fracDt);
        result.fracTrapInProfit =  StringUtil.toFix(fracTrapInProfit);
        result.fracTrapInZtUp = StringUtil.toFix(fracTrapInZtUp);
        result.x = xdata;
        result.y = yrange;
        result.benefitPart = result.getBenefitPart(currentprice, param);
        result.avgCost = StringUtil.toFix(getCostByChip(totalChips * 0.5, param));
        result.percentChips.put("90", result.computePercentChips(0.9, param));
        result.percentChips.put("70", result.computePercentChips(0.7, param));
        return result;
    }

    public double getMax(List<Double> yrange, double[] xdata) {
        double v = 0;
        for (int i = 0; i < yrange.size(); i++) {
            double item = xdata[i];
            if (v < item) {
                v = item;
            }
        }
        return v;
    }

    public double getS2(List<Double> yrange, double[] xdata) {
        double v = 0;
        for (int i = 0; i < yrange.size(); i++) {
            double item = xdata[i];
            v += item;
        }
        return v;
    }

    public Map<String, Object> getMinLine(double curPrice, double min, double max, List<Double> yrange, double[] xdata) {
        double lineMax = getMax(yrange, xdata);
        double mianji = getS2(yrange, xdata);
        double minVFrac = Double.MAX_VALUE;
        double minV = 0;
        double minV2 = 0;
        double aprice = 0;
        for (int i = 0; i < yrange.size(); i++) {
            double price = yrange.get(i);
            if (price >= min && price <= max) {
                double item = xdata[i] / mianji;
                if (item < minVFrac) {
                    minVFrac = item;
                    minV = xdata[i];
                    minV2 = xdata[i] / lineMax;
                    aprice = price;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("priceZf", "" + StringUtil.toFix(KLineUtil.compareMaxSign(aprice, curPrice)));
        map.put("price", "" + StringUtil.toFix(aprice));
        map.put("minLine", "" + StringUtil.toFix4(minV));
        map.put("minLine2", "" + StringUtil.toFix4(minV2));
        map.put("minLineFrac", "" + StringUtil.toFix4(minVFrac));

        return map;
    }

    public double getS1(double min, double max, List<Double> yrange, double[] xdata) {
        double v = 0;
        for (int i = 0; i < yrange.size(); i++) {
            double price = yrange.get(i);
            if (price >= min && price <= max) {
                double item = xdata[i];
                v += item;
            }
        }
        return v;
    }

    public double getS(double min, double max, List<Double> yrange, double[] xdata) {
        double mianji = getS2(yrange, xdata);
        double v = getS1(min, max, yrange, xdata);
        v = v / mianji;
        return v;
    }

    /**
     * 获取指定筹码处的成本
     *
     * @param {number} chip 堆叠筹码
     */
    public double getCostByChip(double chip, Param param) {
        int factor = param.factor;
        double[] xdata = param.xdata;
        double minprice = param.minprice;
        double accuracy = param.accuracy;

        double result = 0;
        double sum = 0;
        for (int i = 0; i < factor; i++) {
            double x = toPrecision(xdata[i], 12) / 1;
            if (sum + x > chip) {
                result = minprice + i * accuracy;
                break;
            }
            sum += x;
        }
        return result;
    }

    static double toPrecision(double number, int precision) {
        String v = new BigDecimal(number, new MathContext(precision)).toString();
        return Double.parseDouble(v);
    }

    /**
     * 构造数字型数组
     *
     * @param {number} count 数组数量
     */
    public static double[] createNumberArray(int count) {
        double[] array = new double[count];
        for (int i = 0; i < count; i++) {
            array[i] = 0.0;
        }
        return array;
    }

    class Param {
        public Param(int factor, double[] xdata, double minprice, double accuracy, double totalChips) {
            this.factor = factor;
            this.xdata = xdata;
            this.minprice = minprice;
            this.accuracy = accuracy;
            this.totalChips = totalChips;
        }

        ;
        public int factor;
        public double[] xdata;
        public double minprice;
        public double accuracy;
        public double totalChips;
    }

    public class CYQData {
        public double[] x;
        public List<Double> y;
        public double benefitPart;
        public double avgCost;
        public Map<String, Map<String, Object>> percentChips = new HashMap<>();
        public double fracZt;
        public double fracZt2;
        public double fracProfit;
        public double fracTrapInProfit;
        public double fracTrapInZtUp;

        public Map minLine;

        public CYQData() {
        }

        /**
         * 获取指定价格的获利比例
         *
         * @param {number} price 价格
         */
        public double getBenefitPart(double price, Param param) {
            int factor = param.factor;
            double[] xdata = param.xdata;
            double minprice = param.minprice;
            double accuracy = param.accuracy;
            double totalChips = param.totalChips;

            float below = 0;
            for (int i = 0; i < factor; i++) {
                double x = toPrecision(xdata[i], 12) / 1;
                if (price >= minprice + i * accuracy) {
                    below += x;
                }
            }
            return totalChips == 0 ? 0 : below / totalChips;
        }

        /**
         * 计算指定百分比的筹码
         *
         * @param {number} percent 百分比大于0，小于1
         */
        public Map<String, Object> computePercentChips(double percent, Param param) {
            Map<String, Object> map = new HashMap<>();
            double totalChips = param.totalChips;

            if (percent > 1 || percent < 0) {
                return null;
            }
            double[] ps = new double[]{(1 - percent) / 2, (1 + percent) / 2};
            double[] pr = new double[]{getCostByChip(totalChips * ps[0], param), getCostByChip(totalChips * ps[1], param)};
            map.put("priceRange", new Pair<Double, Double>(StringUtil.toFix(pr[0]), StringUtil.toFix(pr[1])));
            map.put("concentration", pr[0] + pr[1] == 0 ? 0 : toPrecision((pr[1] - pr[0]) / (pr[0] + pr[1]), 4));
            return map;
        }


    }


}
