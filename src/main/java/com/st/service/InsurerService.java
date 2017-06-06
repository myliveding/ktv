package com.st.service;

import net.sf.json.JSONObject;

public interface InsurerService {
    
    public JSONObject getProjectInfo();
    public JSONObject getMonthByCondition(int insuranceStart, int fundStart,
            int insuranceingEnd, int fundingEnd, int insuranceFlag,
            int fundFlag, int insuranceSelect, int fundSelect,String insuranceRenew,String fundRenew, int months);

    public JSONObject getMonthBySinbleCondition(int insuranceStart, int fundStart, int insuranceingEnd, int fundingEnd, int insuranceFlag, int fundFlag, int insuranceSelect, int fundSelect, String insuranceRenew, String fundRenew, int months);
}
