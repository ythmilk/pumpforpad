package com.data;

import java.io.Serializable;

/** 完整统计值数据结构 */
public class StatisticValueS implements Serializable{
	/**小于参考值次数*/
	private String LowerNumber;
	/**大于参考值次数*/
	private String SuperNumber;
	/**最高持续时间*/
	private String Maxduration;
	/**最低持续时间*/
	private String Minduration;
	/**平均持续时间*/
	private String Avgduration;
	/**大于指定参数值得最高持续时间*/
	private String MaxdurationOfTop;
	/**大于指定参数值得最低持续时间*/
	private String MindurationOfTop;
	/**大于指定参考值得平均持续时间*/
	private String AvgdurationOfTop;
	/**小于指定值得最高持续时间*/
	private String MaxdurationOfButtom;
	/**小于指定值的最低持续时间*/
	private String MindurationOfButtom;
	/**小于指定值得平均持续时间*/
	private String AvgdurationOfButtom;
private static final long serialVersionUID=1l;
	public String getLowerNumber() {
		return LowerNumber;
	}

	public void setLowerNumber(String lowerNumber) {
		LowerNumber = lowerNumber;
	}

	public String getSuperNumber() {
		return SuperNumber;
	}

	public void setSuperNumber(String superNumber) {
		SuperNumber = superNumber;
	}

	public String getMaxduration() {
		return Maxduration;
	}

	public void setMaxduration(String maxduration) {
		Maxduration = maxduration;
	}

	public String getMinduration() {
		return Minduration;
	}

	public void setMinduration(String minduration) {
		Minduration = minduration;
	}

	public String getAvgduration() {
		return Avgduration;
	}

	public void setAvgduration(String avgduration) {
		Avgduration = avgduration;
	}

	public String getMaxdurationOfTop() {
		return MaxdurationOfTop;
	}

	public void setMaxdurationOfTop(String maxdurationOfTop) {
		MaxdurationOfTop = maxdurationOfTop;
	}

	public String getMindurationOfTop() {
		return MindurationOfTop;
	}

	public void setMindurationOfTop(String mindurationOfTop) {
		MindurationOfTop = mindurationOfTop;
	}

	public String getAvgdurationOfTop() {
		return AvgdurationOfTop;
	}

	public void setAvgdurationOfTop(String avgdurationOfTop) {
		AvgdurationOfTop = avgdurationOfTop;
	}

	public String getMaxdurationOfButtom() {
		return MaxdurationOfButtom;
	}

	public void setMaxdurationOfButtom(String maxdurationOfButtom) {
		MaxdurationOfButtom = maxdurationOfButtom;
	}

	public String getMindurationOfButtom() {
		return MindurationOfButtom;
	}

	public void setMindurationOfButtom(String mindurationOfButtom) {
		MindurationOfButtom = mindurationOfButtom;
	}

	public String getAvgdurationOfButtom() {
		return AvgdurationOfButtom;
	}

	public void setAvgdurationOfButtom(String avgdurationOfButtom) {
		AvgdurationOfButtom = avgdurationOfButtom;
	}
}
