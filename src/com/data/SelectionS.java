package com.data;

/** 选择统计项的数据结构 */
public class SelectionS {
	private String Id;
	private String Name;

	public SelectionS(String id, String name) {
		this.Id = id;
		this.Name = name;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
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
