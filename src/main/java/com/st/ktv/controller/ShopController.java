package com.st.ktv.controller;

import com.st.utils.JoYoUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @FileName ShopController
 * @Author dingzr
 * @CreateTime 2017/6/14 21:36 六月
 */

@Controller
@RequestMapping("/shop")
public class ShopController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 进入超市页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getShopping")
    public String getShopping(HttpServletRequest request, HttpServletResponse response) {

        try {
            //获取非精选分类
            JSONObject cate = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHOP_GOODS_CATE, "", new String[]{}));
            if (0 == cate.getInt("error_code")) {
                request.setAttribute("cates", cate.get("result"));
            }
            //获取精选商品，就传1，然后cate_id不用传
            String[] arr = new String[]{"is_select" + 1};
            String mystr = "is_select=" + 1;
            JSONObject goods = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHOP_GOODS, mystr, arr));
            if (0 == goods.getInt("error_code")) {
                request.setAttribute("goods", goods.get("result"));
            }
        } catch (Exception e) {
            logger.error("获取超市商品出错:" + e.getMessage(), e);
        }
        return "shop/shopnowing";
    }

    /**
     * 获取分类商品
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getCateGoods")
    public @ResponseBody Object getCateGoods(HttpServletRequest request, HttpServletResponse response) throws IOException{

        JSONObject goods = JSONObject.fromObject("{\"status\":1,\"msg\":\"出错了\"}");
        String cateId = request.getParameter("id");
        //获取精选商品，就传1，然后cate_id不用传
        String[] arrs;
        StringBuffer mystrs = new StringBuffer();
        List<String> list = new ArrayList<String>();
        if(null != cateId){
            if(cateId.equals("0")){
                mystrs.append("is_select="+1);
                list.add("is_select"+1);
            }else{
                mystrs.append("is_select="+0);
                list.add("is_select"+0);
                mystrs.append("&cate_id="+cateId);
                list.add("cate_id"+cateId);
            }
        }else{
            goods = JSONObject.fromObject("{\"status\":1,\"msg\":\"分类不能为空\"}");
        }
        try {
            arrs = list.toArray(new String[list.size()]);
            goods = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.SHOP_GOODS, mystrs.toString(), arrs));
            if (0 == goods.getInt("error_code")) {
                request.setAttribute("goods", goods.get("result"));
            }
        } catch (Exception e) {
            logger.error("获取超市商品出错:" + e.getMessage(), e);
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(goods);
        return null;
    }

    /**
     * 进入超市页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/gotoStoreDetail")
    public String gotoStoreDetail(HttpServletRequest request, HttpServletResponse response) {
        String shopId = request.getParameter("id");
        try {
            String[] arr = new String[]{"shop_id" + shopId};
            String mystr = "shop_id=" + shopId;
            JSONObject goods = JSONObject.fromObject(JoYoUtil.getInterface(JoYoUtil.STORE_DETAIL, mystr, arr));
            if (0 == goods.getInt("error_code")) {
                request.setAttribute("storeDetail", goods.get("result"));
            }
        } catch (Exception e) {
            logger.error("获取门店详情出错:" + e.getMessage(), e);
        }
        return "order/orderselect";
    }

}
