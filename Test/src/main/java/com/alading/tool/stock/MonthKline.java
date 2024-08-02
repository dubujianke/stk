package com.alading.tool.stock;

import com.huaien.core.util.DateUtil;
import com.alading.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthKline extends Kline{

	public String date;
	public long volume;

	public int year;
	public int month;
	public String key;
	public List<Kline> mdays = new ArrayList<>();

	public List<MonthKline> allLineList = new ArrayList();

	public MonthKline(String line) {
		super(line);
	}

	public MonthKline() {
	}

	public void clear() {
		mdays = null;
		allLineList = null;
	}

	public Date getDateObj() {
		return DateUtil.stringToDate4(key+"-01");
	}

	public void add(Kline kline) {
		mdays.add(kline);
	}

	public int minIdx = 0;
	public int maxIdx = 0;

	public void init(int idx) {
		double max = 0;
		double min = 99999;
		if(mdays.size()==0) {
			return;
		}
		if(key.equalsIgnoreCase("2023-05") && idx == mdays.size()-1) {
			int a = 0;
		}
		Kline ckline = mdays.get(idx);
		ckline.setMopen(mdays.get(0).getOpen());
		ckline.setMclose(mdays.get(idx).getClose());
		for(int i=0; i<=idx; i++) {
			Kline kline = mdays.get(i);
			if(kline.getMin() < min) {
				min = kline.getMin();
			}
			if(kline.getMax() > max) {
				max = kline.getMax();
			}
		}
		ckline.setMmin(min);
		ckline.setMmax(max);

	}

	public void init() {
		double max = 0;
		double min = 99999;
		if(mdays.size()==0) {
			if(key.equalsIgnoreCase("2016-06")) {
				int a = 0;
			}
			return;
		}

		setOpen(mdays.get(0).getOpen());
		setClose(mdays.get(mdays.size() - 1).getClose());
		for(int i = 0; i< mdays.size(); i++) {
			Kline kline = mdays.get(i);
			if(kline.getMin() < min) {
				min = kline.getMin();
				minIdx = i;
			}
			if(kline.getMax() > max) {
				max = kline.getMax();
				maxIdx = i;
			}
		}

		this.setMin(min);
		this.setMax(max);

		for(int i = 0; i< mdays.size(); i++) {
			init(i);
		}

		for(int i = 0; i< mdays.size(); i++) {
			Kline kline = mdays.get(i);
			kline.monthKline = this;
		}
	}

	public boolean isCloseDyOpen() {
		return getClose() > getOpen();
	}

	public double getZhangfu() {
		if(getIdx() - 1 < 0) {
			return 0;
		}
		Kline day0 = allLineList.get(getIdx());
		Kline day1 = allLineList.get(getIdx() - 1);
		double ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
		return ret;
	}

	public String getZhangfuStr() {
		double zf = getZhangfu();
		return StringUtil.spaceString(StringUtil.format(zf,1),5);
	}

	public double getZhangfu2() {
		MonthKline day0 = prev();
		MonthKline day1 = this;
		if(day0 == null) {
			return 1000;
		}
		double ret = 100 * (day1.close - day0.close) / day0.close;
		return ret;
	}

	public String getZhangfuStr2() {
		double zf = getZhangfu2();
		return StringUtil.spaceString(StringUtil.format(zf,2),5);
	}

	public double getMonthEntityZhenfu() {
		Kline day0 = allLineList.get(getIdx());
		double ret = Math.abs(100 * (day0.getClose() - day0.getOpen()) / day0.getOpen());
		return ret;
	}

	public double getAvg20() {
		double total = 0;
		for(int i=0; i<20; i++) {
			Kline day = allLineList.get(getIdx());
			total += day.getClose();
		}
		total = total/20;
		return total;
	}

	public double getMaxBefore(int num) {
		double max = 0;
		int maxIdx = 0;
		for(int i = getIdx() -2; i>= getIdx() -num; i--) {
			Kline day = allLineList.get(i);
			if(day.getEntityMax()>max) {
				max = day.getEntityMax();
				maxIdx = i;
			}
		}
		return max;
	}

	public boolean isWrapAfter(int toOffset) {
		return isWrapAfter(getMin(), getMax(), toOffset);
	}


	public boolean isWrapAfter(double min, double max, int toOffset) {
		int len = toOffset - getIdx();
		if(len<5) {
			return false;
		}
		int cnt = 0;
		for(int i = getIdx() +1; i<=toOffset; i++) {
			Kline day = allLineList.get(i);
			if(this.contain(day)) {
				cnt++;
			}
		}
		int fraction = (int) (1.0f*cnt/len*100);
		if(fraction > 80) {
			return true;
		}
		return false;
	}


	public boolean isVolumStepDownAfter(int toOffset) {
		int len = toOffset - getIdx();
		if(len<5) {
			return false;
		}
		int cnt = 0;
		for(int i = getIdx() +1; i<=toOffset; i++) {
			Kline day = allLineList.get(i);
			if(this.containVolume(day)) {
				cnt++;
			}
		}
		int fraction = (int) (1.0f*cnt/len*100);
		if(fraction > 90) {
			return true;
		}
		return false;
	}


	public double getZhangfu(int num) {
		Kline day0 = allLineList.get(getIdx());
		Kline day1 = allLineList.get(getIdx() -num);
		double ret = 100 * (day0.getClose() - day1.getClose()) / day1.getClose();
		return ret;
	}

	public double getVolFraction() {
		try {
			Kline day0 = allLineList.get(getIdx());
			Kline day1 = allLineList.get(getIdx() - 1);
			double ret = day0.getVolume() / day1.getVolume();
			return ret;
		} catch (Exception e) {

		}
		return 0.1f;
	}

	public MonthKline prev() {
		if(getIdx() -1<0) {
			return null;
		}
		return allLineList.get(getIdx() -1);
	}

	public MonthKline prev(int i) {
		if(getIdx() -i < 0) {
			int a = 0;
			a++;
			return null;
		}
		return allLineList.get(getIdx() -i);
	}

	public MonthKline next() {
		return allLineList.get(getIdx() +1);
	}

	public MonthKline next(int i) {
		return allLineList.get(getIdx() +i);
	}

	public String toString() {
		return key + " open:"+ getOpen() +" close:"+ getClose() + " max:"+ getMax() +" min"+ getMin();
	}

	public String getDateYM() {
		return key;
	}


	public double getPrevZF2(int n) {
		double max = 0;
		for (int i = 0; i < n; i++) {
			Kline item = prev(i + 1);
			if (item == null) {
				break;
			}
			double v =item.getZhangfu();
			if (v > max) {
				max = v;
			}
		}
		return max;
	}


	public boolean hasDZ(int num, double frac) {
		double fracV = this.getPrevZF2(num);
		if(fracV>frac) {
			return true;
		}
		return false;
	}

	public String getDate() {
		return key;
	}


	public double getChangeHand(LineContext context) {
		double total = 0;
		for (int i = 0; i < mdays.size(); i++) {
			Kline kline = mdays.get(i);
			double hand = kline.getHandD(context.getTotalV());
			total+= hand;
		}
		return total;
	}

	public boolean isCrashDownMAI2(int period) {
		double ma120 = getMAI(period);
		if (open > ma120 && close < ma120  && KLineUtil.compareMax(close, open)>10 && KLineUtil.compareMax(close, min)<1f && KLineUtil.compareMax(close, ma120)>=0.5f) {
			return true;
		}
		return false;
	}


	public int getByIdx(int idx) {
		for (int i = 0; i < mdays.size(); i++) {
			Kline kline1 = mdays.get(i);
			if (kline1.getIdx() == idx) {
				return i;
			}
		}
		return -1;
	}

	private int curDayIdx;
	public double getMax() {
		if (getCurDayIdx() == -1) {
			return super.getMax();
		}

		double max = 0;
		if (mdays.size() == 0) {
			return 0;
		}
		if (key.equalsIgnoreCase("2023-05")) {
			int a = 0;
		}
		int idx = getByIdx(getCurDayIdx());
		for (int i = 0; i <= idx; i++) {
			Kline kline = mdays.get(i);
			if (AbsStragety.CURDAY_OPEN) {
				if (i == idx) {
					if (kline.getOpen() > max) {
						max = kline.getMax();
					}
				} else {
					if (kline.getMax() > max) {
						max = kline.getMax();
					}
				}
			} else {
				if (kline.getMax() > max) {
					max = kline.getMax();
				}
			}
		}
		return max;
	}

	public int getCurDayIdx() {
		return curDayIdx;
	}

	public void setCurDayIdx(int curDayIdx) {
		this.curDayIdx = curDayIdx;
	}
}
