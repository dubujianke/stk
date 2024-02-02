package com.mk.report;

import com.mk.tool.stock.*;

import java.util.ArrayList;
import java.util.List;

public class LineReport {
    private String code;
    private String file;
    private StockAllMinuteLine stockAllMinuteLine;
    private int bottomFlag;
    private Kline needForce;
    private String[] colors = new String[]{"#00FF33", "#FFFF49"};
    private List<Grid> listGrid = new ArrayList<>();

    public void clear() {
        stockAllMinuteLine = null;
        listGrid = null;
    }


    public void add(Grid grid) {
        listGrid.add(grid);
    }

    public int getCategorySize() {
        return listGrid.get(0).getCategorySize();
    }

    public String getSerieName(int i) {
        return listGrid.get(i).getSerieName(i);
    }

    public List<String> getCategory() {
        return listGrid.get(0).getCategory();
    }

    public List<String> getX() {
        List<String> alist = new ArrayList<>();
        for (Grid span : listGrid) {
            alist.add(span.kline.getDate());
        }
        return alist;
    }

    public int getSpanSize() {
        return listGrid.get(0).getSpanSize();
    }


    public List<Integer> getSeries(int i) {
        List<Integer> list = new ArrayList<>();
        for (Grid grid : listGrid) {
            list.add(grid.getSpan(i));
        }
        return list;
    }

    public List<Integer> getReportCross() {
        List<Integer> list = new ArrayList<>();
        List<Integer> listDead = new ArrayList<>();
        for (Grid grid : listGrid) {
            list.add(grid.isGoldCross());
        }
        for (Grid grid : listGrid) {
            listDead.add(grid.isDeadCrossAll());
        }

        int noEvnet = getNoEvent();
        if (noEvnet > 0) {
            if (list.get(0) == 0) {
                list.set(0, noEvnet * 10);
            }
        }
        if (listDead.get(0) == 1) {
            list.set(0, 0);
        }
        return list;
    }

    public List<Integer> getIsBottom() {
        List<Integer> list = new ArrayList<>();
        List<Integer> listDead = new ArrayList<>();
        for (Grid grid : listGrid) {
            list.add(0);
        }
        for (Grid grid : listGrid) {
            listDead.add(grid.isDeadCrossAll());
        }

        int noEvnet = getBottomFlag();
        if (noEvnet > 0) {
            if (list.get(0) == 0) {
                list.set(0, noEvnet * 10);
            }
        }
        if (listDead.get(0) == 1) {
            list.set(0, 0);
        }
        return list;
    }

    public int isDeadCross() {
        List<Integer> listDead = new ArrayList<>();

        for (Grid grid : listGrid) {
            listDead.add(grid.isDeadCrossAll());
        }


        if (listDead.get(0) == 1) {
            return 1;
        }
        return 0;
    }

    public List<Integer> getSppedUpWithVOL(LineContext context) {
        List<Integer> list = new ArrayList<>();
        for (Grid grid : listGrid) {
            Kline item = grid.kline;
            MinuteLine minuteLine2 = StockAllMinuteLine.getFirstSpeedUp(item, getStockAllMinuteLine(), getCode(), item.getDate(), context);
            boolean flag = false;
            int total = 0;
            if (minuteLine2 != null) {
                total = minuteLine2.getVol() + minuteLine2.prev().getVol() + minuteLine2.prev(2).getVol();
                if (minuteLine2.getPrice() > minuteLine2.prev().getPrice()
                        && minuteLine2.prev(1).getPrice() > minuteLine2.prev(2).getPrice()
                        && minuteLine2.prev(2).getPrice() > minuteLine2.prev(3).getPrice()
                ) {
                    float frac = KLineUtil.compareMaxSign(minuteLine2.price, item.prev().getClose());
                    if (frac > 1.4) {
                        flag = true;
                    }
                }
            }
            if (flag && minuteLine2 != null) {
                list.add(total);
            } else {
                list.add(0);
            }
        }
        return list;
    }

    public List<Integer> getFirstSpeedDownWithVOLInDays(LineContext context) {
        List<Integer> list = new ArrayList<>();
        for (Grid grid : listGrid) {
            Kline item = grid.kline;
            MinuteLine minuteLine2 = StockAllMinuteLine.getFirstSpeedDownWithVOLInDays(item, getStockAllMinuteLine(), getCode(), item.getDate(), context);
            if (minuteLine2 != null) {
                int total = minuteLine2.getVol() + minuteLine2.prev().getVol() + minuteLine2.prev(2).getVol();
                list.add(total);
            } else {
                list.add(0);
            }
        }
        return list;
    }

    public Object[] isFirstSpeedUpWithVOLInDays(LineContext context) {
        List<Integer> list = getSppedUpWithVOL(context);
        for (int i = 0; i < 5; i++) {
            Integer v = list.get(i);
            if (v > 0) {
                return new Object[]{i, v};
            }
        }
        return new Object[]{-1, 0};
    }

    public Object[] isFirstSpeedDownWithVOLInDays(LineContext context) {
        List<Integer> list = getFirstSpeedDownWithVOLInDays(context);
        for (int i = 0; i < 5; i++) {
            Integer v = list.get(i);
            if (v > 0) {
                return new Object[]{i, v};
            }
        }
        return new Object[]{-1, 0};
    }

    public String getColor(int i) {
        return colors[i];
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public StockAllMinuteLine getStockAllMinuteLine() {
        return stockAllMinuteLine;
    }

    public void setStockAllMinuteLine(StockAllMinuteLine stockAllMinuteLine) {
        this.stockAllMinuteLine = stockAllMinuteLine;
    }

    public int getNoEvent() {
        boolean flag2 = true;
        List<Integer> list2 = getSeries(0);
        for (int i = 0; i < list2.size(); i++) {
            int v = list2.get(0);
            if (v < 200) {
                flag2 = false;
            }
        }
        if (flag2) {
            return 1;
        }
        return 0;
    }


    public int getBottomFlag() {
        return bottomFlag;
    }

    public void setBottomFlag(int bottomFlag) {
        this.bottomFlag = bottomFlag;
    }


    public Kline getNeedForce() {
        return needForce;
    }

    public void setNeedForce(Kline needForce) {
        this.needForce = needForce;
    }
}
