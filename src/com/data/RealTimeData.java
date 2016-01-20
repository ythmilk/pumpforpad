package com.data;

import java.io.Serializable;

/** 实时参数 Json解析数据结构 */
public class RealTimeData extends BaseDataS implements Serializable {
	//
	// public RealTimeData(String Id, String Name) {
	// super(Id, Name);
	// // TODO Auto-generated constructor stub
	// }
	/** 采集时间 */
	private String AcquisitionTime;

	/** 进口压力 */
	private String InletPressure;
	/** 出口压力 */
	private String OutletPressure;
	/** 变频器频率 */
	private String Frequency;
	/** 功率 */
	private String PowerV;
	/** 流量 */
	private String FlowV;
	/** 液位 */
	private String Liquidlevel;

	/** 设备综合状态 */
	private String EquipmentS;
	/** 水泵1状态 */
	private String PumpStatusName1;
	/** 水泵2状态 */
	private String PumpStatusName2;
	/** 水泵3状态 */
	private String PumpStatusName3;
	/** 水泵4状态 */
	private String PumpStatusName4;
	/** 水泵5状态 */
	private String PumpStatusName5;

	public String getAcquisitionTime() {
		return AcquisitionTime;
	}

	public void setAcquisitionTime(String acquisitionTime) {
		AcquisitionTime = acquisitionTime;
	}

	public String getInletPressure() {
		return InletPressure;
	}

	public void setInletPressure(String inletPressure) {
		InletPressure = inletPressure;
	}

	public String getOutletPressure() {
		return OutletPressure;
	}

	public void setOutletPressure(String outletPressure) {
		OutletPressure = outletPressure;
	}

	public String getFrequency() {
		return Frequency;
	}

	public void setFrequency(String frequency) {
		Frequency = frequency;
	}

	public String getPowerV() {
		return PowerV;
	}

	public void setPowerV(String powerV) {
		PowerV = powerV;
	}

	public String getFlowV() {
		return FlowV;
	}

	public void setFlowV(String flowV) {
		FlowV = flowV;
	}

	public String getLiquidlevel() {
		return Liquidlevel;
	}

	public void setLiquidlevel(String liquidlevel) {
		Liquidlevel = liquidlevel;
	}

	public String getEquipmentS() {
		return EquipmentS;
	}

	public void setEquipmentS(String equipmentS) {
		EquipmentS = equipmentS;
	}

	public String getPumpStatusName1() {
		return PumpStatusName1;
	}

	public void setPumpStatusName1(String pumpStatusName1) {
		PumpStatusName1 = pumpStatusName1;
	}

	public String getPumpStatusName2() {
		return PumpStatusName2;
	}

	public void setPumpStatusName2(String pumpStatusName2) {
		PumpStatusName2 = pumpStatusName2;
	}

	public String getPumpStatusName3() {
		return PumpStatusName3;
	}

	public void setPumpStatusName3(String pumpStatusName3) {
		PumpStatusName3 = pumpStatusName3;
	}

	public String getPumpStatusName4() {
		return PumpStatusName4;
	}

	public void setPumpStatusName4(String pumpStatusName4) {
		PumpStatusName4 = pumpStatusName4;
	}

	public String getPumpStatusName5() {
		return PumpStatusName5;
	}

	public void setPumpStatusName5(String pumpStatusName5) {
		PumpStatusName5 = pumpStatusName5;
	}

	/** 根据ID获取泵站名 */
	public String getPumpStatusName(int i) {
		String pumpName = null;
		switch (i) {
		case 1:
			pumpName = PumpStatusName1;

			break;
		case 2:
			pumpName = PumpStatusName2;

			break;
		case 3:
			pumpName = PumpStatusName3;

			break;
		case 4:
			pumpName = PumpStatusName4;

			break;
		case 5:
			pumpName = PumpStatusName5;
			break;
		default:
			break;
		}
		return pumpName;
	}
}
