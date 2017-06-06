package com.st.service.impl;

import com.st.core.util.date.DateUtil;
import com.st.service.InsurerService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
@Transactional
@Service("insurerService")
public class InsurerServiceImpl implements InsurerService {

    public JSONObject getProjectInfo() {

        return null;
    }
    /**
     * 前台交互计算月份  
     * @param insuranceStart   公积金起缴月
     * @param fundStart   社保起缴月
     * @param insuranceingEnd   已保社保结束月
     * @param fundingEnd   已保公积金结束月
     * @param insuranceFlag   社保强制
     * @param fundFlag   公积金强制
     * @param fundSelect   勾选公积金
     * @param insuranceSelect   勾选社保
     * @param insuranceRenewFlag 社保可续费状态 0 可续费  1办理中，2停保中，3 时间未到
     * @param insuranceRenewFlag 公积金可续费状态 0 可续费  1办理中，2停保中，3 时间未到
     * @param months  套餐月数
     * @return HashMap
     * @throws  
     */
    public JSONObject getMonthByCondition(int insuranceStart, int fundStart,
            int insuranceingEnd, int fundingEnd, int insuranceFlag,
            int fundFlag, int insuranceSelect, int fundSelect,String insuranceRenewFlag,String fundRenewFlag,int months) {
        JSONObject resultObjStr = new JSONObject();
        HashMap<String,Integer> result =new HashMap<String, Integer>();
        HashMap<String,String> serviceMonth =new HashMap<String, String>();
        
        String insuranceStartStr=DateUtil.convertToDate(insuranceStart);
        String fundStartStr=DateUtil.convertToDate(fundStart);
        String insuranceingEndStr=DateUtil.convertToDate(insuranceingEnd);
        String fundingEndStr=DateUtil.convertToDate(fundingEnd);
        String insuranceStartResult="1970-01-01";
        String fundStartResult="1970-01-01";
        String insuranceEndResult="1970-01-01";
        String fundEndResult="1970-01-01";
        int insuranceRenew=0;
        int fundRenew=0;
        String tempStart="";
        if(insuranceStart==0&&insuranceingEnd!=0){
            insuranceStartStr=DateUtil.addMonth(insuranceingEndStr, 1);
        }
        if(fundStart==0&&fundingEnd!=0){
            fundStartStr=DateUtil.addMonth(fundingEndStr, 1);
        }
        if(DateUtil.monthCompare(insuranceStartStr, fundStartStr)>0){
            tempStart=insuranceStartStr;
        }else{
            tempStart=fundStartStr;
        }
        if(insuranceFlag==1&&fundFlag==1){
            if(insuranceingEnd==0&&fundingEnd==0){
                insuranceStartResult=tempStart;
                fundStartResult=tempStart;
                insuranceEndResult=DateUtil.addMonth(tempStart, months-1);
                fundEndResult=DateUtil.addMonth(tempStart, months-1);
            }else if(insuranceingEnd==0){
                insuranceStartResult=insuranceStartStr;
                fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                if(insuranceStart!=0){
                    serviceMonth=DateUtil.serviceMonthCalc(DateUtil.addMonth(insuranceStartStr, -1),fundingEndStr,months);
                    months=Integer.valueOf(serviceMonth.get("months"));
                    insuranceEndResult=serviceMonth.get("timeEnd");
                    fundEndResult=serviceMonth.get("timeEnd");
                    if(DateUtil.monthCompare(fundingEndStr, fundEndResult)>0){
                        if (!fundRenewFlag.equals("2")){ //停保办理中的强制项不追加
                            fundRenew=1;
                        }
                    }
                }else{
                    insuranceEndResult=DateUtil.addMonth(tempStart, 2);
                    fundEndResult=insuranceEndResult;
                }
                
            }else if(fundingEnd==0){
                fundStartResult=fundStartStr;
                insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                if(fundStart!=0){
                    serviceMonth=DateUtil.serviceMonthCalc(DateUtil.addMonth(fundStartStr, -1),insuranceingEndStr,months);
                    months=Integer.valueOf(serviceMonth.get("months"));
                    insuranceEndResult=serviceMonth.get("timeEnd");
                    if(DateUtil.monthCompare(insuranceingEndStr, insuranceEndResult)>0){
                        if (!insuranceRenewFlag.equals("2")){
                            insuranceRenew=1;
                        }
                    }
                    fundEndResult=serviceMonth.get("timeEnd");
                }else{
                    fundEndResult=DateUtil.addMonth(tempStart, 2);
                    insuranceEndResult=fundEndResult;
                }
            }else{
                insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                serviceMonth=DateUtil.serviceMonthCalc(insuranceingEndStr,fundingEndStr,months);
                months=Integer.valueOf(serviceMonth.get("months"));
                insuranceEndResult=serviceMonth.get("timeEnd");
                if(DateUtil.monthCompare(insuranceingEndStr, insuranceEndResult)>0){
                    if (!insuranceRenewFlag.equals("2")){
                        insuranceRenew=1;
                    }
                }
                fundEndResult=serviceMonth.get("timeEnd");
                if(DateUtil.monthCompare(fundingEndStr, fundEndResult)>0){
                    if (!fundRenewFlag.equals("2")){
                        fundRenew=1;
                    }
                }
            }
        }
        if(insuranceFlag==1&&(fundFlag==0||fundFlag==2)){
            if(insuranceingEnd>0){
                insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
            }else{
                insuranceStartResult=insuranceStartStr;
            }
            String insuranceEndTemp="1970-01-01";
            if(insuranceSelect==1){//勾选强制项
                if(insuranceingEnd>0){
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                }else{
                    insuranceStartResult=insuranceStartStr;//ttttt
                }
                if(fundingEnd>0&&DateUtil.monthCompare(insuranceStartResult,fundingEndStr)>0){
                    insuranceEndTemp=fundingEndStr;
                }else{
                    insuranceEndTemp=DateUtil.addMonth(insuranceStartResult, months-1);
                }
                if(insuranceingEnd>0){
                    serviceMonth=DateUtil.serviceMonthCalc(insuranceingEndStr,insuranceEndTemp,months);
                    months=Integer.valueOf(serviceMonth.get("months"));
                    insuranceEndResult=serviceMonth.get("timeEnd");
                }else{
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }
            }
           
            if(fundSelect==1){
                if(fundingEnd>0){
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }else{
                    fundStartResult=fundStartStr;
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }
                if(!insuranceEndResult.equals("1970-01-01")&&DateUtil.monthCompare(fundEndResult,insuranceEndResult)<0){
                    fundEndResult=insuranceEndResult;
                }
                if(insuranceSelect==2&&DateUtil.monthCompare(fundEndResult,insuranceingEndStr)<0){
                    if (!insuranceRenewFlag.equals("2")){
                        insuranceRenew=1;
                    }
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                    insuranceEndResult=fundEndResult;
                }
            }
        } 
        if(fundFlag==1&&insuranceFlag==0){  //155300
            if(fundingEnd>0){
                fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
            }else{
                fundStartResult=fundStartStr;
            }
            String fundEndTemp="1970-01-01";
            if(fundSelect==1){//勾选强制项
                if(fundingEnd>0){
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                }else{
                    fundStartResult=fundStartStr;//ttttt
                }
                if(insuranceingEnd>0&&DateUtil.monthCompare(fundStartResult,insuranceingEndStr)>0){
                    fundEndTemp=insuranceingEndStr;
                }else{
                    fundEndTemp=DateUtil.addMonth(fundStartResult, months-1);
                }
                if(fundingEnd>0){
                    serviceMonth=DateUtil.serviceMonthCalc(fundingEndStr,fundEndTemp,months);
                    months=Integer.valueOf(serviceMonth.get("months"));
                    fundEndResult=serviceMonth.get("timeEnd");
                }else{
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }
            }
            if(insuranceSelect==1){
                if(insuranceingEnd>0){
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }else{
                    insuranceStartResult=insuranceStartStr;
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }
                if(!fundEndResult.equals("1970-01-01")&&DateUtil.monthCompare(insuranceEndResult,fundEndResult)<0){
                    insuranceEndResult=fundEndResult;
                }
                if(fundSelect==2&&DateUtil.monthCompare(insuranceEndResult,fundingEndStr)<0){
                    if (!fundRenewFlag.equals("2")){
                        fundRenew=1;
                    }
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                    fundEndResult=insuranceEndResult;
                }
            }
        } 
        if(insuranceFlag==0&&fundFlag==0){
            if(insuranceSelect==1){
                if(insuranceingEnd>0){
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }else{
                    insuranceStartResult=insuranceStartStr;
                    insuranceEndResult=DateUtil.addMonth(insuranceStartStr, months-1);
                }
            }
            if(fundSelect==1){
                if(fundingEnd>0){
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }else{
                    fundStartResult=fundStartStr;
                    fundEndResult=DateUtil.addMonth(fundStartStr, months-1);
                }
            }
        }
        String alertStr="";
        if(insuranceSelect==1&&fundSelect==1){
            if(insuranceFlag==1&&fundFlag==1&&(DateUtil.monthCompare(fundStartResult,fundEndResult)!=(months-1)||DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)!=(months-1))){
                alertStr="因政策原因，社保与公积金强制缴纳，两项业务的参保截止月须一致，参保截止月=两项业务起缴月的小值+参保套餐";
            }else if(insuranceFlag==1){
                if(!fundStartResult.equals("1970-01-01")&&DateUtil.monthCompare(fundStartResult,fundEndResult)!=(months-1)){
                    alertStr="因政策原因，社保强制缴纳，两项业务的参保截止月须一致，参保截止月=两项业务起缴月的小值+参保套餐";
                }
                
            }else if(fundFlag==1){
                if(!insuranceStartResult.equals("1970-01-01")&&DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)!=(months-1)){
                    alertStr="因政策原因，公积金强制缴纳，两项业务的参保截止月须一致，参保截止月=两项业务起缴月的小值+参保套餐";
                }
            }
            if(fundStartResult.equals(insuranceStartResult)){
                alertStr="";
            }
        }else if(insuranceSelect==2||fundSelect==2){
            if(fundFlag==1&&fundRenew==1){
                alertStr="因政策原因，公积金强制缴纳，两项业务的参保截止月须一致，参保截止月=两项业务起缴月的小值+参保套餐";
            }else if(insuranceFlag==1&&insuranceRenew==1){
                alertStr="因政策原因，社保强制缴纳，两项业务的参保截止月须一致，参保截止月=两项业务起缴月的小值+参保套餐";
            }
        }
            if(DateUtil.monthCompare(insuranceStartResult, insuranceEndResult)<0){//比较计算得到的起止月，当起始月大于截止月时，取0值
              result.put("insuranceStart", 0);
              result.put("insuranceEnd", 0);
            }else{
              result.put("insuranceStart", DateUtil.convertToInt0(insuranceStartResult));
              result.put("insuranceEnd", DateUtil.convertToInt0(insuranceEndResult));
            }
            if(DateUtil.monthCompare(fundStartResult, fundEndResult)<0){
              result.put("fundStart", 0);
              result.put("fundEnd", 0);
            }else{
              result.put("fundStart", DateUtil.convertToInt0(fundStartResult));
              result.put("fundEnd", DateUtil.convertToInt0(fundEndResult));
          }
          resultObjStr.put("status", 0);
          HashMap<String, Integer> resultMap=new HashMap<String, Integer>();//公积金续费月份
          resultMap.put("renew_start", result.get("insuranceStart"));
          resultMap.put("renew_end", result.get("insuranceEnd"));
          resultObjStr.put("socialSecurity_renew", resultMap);
          resultMap.put("renew_start", result.get("fundStart"));
          resultMap.put("renew_end", result.get("fundEnd"));
          resultObjStr.put("fund_renew", resultMap);
          resultObjStr.put("socialSecurity_renew_flag", insuranceRenew);
          resultObjStr.put("fund_renew_flag", fundRenew);
          resultObjStr.put("renew_tips", alertStr);//
         resultObjStr.put("press", 1);
         return resultObjStr;
    }

    /**
     * 前台交互计算月份  单月套餐专用
     * @param insuranceStart   公积金起缴月
     * @param fundStart   社保起缴月
     * @param insuranceingEnd   已保社保结束月
     * @param fundingEnd   已保公积金结束月
     * @param insuranceFlag   社保强制  0不强制； 1强制
     * @param fundFlag   公积金强制
     * @param fundSelect   勾选公积金  1选择；2不可选
     * @param insuranceSelect   勾选社保
     * @param insuranceRenewFlag 社保可续费状态 0 可续费  1办理中，2停保中，3 时间未到
     * @param insuranceRenewFlag 公积金可续费状态 0 可续费  1办理中，2停保中，3 时间未到
     * @param months  套餐月数
     * @return HashMap
     * @throws
     */
    @Override
    public JSONObject getMonthBySinbleCondition(int insuranceStart, int fundStart, int insuranceingEnd, int fundingEnd, int insuranceFlag, int fundFlag, int insuranceSelect, int fundSelect, String insuranceRenewFlag, String fundRenewFlag, int months) {
        JSONObject resultObjStr = new JSONObject();
        HashMap<String,Integer> result =new HashMap<String, Integer>();
        HashMap<String,String> serviceMonth =new HashMap<String, String>();
        Integer press=1;
        String insuranceStartStr=DateUtil.convertToDate(insuranceStart);
        String fundStartStr=DateUtil.convertToDate(fundStart);
        String insuranceingEndStr=DateUtil.convertToDate(insuranceingEnd);
        String fundingEndStr=DateUtil.convertToDate(fundingEnd);
        String insuranceStartResult="1970-01-01";
        String fundStartResult="1970-01-01";
        String insuranceEndResult="1970-01-01";
        String fundEndResult="1970-01-01";
        int insuranceRenew=0;
        int fundRenew=0;
        String tempStart="";
        if(insuranceStart==0&&insuranceingEnd!=0){
            insuranceStartStr=DateUtil.addMonth(insuranceingEndStr, 1);
        }
        if(fundStart==0&&fundingEnd!=0){
            fundStartStr=DateUtil.addMonth(fundingEndStr, 1);
        }
        if(DateUtil.monthCompare(insuranceStartStr, fundStartStr)>0){
            tempStart=insuranceStartStr;
        }else{
            tempStart=fundStartStr;
        }
        if(insuranceFlag==1&&fundFlag==1){
            if(insuranceingEnd==0&&fundingEnd==0){
                insuranceStartResult=tempStart;
                fundStartResult=tempStart;
                insuranceEndResult=DateUtil.addMonth(tempStart, months-1);
                fundEndResult=DateUtil.addMonth(tempStart, months-1);
            }else if(insuranceingEnd==0){//社保无在保
                insuranceStartResult=insuranceStartStr;
                fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                if(insuranceStart!=0){
                    if (fundRenew==0&&fundingEnd>0){//公积金在保
                        if (fundSelect==2){//公积金不可续
                            insuranceEndResult=fundingEndStr;
                        }else{//都强制，公积金可续费，社保为新增
                            fundEndResult=DateUtil.addMonth(fundingEndStr,months);
                            insuranceEndResult=DateUtil.addMonth(fundingEndStr,months);
                        }
                    }else {
                        serviceMonth = DateUtil.serviceMonthCalc(DateUtil.addMonth(insuranceStartStr, -1), fundingEndStr, months);
                        months = Integer.valueOf(serviceMonth.get("months"));
                        insuranceEndResult = serviceMonth.get("timeEnd");
                        fundEndResult = serviceMonth.get("timeEnd");
                        if (DateUtil.monthCompare(fundingEndStr, fundEndResult) > 0) {
                            if (!fundRenewFlag.equals("2")) { //停保办理中的强制项不追加
                                fundRenew = 1;
                            }
                        }
                    }
                }else{
                    insuranceEndResult=DateUtil.addMonth(tempStart, months-1);
                    fundEndResult=insuranceEndResult;
                }
            }else if(fundingEnd==0){//公积金无在保
                fundStartResult=fundStartStr;
                insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                if(fundStart!=0){
                    if (insuranceRenew==0&&insuranceingEnd>0){//社保在保
                        if(insuranceSelect==2){//社保不可选
                            fundEndResult=insuranceingEndStr;
                        }else { //都强制，社保可续费，公积金为新增
                            fundEndResult=DateUtil.addMonth(insuranceingEndStr,months);
                            insuranceEndResult=DateUtil.addMonth(insuranceingEndStr,months);
                        }
                    }else {
                        serviceMonth = DateUtil.serviceMonthCalc(DateUtil.addMonth(fundStartStr, -1), insuranceingEndStr, months);
                        months = Integer.valueOf(serviceMonth.get("months"));
                        insuranceEndResult = serviceMonth.get("timeEnd");
                        if (DateUtil.monthCompare(insuranceingEndStr, insuranceEndResult) > 0) {
                            if (!insuranceRenewFlag.equals("2")) {
                                insuranceRenew = 1;
                            }
                        }
                        fundEndResult = serviceMonth.get("timeEnd");
                    }
                }else{
                    fundEndResult=DateUtil.addMonth(tempStart, months-1);
                    insuranceEndResult=fundEndResult;
                }
            }else{
                insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                serviceMonth=DateUtil.serviceMonthCalc(insuranceingEndStr,fundingEndStr,months);
                months=Integer.valueOf(serviceMonth.get("months"));
                insuranceEndResult=serviceMonth.get("timeEnd");
                if(DateUtil.monthCompare(insuranceingEndStr, insuranceEndResult)>0){
                    if (!insuranceRenewFlag.equals("2")){
                        insuranceRenew=1;
                    }
                }
                fundEndResult=serviceMonth.get("timeEnd");
                if(DateUtil.monthCompare(fundingEndStr, fundEndResult)>0){
                    if (!fundRenewFlag.equals("2")){
                        fundRenew=1;
                    }
                }
            }
        }
        if(insuranceFlag==1&&(fundFlag==0||fundFlag==2)){
            if(insuranceingEnd>0){
                insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
            }else{
                insuranceStartResult=insuranceStartStr;
            }
            String insuranceEndTemp="1970-01-01";
            if(insuranceSelect==1){//勾选强制项
                if(insuranceingEnd>0){
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                }else{
                    insuranceStartResult=insuranceStartStr;//ttttt
                }
                if(fundingEnd>0&&DateUtil.monthCompare(insuranceStartResult,fundingEndStr)>0){
                    insuranceEndTemp=fundingEndStr;
                }else{
                    insuranceEndTemp=DateUtil.addMonth(insuranceStartResult, months-1);
                }
                if(insuranceingEnd>0){
                    serviceMonth=DateUtil.serviceMonthCalc(insuranceingEndStr,insuranceEndTemp,months);
                    months=Integer.valueOf(serviceMonth.get("months"));
                    insuranceEndResult=serviceMonth.get("timeEnd");
                }else{
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }
            }

            if(fundSelect==1){
                if(fundingEnd>0){
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }else{
                    fundStartResult=fundStartStr;
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }
                if(!insuranceEndResult.equals("1970-01-01")&&DateUtil.monthCompare(fundEndResult,insuranceEndResult)<0){
                    fundEndResult=insuranceEndResult;
                }
                if(insuranceSelect==2&&DateUtil.monthCompare(fundEndResult,insuranceingEndStr)<0){
                    if (!insuranceRenewFlag.equals("2")){
                        insuranceRenew=1;
                    }
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                    insuranceEndResult=fundEndResult;
                }
            }
        }
        if(fundFlag==1&&(insuranceFlag==0||insuranceFlag==2)){  //155300
            if(fundingEnd>0){
                fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
            }else{
                fundStartResult=fundStartStr;
            }
            String fundEndTemp="1970-01-01";
            if(fundSelect==1){//勾选强制项
                if(fundingEnd>0){
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                }else{
                    fundStartResult=fundStartStr;//ttttt
                }
                if(insuranceingEnd>0&&DateUtil.monthCompare(fundStartResult,insuranceingEndStr)>0){
                    fundEndTemp=insuranceingEndStr;
                }else{
                    fundEndTemp=DateUtil.addMonth(fundStartResult, months-1);
                }
                if(fundingEnd>0){
                    serviceMonth=DateUtil.serviceMonthCalc(fundingEndStr,fundEndTemp,months);
                    months=Integer.valueOf(serviceMonth.get("months"));
                    fundEndResult=serviceMonth.get("timeEnd");
                }else{
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }
            }
            if(insuranceSelect==1){
                if(insuranceingEnd>0){
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }else{
                    insuranceStartResult=insuranceStartStr;
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }
                if(!fundEndResult.equals("1970-01-01")&&DateUtil.monthCompare(insuranceEndResult,fundEndResult)<0){
                    insuranceEndResult=fundEndResult;
                }
                if(fundSelect==2&&DateUtil.monthCompare(insuranceEndResult,fundingEndStr)<0){
                    if (!fundRenewFlag.equals("2")){
                        fundRenew=1;
                    }
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                    fundEndResult=insuranceEndResult;
                }
            }
        }
        if(insuranceFlag==0&&fundFlag==0){
            if(insuranceSelect==1){
                if(insuranceingEnd>0){
                    insuranceStartResult=DateUtil.addMonth(insuranceingEndStr, 1);
                    insuranceEndResult=DateUtil.addMonth(insuranceStartResult, months-1);
                }else{
                    insuranceStartResult=insuranceStartStr;
                    insuranceEndResult=DateUtil.addMonth(insuranceStartStr, months-1);
                }
            }
            if(fundSelect==1){
                if(fundingEnd>0){
                    fundStartResult=DateUtil.addMonth(fundingEndStr, 1);
                    fundEndResult=DateUtil.addMonth(fundStartResult, months-1);
                }else{
                    fundStartResult=fundStartStr;
                    fundEndResult=DateUtil.addMonth(fundStartStr, months-1);
                }
            }
        }
        String alertStr="";
        if(insuranceSelect==1&&fundSelect==1){
            if(insuranceFlag==1&&fundFlag==1&&(DateUtil.monthCompare(fundStartResult,fundEndResult)!=(months-1)||DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)!=(months-1))){
                if (DateUtil.monthCompare(fundStartResult,fundEndResult)<0){
                    alertStr="当前城市社保公积金强制缴纳，参保截止月份须一致，请重新选择参保套餐或公积金";
                    press=0;
                    if (DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)<0){
                        alertStr+="，和社保";
                    }
                    alertStr+="起缴月份。具体请咨询400-111-8900";
                }
                if (DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)<0){
                    alertStr="当前城市社保公积金强制缴纳，参保截止月份须一致，请重新选择参保套餐或社保";
                    press=0;
                    if (DateUtil.monthCompare(fundStartResult,fundEndResult)<0){
                        alertStr+="，和公积金";
                    }
                    alertStr+="起缴月份。具体请咨询400-111-8900";
                }
            }else if(insuranceFlag==1){
                if(!fundStartResult.equals("1970-01-01")&&DateUtil.monthCompare(fundStartResult,fundEndResult)!=(months-1)){
                    if (DateUtil.monthCompare(fundStartResult,fundEndResult)<0){
                        alertStr="当前城市社保强制缴纳，公积金的参保月份须在社保的参保范围内，请重新选择参保套餐或公积金起缴月份以满足参保条件。具体请咨询400-111-8900";
                        press=0;
                    }
                }
            }else if(fundFlag==1){
                if(!insuranceStartResult.equals("1970-01-01")&&DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)!=(months-1)){
                    if (DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)<0){
                        alertStr="当前城市公积金强制缴纳，社保的参保月份须在公积金的参保范围内，请重新选择参保套餐或社保起缴月份以满足参保条件。具体请咨询400-111-8900";
                        press=0;
                    }
                }
            }
            if(fundStartResult.equals(insuranceStartResult)){
                alertStr="";
            }
        }else if(insuranceSelect==2||fundSelect==2){
            if(fundFlag==1&&fundRenew==1){
                press=0;
                alertStr="当前城市公积金强制缴纳，社保的参保月份须在公积金的参保范围内，因公积金还未满足续费条件，导致无法购买社保业务。具体请咨询400-111-8900";
                if (insuranceFlag==1){
                    alertStr="因当前城市社保公积金强制缴纳，参保截止月份须一致，因公积金还未满足续费条件，导致无法购买公积金业务。具体请咨询400-111-8900";
                }
            }else if(insuranceFlag==1&&insuranceRenew==1){
                press=0;
                alertStr="当前城市社保强制缴纳，公积金的参保月份须在社保的参保范围内，因社保还未满足续费条件，导致无法购买公积金业务。具体请咨询400-111-8900";
                if (fundFlag==1){
                    alertStr="因当前城市社保公积金强制缴纳，参保截止月份须一致，因社保还未满足续费条件，导致无法购买公积金业务。具体请咨询400-111-8900";
                }
            }
            if (fundFlag==1&&insuranceFlag==1){
                if (insuranceSelect==2&&DateUtil.monthCompare(fundStartResult,fundEndResult)<0){
                    alertStr="因当前城市社保公积金强制缴纳，参保截止月份须一致，因社保还未满足续费条件，导致无法购买公积金业务。具体请咨询400-111-8900";
                    press=0;
                }
                if (fundSelect==2&&DateUtil.monthCompare(insuranceStartResult,insuranceEndResult)<0){
                    alertStr="因当前城市社保公积金强制缴纳，参保截止月份须一致，因公积金还未满足续费条件，导致无法购买公积金业务。具体请咨询400-111-8900";
                    press=0;
                }
            }
        }
        if(DateUtil.monthCompare(insuranceStartResult, insuranceEndResult)<0){//比较计算得到的起止月，当起始月大于截止月时，取0值
            result.put("insuranceStart", 0);
            result.put("insuranceEnd", 0);
        }else{
            result.put("insuranceStart", DateUtil.convertToInt0(insuranceStartResult));
            result.put("insuranceEnd", DateUtil.convertToInt0(insuranceEndResult));
        }
        if(DateUtil.monthCompare(fundStartResult, fundEndResult)<0){
            result.put("fundStart", 0);
            result.put("fundEnd", 0);
        }else{
            result.put("fundStart", DateUtil.convertToInt0(fundStartResult));
            result.put("fundEnd", DateUtil.convertToInt0(fundEndResult));
        }
        resultObjStr.put("status", 0);
        HashMap<String, Integer> resultMap=new HashMap<String, Integer>();//公积金续费月份
        resultMap.put("renew_start", result.get("insuranceStart"));
        resultMap.put("renew_end", result.get("insuranceEnd"));
        resultObjStr.put("socialSecurity_renew", resultMap);
        resultMap.put("renew_start", result.get("fundStart"));
        resultMap.put("renew_end", result.get("fundEnd"));
        resultObjStr.put("fund_renew", resultMap);
        resultObjStr.put("socialSecurity_renew_flag", insuranceRenew);
        resultObjStr.put("fund_renew_flag", fundRenew);
        resultObjStr.put("press", press);
        resultObjStr.put("renew_tips", alertStr);//
        return resultObjStr;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        InsurerServiceImpl insurerServiceImpl=new InsurerServiceImpl();
//        System.out.println(DateUtil.convertToDate(1459510690)+"---"+ DateUtil.convertToDate(1462096472));
//        insurerServiceImpl.getMonthByCondition(0, 1459499478, 1464766948, 0, 1, 0, 2, 1, 3);
//        insurerServiceImpl.getMonthByCondition(0, 1459499478, 1464766948, 0, 1, 0, 2, 1, 12);
//        insurerServiceImpl.getMonthByCondition(0, 1459510690, 1462096472, 0, 1, 1, 1, 1, 3);
//        insurerServiceImpl.getMonthByCondition(1464781303, 1459510893, 1462096472, 0, 1, 0, 1, 1, 3);
    }

}
