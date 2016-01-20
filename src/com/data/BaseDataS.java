package com.data;
/**
 * 基础的数据结构，包括ID和Name*/
public class BaseDataS {
	private String ID;
	private String Name;
//public BaseDataS(String Id,String Name){
//	this.ID=ID;
//	this.Name=Name;
//}
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	@Override
	public String toString() {
		return Name;
	}
}
