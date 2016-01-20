package com.data;

import java.io.Serializable;

/**获得所以泵站的信息*/
public class PumpLocationS extends BaseDataS implements Serializable {
private Double Longitude;
private Double Latitude;
public Double getLongitude() {
	return Longitude;
}
public void setLongitude(Double longitude) {
	Longitude = longitude;
}
public Double getLatitude() {
	return Latitude;
}
public void setLatitude(Double latitude) {
	Latitude = latitude;
}
}
