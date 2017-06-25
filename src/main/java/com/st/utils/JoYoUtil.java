package com.st.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 金柚个人社保通接口URL
 * <p/>
 * 创建人：wuhao 创建时间：2015-6-11
 */
public class JoYoUtil {

    private static Logger logger = LoggerFactory.getLogger(JoYoUtil.class);


    /**
     * 公司信息
     */
    public static final String COMPANY_INFO = Constant.PHP_API_URL + "/api/get_company_info";

    /**
     * 获取网站参数
     */
    public static final String WEB_PARAMS = Constant.PHP_API_URL + "/api/get_web_params";

    /**
     * 获取轮播图
     */
    public static final String INDEX_BANNERS = Constant.PHP_API_URL + "/api/get_banners";

    /**
     *  获取盛世招聘
     */
    public static final String COMPANY_RECRUITS = Constant.PHP_API_URL + "/api/get_recruits";
    /**
     * 获取优惠活动列表
     */
    public static final String DISCOUNT_LIST = Constant.PHP_API_URL + "/api/get_discounts";

    /**
     * 获取优惠活动详情
     */
    public static final String DISCOUNT_DETAIL = Constant.PHP_API_URL + "/api/get_discount_detail";

    /**
     * 获取超市商品分类
     */
    public static final String SHOP_GOODS_CATE = Constant.PHP_API_URL + "/api/get_goods_cate";

    /**
     * 获取某类下超市商品
     */
    public static final String SHOP_GOODS = Constant.PHP_API_URL + "/api/get_goods";

    /**
     * 获取用户站内信
     */
    public static final String USER_MESSAGES = Constant.PHP_API_URL + "/api/get_messages";

    /**
     * 所有门店
     */
    public static final String STORES = Constant.PHP_API_URL + "/api/get_shops";

    /**
     * 门店详情
     */
    public static final String STORE_DETAIL = Constant.PHP_API_URL + "/api/get_shop_detail";

    /**
     * 获取包厢类型
     */
    public static final String ROOM_TYPE = Constant.PHP_API_URL + "/api/get_room_type";


    /**
     *  获取包厢列表
     */
    public static final String ROOM_LIST = Constant.PHP_API_URL + "/api/get_room_list";


    /**
     *  获取包厢详情
     */
    public static final String ROOM_DETAIL = Constant.PHP_API_URL + "/api/get_room_detail";

    /**
     *  获取套餐
     */
    public static final String GET_PACKAGES = Constant.PHP_API_URL + "/api/get_packages";


    /**
     *  创建预订订单
     */
    public static final String CREATRE_ORDER = Constant.PHP_API_URL + "/api/create_book_order";

    /**
     *  取消预订订单
     */
    public static final String CANCLE_ORDER = Constant.PHP_API_URL + "/api/cancel_book_order";

    /**
     *  预订订单列表
     */
    public static final String ORDER_LIST = Constant.PHP_API_URL + "/api/get_my_book_orders";

    /**
     *  预订订单详情
     */
    public static final String ORDER_DETAIL = Constant.PHP_API_URL + "/api/get_order_detail";

    /**
     *  申请退款
     */
    public static final String ORDER_REFUND = Constant.PHP_API_URL + "/api/refund_book_order";

    /**
     *  确认是否开机
     */
    public static final String CONFIRM_BOOT = Constant.PHP_API_URL + "/api/confirm_boot";

    /**
     *  订单支付后的回掉函数
     */
    public static final String PAY_ORDER = Constant.PHP_API_URL + "/api/pay_book_order";

    /**
     *  获取购物车商品数量
     */
    public static final String SHOP_CART_COUNT = Constant.PHP_API_URL + "/api/shopping_cart_count";

    /**
     *  购物车添加
     */
    public static final String ADD_SHOP_CART = Constant.PHP_API_URL + "/api/add_shopping_cart";

    /**
     *  获取购物车
     */
    public static final String GET_SHOP_CART = Constant.PHP_API_URL + "/api/get_shopping_carts";

    /**
     *  删除购物车商品
     */
    public static final String DEL_SHOP_CART = Constant.PHP_API_URL + "/api/del_shopping_carts";

    /**
     *  超市结算
     */
    public static final String SHOP_SETTLEMENT = Constant.PHP_API_URL + "/api/settlement";

    /**
     *  超市订单支付回调
     */
    public static final String PAY_GOODS_ORDER = Constant.PHP_API_URL + "/api/pay_goods_order";

    /**
     *  获取商品订单列表
     */
    public static final String GET_GOODS_ORDERS = Constant.PHP_API_URL + "/api/get_my_goods_orders";

    /**
     *  取消商品订单
     */
    public static final String CANCEL_GOODS_ORDER = Constant.PHP_API_URL + "/api/cancel_goods_order";

    /**
     *  商品订单退款
     */
    public static final String REFUND_GOODS_ORDER = Constant.PHP_API_URL + "/api/refund_goods_order";

    /**
     *  删除商品订单
     */
    public static final String DEL_GOODS_ORDER = Constant.PHP_API_URL + "/api/del_goods_order";

    /**
     *  确认商品订单
     */
    public static final String CONFIRM_GOODS_ORDER = Constant.PHP_API_URL + "/api/confirm_goods_order";






     /**
     * 查询当前的活动URL
     */
    public static final String PERSONRED_DEMANDACTIVITY_URL = Constant.PHP_API_URL + "/openapi/personsocial/personred/demandactivity/client_id/social10001";
     /**
     * 站内信查询URL
     */
    public static final String STATIONLETTER_DEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/stationletter/demand/client_id/social10001";
    /**
     * 站内信已读消息接口URL
     */
    public static final String STATIONLETTER_READ_URL = Constant.PHP_API_URL + "/openapi/personsocial/stationletter/read/client_id/social10001";
    /**
     * 文章类别查询URL
     */
    public static final String ARTICLE_TYPEDEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/typedemand/client_id/social10001";
    /**
     * 文章类别查询URL 平级 3.51
     */
    public static final String ARTICLE_TYPEALL_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/getalltypedemand/client_id/social10001";
    /**
     * 首页banner查询 3.4
     */
    public static final String ARTICLE_BANNERDEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/advdemand/client_id/social10001";
    /**
     * 伪静态查询接口
     */
    public static final String ARTICLE_STATIC_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/alltypedemand/client_id/social10001";
    /**
     * 文章查询URL
     */
    public static final String ARTICLE_DEMAND_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/demand/client_id/social10001";
    /**
     * 文章大类搜索URL
     */
    public static final String ARTICLE_SEARCH_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/searcharticle/client_id/social10001";
    /**
     * 文章详情URL
     */
    public static final String ARTICLE_DETAIL_URL = Constant.PHP_API_URL + "/openapi/personsocial/article/detail/client_id/social10001";

   /**
     *
     * @param urlPath 接口地址
     * @param mystr   待提交的数据，包括数据以及密文
     * @return 返回json字符串 error为0正常
     * @throws Exception
     */
    public static String getInterface(String urlPath, String mystr, String[] arr) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(1000 * 50);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.getOutputStream().write(mystr.toString().getBytes("UTF-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            logger.info("提交的数据为：" + urlPath + mystr);

            InputStream inputStream = connection.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str = null;

            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            connection.disconnect();
            logger.info("返回的数据为：" + sb);
        } catch (Exception e) {
            logger.error("获取数据出错,提交的数据为：" + urlPath + mystr, e);
        }
        return sb.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        logger.info("提交的数据为：" + url + "?" + param);
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("返回的数据为：" + result);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！" + e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            logger.info("提交的数据为：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("返回的数据为：" + result);
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！" + e.getMessage(), e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
        return result;
    }

}
