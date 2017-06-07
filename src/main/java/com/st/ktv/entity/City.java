package com.st.ktv.entity;

public class City {
	private String country;//国家
	private String area;//地区
	private String province;//省份
	private String city;//城市
	private String county;//县
	private String isp;//网络：电信、移动	
	private boolean flag;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public City() {
		super();
	}

	public City(String country, String area, String province, String city,
			String county, String isp, boolean flag) {
		super();
		this.country = country;
		this.area = area;
		this.province = province;
		this.city = city;
		this.county = county;
		this.isp = isp;
		this.flag = flag;
	}
	
	
}
