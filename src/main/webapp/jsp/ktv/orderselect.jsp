<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html style="background: #fbd1c1;">
<head>
    <meta charset="utf-8">
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta name="author" content="linx" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple fullscreen -->
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <title>ktv</title>   
    <link rel="stylesheet" href="jsp/resources/css/main.css">
</head>
<body>  
    <!-- <div id="header" style="background: #fbd1c1;">
         <a href="javascript:history.go(-1);">
             <img src="img/b2.png">
         </a>
         <h1 >选择包厢</h1>
     </div>  -->
    <div class="main" style="padding-top: 0;">
        <div class="orderselectimg">
            <img src="jsp/resources/img/5.png" >
            <a class="backlast" href="javascript:history.go(-1)"> </a>
            <h1>盛世欢唱邵武燕林店</h1>
            <div class="orderaddress">
                <h2>福建省南平邵武市燕林广场法院旁</h2>
                <a href="tel:13338888888" class="tel">
                    <img src="jsp/resources/img/tel.png" >
                </a>
            </div>
            <div class="orderaddress">
                <h2>福建省南平邵武市燕林广场法院旁</h2>
                <a href="" class="navigation">
                    <img src="jsp/resources/img/go.png">
                    <span>导航到这里</span>
                </a>
            </div>
        </div>
        <div class="ordertime">
            <h2>选择预定时间</h2>
            <div class="ordertimetab">
                <a href="javascript:void(0);" class="act">
                    <span>今天</span>
                    <em>04-13</em>
                </a>
                <a href="javascript:void(0);">
                    <span>周三</span>
                    <em>04-14</em>
                </a>
                <a href="javascript:void(0);">
                    <span>周四</span>
                    <em>04-15</em>
                </a>
                 <a href="javascript:void(0);">
                    <span>周五</span>
                    <em>04-16</em>
                </a>
                 <a href="javascript:void(0);">
                    <span>周六</span>
                    <em>04-17</em>
                </a>
                <div class="clear"></div>
            </div>
            <div class="orderroom"> 
                <a href="javascript:void(0);" class="act">小包厢(3-5)人</a>
                <a href="javascript:void(0);">中包厢(6-10)人</a>
                <a href="javascript:void(0);">大包厢(10-30)人</a> 
                <div class="clear"></div> 
            </div>
            <div class="allroom">
                <ul> 
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <li>
                        <div class="allroomitem">
                            <span>B26</span>
                            <span>小包</span>
                            <span>8-10人</span> 
                        </div>
                        <a href="roominfo.jsp">
                            <i>查看包厢 </i>
                            <i>环境照片</i>
                            <em></em>
                        </a>
                    </li>
                    <div class="clear"></div>
                </ul>
            </div>
        </div>
        <div class="order-hour">
            <h3>今天(01-18)<i>23:00-次日01:00</i>(1.5小时)</h3>
            <h4>欢唱时间不足7小时，按7小时计算</h4>
            <ul>
                <li>
                    <span>18:00</span>
                </li>
                 <li>
                    <span>18:00</span>
                </li>
                <li>
                    <span>18:00</span>
                </li>
                <li>
                    <span>19:00</span>
                </li> 
                <li>
                    <span>20:00</span>
                </li>
                <li>
                    <span class="starttime">21:00</span>
                </li>
                <li>
                    <span class="endtime">
                    23:00
                    <img src="jsp/resources/img/ciri.png" >
                    </span>
                </li>
                <div class="clear"></div>
            </ul>
        </div>
        <div class="selectall">
            <p>请核对您选择的内容  </p>
            <p>开机时间：1月18日18:30</p>
            <p>包间类型：大包厢B22</p>
            <a href="javascript:void(0);">下一步，选择套餐</a>
        </div>
    </div> 
    <div class="footer">
        <ul>
            <li>
                <a href="" class="footer-now red">
                    <img src="jsp/resources/img/h1.png">
                    <span>在线定包</span>
                </a>
            </li>
            <li>
                <a href="about.jsp" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="integralmall.jsp" class="red">
                    <img src="jsp/resources/img/h3.png">
                    <span>积分商城</span>
                </a>
            </li>
            <li>
                <a href="usercenter.jsp" class="red">
                    <img src="jsp/resources/img/h4.png">
                    <span>我的盛世</span>
                </a>
            </li>
            <div class="clear"></div>
        </ul>
    </div>
    <div id="bg"></div>
    <form class="notel">
        <h4>绑定手机</h4>
        <p>您的账户尚未绑定手机号码，请根据提示绑定手机。</p>
        <h2>手机号码</h2>
        <input type="tel">
        <div class="notel-status">
            <span>取消</span> 
            <button type="submit">确定</button>
            <div class="clear"></div>
        </div>
        
        <p>注：手机号码填写将作为您的联系方式，请认真填写！</p>
    </form>
</body> 
<script src='jsp/resources/js/rem.js'></script>
<script src='jsp/resources/js/jquery.min.js'></script>
     <script>
        //选择日期
        $('.ordertimetab a').click(function(){
            $('.ordertimetab a').removeClass('act');
            $(this).addClass('act');
        })
        //选择包厢类型
         $('.orderroom a').click(function() {
            $('.orderroom a').removeClass('act');
            $(this).addClass('act');
         })

         //选择包厢
         $('.allroom li .allroomitem').click(function(){
             $('.allroom li .allroomitem').show();
             $('.allroom li a').removeClass('action');
             $(this).hide();
             $(this).parents('li').find('a').addClass('action');
         })

         //时间选择
         $('.order-hour li').click(function(){
            $('.order-hour li span').removeClass('starttime');
            $(this).find('span').addClass('starttime');
         })

         //tips 绑定手机
         $('.selectall a').click(function(){ 
            $('form.notel').show();
            $('#bg').show();
            $('html,body').css('overflow','hidden');
         })

         //取消绑定手机
         $('.notel-status span').click(function(){
            $('#bg').hide();
            $('form.notel').hide();
            $('html,body').css('overflow','auto');
         })
     </script>
</html>
