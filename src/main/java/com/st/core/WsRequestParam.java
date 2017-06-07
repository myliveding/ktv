/**
 * @{#} WsRequestParam.java Created on 2006-11-16 12:43:29
 *
 * Copyright (c) 2006 by WASU.
 */

package com.st.core;

/**
 * @author <a href="mailto:yurz@onewaveinc.com">yurz</a>
 * @version 1.0
 */

public class WsRequestParam  implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.String encryptInfo;

    private java.lang.String extendInfo;

    private java.lang.String requestContent;

    private int requestNo;

    private int requestSystemNo;

    private int versionNo;

    public WsRequestParam() {
    }

    public WsRequestParam(
           java.lang.String encryptInfo,
           java.lang.String extendInfo,
           java.lang.String requestContent,
           int requestNo,
           int requestSystemNo,
           int versionNo) {
           this.encryptInfo = encryptInfo;
           this.extendInfo = extendInfo;
           this.requestContent = requestContent;
           this.requestNo = requestNo;
           this.requestSystemNo = requestSystemNo;
           this.versionNo = versionNo;
    }


    /**
     * Gets the encryptInfo value for this WsRequestParam.
     * 
     * @return encryptInfo
     */
    public java.lang.String getEncryptInfo() {
        return encryptInfo;
    }


    /**
     * Sets the encryptInfo value for this WsRequestParam.
     * 
     * @param encryptInfo
     */
    public void setEncryptInfo(java.lang.String encryptInfo) {
        this.encryptInfo = encryptInfo;
    }


    /**
     * Gets the extendInfo value for this WsRequestParam.
     * 
     * @return extendInfo
     */
    public java.lang.String getExtendInfo() {
        return extendInfo;
    }


    /**
     * Sets the extendInfo value for this WsRequestParam.
     * 
     * @param extendInfo
     */
    public void setExtendInfo(java.lang.String extendInfo) {
        this.extendInfo = extendInfo;
    }


    /**
     * Gets the requestContent value for this WsRequestParam.
     * 
     * @return requestContent
     */
    public java.lang.String getRequestContent() {
        return requestContent;
    }


    /**
     * Sets the requestContent value for this WsRequestParam.
     * 
     * @param requestContent
     */
    public void setRequestContent(java.lang.String requestContent) {
        this.requestContent = requestContent;
    }


    /**
     * Gets the requestNo value for this WsRequestParam.
     * 
     * @return requestNo
     */
    public int getRequestNo() {
        return requestNo;
    }


    /**
     * Sets the requestNo value for this WsRequestParam.
     * 
     * @param requestNo
     */
    public void setRequestNo(int requestNo) {
        this.requestNo = requestNo;
    }


    /**
     * Gets the requestSystemNo value for this WsRequestParam.
     * 
     * @return requestSystemNo
     */
    public int getRequestSystemNo() {
        return requestSystemNo;
    }


    /**
     * Sets the requestSystemNo value for this WsRequestParam.
     * 
     * @param requestSystemNo
     */
    public void setRequestSystemNo(int requestSystemNo) {
        this.requestSystemNo = requestSystemNo;
    }


    /**
     * Gets the versionNo value for this WsRequestParam.
     * 
     * @return versionNo
     */
    public int getVersionNo() {
        return versionNo;
    }


    /**
     * Sets the versionNo value for this WsRequestParam.
     * 
     * @param versionNo
     */
    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    private java.lang.Object equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof WsRequestParam)) return false;
        WsRequestParam other = (WsRequestParam) obj;
        if (this == obj) return true;
        if (equalsCalc != null) {
            return (equalsCalc == obj);
        }
        equalsCalc = obj;
        boolean equals;
        equals =
            ((this.encryptInfo==null && other.getEncryptInfo()==null) || 
             (this.encryptInfo!=null &&
              this.encryptInfo.equals(other.getEncryptInfo()))) &&
            ((this.extendInfo==null && other.getExtendInfo()==null) || 
             (this.extendInfo!=null &&
              this.extendInfo.equals(other.getExtendInfo()))) &&
            ((this.requestContent==null && other.getRequestContent()==null) || 
             (this.requestContent!=null &&
              this.requestContent.equals(other.getRequestContent()))) &&
            this.requestNo == other.getRequestNo() &&
            this.requestSystemNo == other.getRequestSystemNo() &&
            this.versionNo == other.getVersionNo();
        equalsCalc = null;
        return equals;
    }

    private boolean hashCodeCalc = false;
    public synchronized int hashCode() {
        if (hashCodeCalc) {
            return 0;
        }
        hashCodeCalc = true;
        int hashCode = 1;
        if (getEncryptInfo() != null) {
            hashCode += getEncryptInfo().hashCode();
        }
        if (getExtendInfo() != null) {
            hashCode += getExtendInfo().hashCode();
        }
        if (getRequestContent() != null) {
            hashCode += getRequestContent().hashCode();
        }
        hashCode += getRequestNo();
        hashCode += getRequestSystemNo();
        hashCode += getVersionNo();
        hashCodeCalc = false;
        return hashCode;
    }

}
