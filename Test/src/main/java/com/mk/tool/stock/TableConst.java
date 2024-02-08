package com.mk.tool.stock;

import com.mk.model.Col;
import com.mk.model.Row;

public class TableConst {

    public static Row getHeader() {
        Row row = new Row();
        try {
            row.add(new Col("code"));
            row.add(new Col("bk"));
            row.add(new Col("前日期"));
            row.add(new Col("日期"));
            row.add(new Col("分钟"));
            row.add(new Col("Test"));
            row.add(new Col("type"));
            row.add(new Col("涨幅"));
            row.add(new Col("振幅"));
            row.add(new Col("上月z幅"));
            row.add(new Col("上上月z幅"));
            row.add(new Col("大涨幅度"));
            row.add(new Col("大涨Idx"));
            row.add(new Col("大涨幅度2"));
            row.add(new Col("大涨Idx2"));
            row.add(new Col("大涨幅度3"));
            row.add(new Col("大涨Idx3"));
            row.add(new Col("股本"));
            row.add(new Col("日Crash"));
            row.add(new Col("周Crash"));
            row.add(new Col("月Crash"));

            row.add(new Col("日上吊线"));
            row.add(new Col("周上吊线"));
            row.add(new Col("月上吊线"));
            row.add(new Col("wcrash"));
            row.add(new Col("gap250"));
            row.add(new Col("gap120"));
//            row.add(new Col("gap120250"));
            row.add(new Col("gap60"));
            row.add(new Col("gap30"));


            row.add(new Col("k250"));
            row.add(new Col("k120"));
            row.add(new Col("k60"));
            row.add(new Col("k30"));

            row.add(new Col("w30d60"));
            row.add(new Col("w30d120"));
            row.add(new Col("w30d250"));
            row.add(new Col("w60d120"));
            row.add(new Col("w60d250"));
            row.add(new Col("w120d250"));


            //30d60	30d120	30d250	60d120	60d250	120d250
            row.add(new Col("10d30"));
            row.add(new Col("10d60"));
            row.add(new Col("30d60"));
            row.add(new Col("30d120"));
            row.add(new Col("30d250"));
            row.add(new Col("60d120"));
            row.add(new Col("60d250"));
            row.add(new Col("120d250"));


            row.add(new Col("10g30"));
            row.add(new Col("10g60"));
            row.add(new Col("30g60"));
            row.add(new Col("30g120"));
            row.add(new Col("30g250"));
            row.add(new Col("60g120"));
            row.add(new Col("60g250"));
            row.add(new Col("120g250"));



            row.add(new Col("60金叉120数"));
            row.add(new Col("120金叉250数"));

            row.add(new Col("30_60guaili"));
            row.add(new Col("30_120guaili"));
            row.add(new Col("30_250guaili"));
            row.add(new Col("60_120guaili"));
            row.add(new Col("60_250guaili"));
            row.add(new Col("120_250guaili"));

            row.add(new Col("w30_60guaili"));
            row.add(new Col("w30_120guaili"));
            row.add(new Col("w30_250guaili"));
            row.add(new Col("w60_120guaili"));
            row.add(new Col("w60_250guaili"));
            row.add(new Col("w120_250guaili"));

            //gap3060	gap30120	gap30250	gap60120	gap60250
            row.add(new Col("gap3060"));
            row.add(new Col("gap30120"));
            row.add(new Col("gap30250"));
            row.add(new Col("gap60120"));
            row.add(new Col("gap60250"));

            //gap250W	gap120W	gap250MOpen	gap120MOpen	gap250MCur	gap120MCur
            row.add(new Col("gap250W"));
            row.add(new Col("gap120W"));
            row.add(new Col("gap250MOpen"));
            row.add(new Col("gap120MOpen"));
            row.add(new Col("gap250MCur"));
            row.add(new Col("gap120MCur"));

            //gap120(9)	gap120(8)	gap120(7)	gap120(6)	gap120(5)	gap120(4)	gap120(3)	gap120(2)	gap120(1)	gap120(0)
            for (int i = 9; i >= 0; i--) {
                row.add(new Col("gap120(" + i + ")"));
            }
            for (int i = 9; i >= 0; i--) {
                row.add(new Col("gap250(" + i + ")"));
            }

            //maxPointMA250_	minPointMA250_	maxPointMA250	minPointMA250
            row.add(new Col("maxPointMA250_"));
            row.add(new Col("minPointMA250_"));
            row.add(new Col("maxPointMA250"));
            row.add(new Col("minPointMA250"));


            //zhenf(9)	zhenf(8)	zhenf(7)	zhenf(6)	zhenf(5)	zhenf(4)	zhenf(3)	zhenf(2)	zhenf(1)	zhenf(0)
            row.add(new Col("zhenf(9)"));
            row.add(new Col("zhenf(8)"));
            row.add(new Col("zhenf(7)"));
            row.add(new Col("zhenf(6)"));
            row.add(new Col("zhenf(5)"));
            row.add(new Col("zhenf(4)"));
            row.add(new Col("zhenf(3)"));
            row.add(new Col("zhenf(2)"));
            row.add(new Col("zhenf(1)"));
            row.add(new Col("zhenf(0)"));


            //price_(9)	price_(8)	price_(7)	price_(6)	price_(5)	price_(4)	price_(3)	price_(2)	price_(1)	price_(0)
            for (int i = 9; i >= 0; i--) {
                row.add(new Col("price_(" + i + ")"));
            }
            for (int i = 9; i >= 0; i--) {
                row.add(new Col("price(" + i + ")"));
            }

            //gold3060	gold30120	gold30250	gold60120	gold60250	gold120250
            row.add(new Col("gold3060"));
            row.add(new Col("gold30120"));
            row.add(new Col("gold30250"));
            row.add(new Col("gold60120"));
            row.add(new Col("gold60250"));
            row.add(new Col("gold120250"));


            //daytouch250	daytouch120	10dayma250	10dayma120
            row.add(new Col("daytouch250"));
            row.add(new Col("daytouch120"));
            row.add(new Col("10dayma250"));
            row.add(new Col("10dayma120"));

            //pricemin
            row.add(new Col("pricemin"));

            //prev(9)	prev(8)	prev(7)	prev(6)	prev(5)	prev(4)	prev(3)	prev(2)	prev(1)	cur
            row.add(new Col("prev(9)"));
            row.add(new Col("prev(8)"));
            row.add(new Col("prev(7)"));
            row.add(new Col("prev(6)"));
            row.add(new Col("prev(5)"));
            row.add(new Col("prev(4)"));
            row.add(new Col("prev(3)"));
            row.add(new Col("prev(2)"));
            row.add(new Col("prev(1)"));
            row.add(new Col("cur"));


            row.add(new Col("wprev_(4)"));
            row.add(new Col("wprev_(3)"));
            row.add(new Col("wprev_(2)"));
            row.add(new Col("wprev_(1)"));
            row.add(new Col("wcur_"));
            row.add(new Col("wprev(4)"));
            row.add(new Col("wprev(3)"));
            row.add(new Col("wprev(2)"));
            row.add(new Col("wprev(1)"));
            row.add(new Col("wcur"));

            //minw
            row.add(new Col("minw"));

            row.add(new Col("跳空下跌幅度"));
            row.add(new Col("跳空下跌Crash"));
            row.add(new Col("跳空下跌"));
            row.add(new Col("跳空下跌Idx"));
            row.add(new Col("跳空涨幅"));

            row.add(new Col("跳空上涨幅度"));
            row.add(new Col("跳空上涨Crash"));
            row.add(new Col("跳空上涨Idx"));

            row.add(new Col("跳空横盘"));
            row.add(new Col("横盘MA"));
            row.add(new Col("横盘MA距离"));

            row.add(new Col("跳空横盘2"));
            row.add(new Col("横盘MA2"));
            row.add(new Col("横盘MA距离2"));

            //放量下跌	上下压力	周涨幅	月长度	月涨幅	月均涨 分时最小	上上周涨幅	上周涨幅	两周涨幅	上上月涨幅	上月涨幅
            row.add(new Col("放量下跌"));
            row.add(new Col("上下压力"));
            row.add(new Col("周涨幅"));
            row.add(new Col("月长度"));
            row.add(new Col("月涨幅"));
            row.add(new Col("月均涨"));

            row.add(new Col("分时最小"));
            row.add(new Col("上上周涨幅"));
            row.add(new Col("上周涨幅"));
            row.add(new Col("两周涨幅"));
            row.add(new Col("上上月涨幅"));
            row.add(new Col("上月涨幅"));
            row.add(new Col("上上月换手"));
            row.add(new Col("上月换手"));
            row.add(new Col("上上月上影线"));
            row.add(new Col("上月上影线"));
            row.add(new Col("两月涨幅"));
            row.add(new Col("周MIN涨幅"));
            row.add(new Col("月MIN涨幅"));
            row.add(new Col("MIN涨幅"));

            //死叉
            row.add(new Col("死叉"));

            //mtP3	mtP2	mtP1	mtP0	mtZf
            row.add(new Col("mtP3"));
            row.add(new Col("mtP2"));
            row.add(new Col("mtP1"));
            row.add(new Col("mtP0"));
            row.add(new Col("mtZf"));

            //换手2	换手1	换手0
            row.add(new Col("换手2"));
            row.add(new Col("换手1"));
            row.add(new Col("换手0"));
//            row.add(new Col("换手"));

            row.add(new Col("月压力数"));
            //月压力30	月压力60	月压力120	月压力250
            row.add(new Col("月压力30"));
            row.add(new Col("月压力60"));
            row.add(new Col("月压力120"));
            row.add(new Col("月压力250"));
//            row.add(new Col("月压力"));

            row.add(new Col("周压力数"));
//            周压力30	周压力60	周压力120	周压力250
            row.add(new Col("周压力30"));
            row.add(new Col("周压力60"));
            row.add(new Col("周压力120"));
            row.add(new Col("周压力250"));
//            row.add(new Col("周压力"));

            //日压力数	日压力30	日压力60	日压力120	日压力250	日压力
            row.add(new Col("日压力数"));
            row.add(new Col("日压力30"));
            row.add(new Col("日压力60"));
            row.add(new Col("日压力120"));
            row.add(new Col("日压力250"));
//            row.add(new Col("日压力"));

            //月MA数	月MA30	月MA60	月MA120	月MA250	月MA
            row.add(new Col("月MA数"));
            row.add(new Col("月MA30"));
            row.add(new Col("月MA60"));
            row.add(new Col("月MA120"));
            row.add(new Col("月MA250"));
//            row.add(new Col("月MA"));

            //周MA数	周MA30	周MA60	周MA120	周MA250	周MA
            row.add(new Col("周MA数"));
            row.add(new Col("周MA30"));
            row.add(new Col("周MA60"));
            row.add(new Col("周MA120"));
            row.add(new Col("周MA250"));
//            row.add(new Col("周MA"));

            //月MINMA支撑30	月MINMA支撑60	月MINMA支撑120	月MINMA支撑250	月MINMA支撑
            row.add(new Col("月MINMA支撑30"));
            row.add(new Col("月MINMA支撑60"));
            row.add(new Col("月MINMA支撑120"));
            row.add(new Col("月MINMA支撑250"));
//            row.add(new Col("月MINMA支撑"));

            //周MINMA支撑30	周MINMA支撑60	周MINMA支撑120	周MINMA支撑250	周MINMA支撑
            row.add(new Col("周MINMA支撑30"));
            row.add(new Col("周MINMA支撑60"));
            row.add(new Col("周MINMA支撑120"));
            row.add(new Col("周MINMA支撑250"));
//            row.add(new Col("周MINMA支撑"));


            //日MINMA支撑30	日MINMA支撑60	日MINMA支撑120	日MINMA支撑250	日MINMA支撑
            row.add(new Col("日MINMA支撑30"));
            row.add(new Col("日MINMA支撑60"));
            row.add(new Col("日MINMA支撑120"));
            row.add(new Col("日MINMA支撑250"));
//            row.add(new Col("日MINMA支撑"));

            //月MINMA压力30	月MINMA压力60	月MINMA压力120	月MINMA压力250	月MINMA压力
            row.add(new Col("月MINMA压力30"));
            row.add(new Col("月MINMA压力60"));
            row.add(new Col("月MINMA压力120"));
            row.add(new Col("月MINMA压力250"));
//            row.add(new Col("月MINMA压力"));

            //周MINMA压力30	周MINMA压力60	周MINMA压力120	周MINMA压力250	周MINMA压力
            row.add(new Col("周MINMA压力30"));
            row.add(new Col("周MINMA压力60"));
            row.add(new Col("周MINMA压力120"));
            row.add(new Col("周MINMA压力250"));
//            row.add(new Col("周MINMA压力"));

            //日MINMA压力30	日MINMA压力60	日MINMA压力120	日MINMA压力250	日MINMA压力
            row.add(new Col("日MINMA压力30"));
            row.add(new Col("日MINMA压力60"));
            row.add(new Col("日MINMA压力120"));
            row.add(new Col("日MINMA压力250"));
//            row.add(new Col("日MINMA压力"));


            //50日跌幅	avgk120	avgk250	10hor3day	20hor4day	20hor5day	20hor6day 10hor3day	20hor4day	20hor5day	20hor6day
            row.add(new Col("50日跌幅"));
            row.add(new Col("avgk120"));
            row.add(new Col("avgk250"));

            row.add(new Col("10hor3day"));
            row.add(new Col("20hor4day"));
            row.add(new Col("20hor5day"));
            row.add(new Col("20hor6day"));

            row.add(new Col("10hor3day_frac"));
            row.add(new Col("20hor4day_frac"));
            row.add(new Col("20hor5day_frac"));
            row.add(new Col("20hor6day_frac"));

            //bottom	bottom frac	bottom zf	wbottom	wbottom frac	wbottom zf	wbottombz	wbottombz frac	wbottombz zf
            row.add(new Col("bottom"));
            row.add(new Col("bottom frac"));
            row.add(new Col("bottom zf"));
            row.add(new Col("wbottom"));
            row.add(new Col("wbottom frac"));
            row.add(new Col("wbottom zf"));
            row.add(new Col("wbottombz"));
            row.add(new Col("wbottombz frac"));
            row.add(new Col("wbottombz zf"));

            //prev0分时	prev1分时	prev2分时	prev3分时	prev4分时
            row.add(new Col("prev0分时"));
            row.add(new Col("prev1分时"));
            row.add(new Col("prev2分时"));
            row.add(new Col("prev3分时"));
            row.add(new Col("prev4分时"));

            //加速天序号	加速天量	减速天序号	减速天量
            row.add(new Col("加速天序号"));
            row.add(new Col("加速天量"));
            row.add(new Col("减速天序号"));
            row.add(new Col("减速天量"));
            row.add(new Col("fistMinute"));
            row.add(new Col("5Point"));
            row.add(new Col("LFT"));
            row.add(new Col("RGT"));
            row.add(new Col("MA120Price"));

            row.add(new Col("是否跳空" ));

            row.add(new Col("mVPrev2" ));
            row.add(new Col("mVPrev1" ));
            row.add(new Col("mVPrev0" ));
            row.add(new Col("specialHor" ));
            row.add(new Col("maxzf" ));

            row.add(new Col("v900(0)" ));
            row.add(new Col("v900(1)" ));
            row.add(new Col("v900(2)" ));
            row.add(new Col("v900(3)" ));
            row.add(new Col("v900(4)" ));
            row.add(new Col("max" ));
            row.add(new Col("curMinute" ));

            row.add(new Col(" mprev(2)" ));
            row.add(new Col(" mprev(1)" ));
            row.add(new Col(" mprev(0)" ));
            row.add(new Col("PrevMA250Month(2)" ));
            row.add(new Col("PrevMA250Month(1)" ));
            row.add(new Col("PrevMA250Month(0)" ));
            row.add(new Col("PrevMA120Month(2)" ));
            row.add(new Col("PrevMA120Month(1)" ));
            row.add(new Col("PrevMA120Month(0)" ));
            row.add(new Col("PrevMA60Month(2)" ));
            row.add(new Col("PrevMA60Month(1)" ));
            row.add(new Col("PrevMA60Month(0)" ));
            //PrevMA250Month(0) > -4 err
            //PrevMA60Month(0)  > -4.8 err
            //PrevMA60Month(1)  > -3.5 err

            row.add(new Col("bottomup" ));

        } catch (Exception e) {
            e.printStackTrace();
//                            throw new RuntimeException(e);
        }
        return row;
    }

}
