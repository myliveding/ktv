package com.st.javabean.vo;

import java.io.Serializable;

public class AmbitusVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Integer unitid;
    private String unitname;
    private String address;
    private String tel;
    private String lng;
    private String lat;
    private String flagpic;
    private String category;
    private String distance;
    
    
    
    
    
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getUnitid() {
		return unitid;
	}
	public void setUnitid(Integer unitid) {
		this.unitid = unitid;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getFlagpic() {
		return flagpic;
	}
	public void setFlagpic(String flagpic) {
		this.flagpic = flagpic;
	}
	public AmbitusVO(Integer unitid, String unitname, String address,
			String tel, String lng, String lat,String flagpic,String category) {
		super();
		this.unitid = unitid;
		this.unitname = unitname;
		this.address = address;
		this.tel = tel;
		this.lng = lng;
		this.lat = lat;
		this.flagpic=flagpic;
		this.category=category;
	}
	public AmbitusVO() {
		super();
	}
  
    

}
