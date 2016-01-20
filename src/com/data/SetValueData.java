package com.data;

import java.io.Serializable;

/**恒定参数 JSon解析数据结构*/
public class SetValueData implements Serializable{
	/***/
	String ID;
	String SetTime;
	/**恒压值设定*/
	String SettingPressure;
	/**进水压力底限*/
	String InletPressureLowest;
	/**出水压力高限*/
	String OutletPressureMaximum;
	/**变频器频率高限*/
	String InverterFrequencyMaximum;
	/**变频器功率低限*/
	String InverterPowerLowest;
	/**定期轮换时间*/
	String  TimingRotationTime;
	/**出水压力量程*/
	String PressureFullscale;
	/**泵站数量*/
	int PumpingNum;
	/**有无辅泵*/
	int HaveAuxiliaryPump;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSetTime() {
		return SetTime;
	}
	public void setSetTime(String setTime) {
		SetTime = setTime;
	}
	public String getSettingPressure() {
		return SettingPressure;
	}
	public void setSettingPressure(String settingPressure) {
		SettingPressure = settingPressure;
	}
	public String getInletPressureLowest() {
		return InletPressureLowest;
	}
	public void setInletPressureLowest(String inletPressureLowest) {
		InletPressureLowest = inletPressureLowest;
	}
	public String getOutletPressureMaximum() {
		return OutletPressureMaximum;
	}
	public void setOutletPressureMaximum(String outletPressureMaximum) {
		OutletPressureMaximum = outletPressureMaximum;
	}
	public String getInverterFrequencyMaximum() {
		return InverterFrequencyMaximum;
	}
	public void setInverterFrequencyMaximum(String inverterFrequencyMaximum) {
		InverterFrequencyMaximum = inverterFrequencyMaximum;
	}
	public String getInverterPowerLowest() {
		return InverterPowerLowest;
	}
	public void setInverterPowerLowest(String inverterPowerLowest) {
		InverterPowerLowest = inverterPowerLowest;
	}
	public String getTimingRotationTime() {
		return TimingRotationTime;
	}
	public void setTimingRotationTime(String timingRotationTime) {
		TimingRotationTime = timingRotationTime;
	}
	public String getPressureFullscale() {
		return PressureFullscale;
	}
	public void setPressureFullscale(String pressureFullscale) {
		PressureFullscale = pressureFullscale;
	}
	public int getPumpingNum() {
		return PumpingNum;
	}
	public void setPumpingNum(int pumpingNum) {
		PumpingNum = pumpingNum;
	}
	public int getHaveAuxiliaryPump() {
		return HaveAuxiliaryPump;
	}
	public void setHaveAuxiliaryPump(int haveAuxiliaryPump) {
		HaveAuxiliaryPump = haveAuxiliaryPump;
	}
	
}
