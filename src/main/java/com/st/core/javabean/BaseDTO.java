package com.st.core.javabean;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseDTO {

	
	private int page;
	private int rows;
	@SuppressWarnings("unused")
	private int start;
	
	private String insertRole;
	private String updateRole;
	private String deleteRole;



	
	
	public String getInsertRole() {
		return insertRole;
	}



	public void setInsertRole(String insertRole) {
		this.insertRole = insertRole;
	}



	public String getUpdateRole() {
		return updateRole;
	}



	public void setUpdateRole(String updateRole) {
		this.updateRole = updateRole;
	}



	public String getDeleteRole() {
		return deleteRole;
	}



	public void setDeleteRole(String deleteRole) {
		this.deleteRole = deleteRole;
	}



	public int getStart() {
		return (page-1)*rows;
	}



	public void setStart(int start) {
		this.start = start;
	}



	public BaseDTO() {
		super();
	}

	

	@Override
	public int hashCode() {
		return new HashCodeBuilder().hashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}


}
