<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MM-dd");
    java.util.Date currentTime = new java.util.Date();//得到当前系统时间
    String nowTime = formatter.format(currentTime); //将日期时间格式化
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
    <jsp:include page="/jsp/layouts/head.jsp" flush="true"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <title>ktv</title>   
    <link rel="stylesheet" href="<%=basePath%>jsp/resources/css/main.css">
</head>
<body>  
    <div class="main" style="padding-top: 0;">
        <div class="orderselectimg">
            <input type="hidden" class="shopid" value="${storeDetail.id}" />
            <img src="<%=basePath%>jsp/resources/img/5.png" >
            <a class="backlast" href="javascript:history.go(-1)"> </a>
            <h1>${storeDetail.name}</h1>
            <div class="orderaddress">
                <h2>${storeDetail.address}</h2>
                <a href="tel:${storeDetail.phone}" class="tel">
                     <img src="<%=basePath%>jsp/resources/img/tel.png" >
                </a>
            </div>
            <div class="orderaddress">
                <h2>${storeDetail.address}</h2>
                <a href="" class="navigation">
                    <img src="<%=basePath%>jsp/resources/img/go.png">
                    <span>导航到这里</span>
                </a>
            </div>
        </div>
        <div class="ordertime">
            <h2>选择预定时间</h2>
            <div class="ordertimetab">
                <a href="javascript:void(0);" class="act">
                    <span>今天</span>
                    <em><%=nowTime%></em>
                </a>
                <div class="clear"></div>
            </div>
            <div class="orderroom">
                <c:forEach var = "roomType" items="${roomTypes}">
                    <a href="javascript:void(0);">
                    <input type="hidden" class="room_type_id" value="${roomType.room_type_id}" />
                    ${roomType.room_type_name}(${roomType.room_peoples})人</a>
                </c:forEach>
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
                        <a href="${pageContext.request.contextPath}/order/gotoRoomInfo.do?iid=${a.iid}">
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

        </div>
        <div class="selectall">
            <a href="javascript:void(0);">下一步，选择套餐</a>
        </div>
    </div>
    <jsp:include page="/jsp/layouts/foot.jsp" flush="true"/>
    <div id="bg"></div>
    <form class="notel">
        <h4>绑定手机</h4>
        <p>您的账户尚未绑定手机号码，请根据提示绑定手机。</p>
        <h2>手机号码</h2>
        <input class="notel-tel" type="tel">
        <div class="notel-status">
            <span>取消</span> 
            <button type="button" onclick="return checkTel()">确定</button>
            <div class="clear"></div>
        </div>
        <p>注：手机号码填写将作为您的联系方式，请认真填写！</p>
    </form>
</body>
<script src='<%=basePath%>jsp/resources/js/rem.js'></script>
<script src='<%=basePath%>jsp/resources/js/jquery.min.js'></script>
     <script>
        var shopId = $('.shopid').val();
        var roomTypeId = $('.orderroom a:eq(0)').find('.room_type_id').val();
        //初始化页面是选中第一个包厢类型
        $(document).ready(function(){
          $('.orderroom a:eq(0)').addClass('act');
          //调用第一个包厢类型去获取剩余包厢集合
          getRoomList(shopId, roomTypeId);
        });

        //选择日期
        $('.ordertimetab a').click(function(){
            $('.ordertimetab a').removeClass('act');
            $(this).addClass('act');
        })

        //选择包厢类型
         $('.orderroom a').click(function() {
            $('.orderroom a').removeClass('act');
            $(this).addClass('act');
            //$(this).index();
            //调用去获取
            roomTypeId = $(this).find('.room_type_id').val();
            getRoomList(shopId, roomTypeId);
         })

        //获取包厢列表
        var iid;
        function getRoomList(shopId, roomTypeId){
            $.ajax({
                'url': "${pageContext.request.contextPath}/shop/getRoomList.do",
                'type': 'post',
                'dataType': 'json',
                'data': {
                    shopId: shopId,
                    roomTypeId: roomTypeId
                },
                success: function success(d) {
                    if (d.error_code == 0) {
                         var str = '';
                         for(var i=0; i< d.result.length; i++){
                             str = str + '<li><div class="allroomitem"><input type="hidden" class="iid" value="'
                             + d.result[i].iid + '"/><span class="name">'
                             + d.result[i].room_name + '</span><span>' + d.result[i].room_type_name
                             + '</span><span>' + d.result[i].peoples + '人</span></div>'
                             + '<a href="' + packageJson.JAVA_DOMAIN  + '/order/gotoRoomInfo.do?iid=' + d.result[i].iid + '">'
                             + '<i>查看包厢 </i><i>环境照片</i><em></em></a></li>';
                         }
                        str += '<div class="clear"></div>';
                        $(".allroom ul").html(str);

                        //选择包厢
                        $('.allroom li .allroomitem').click(function(){
                            $('.allroom li .allroomitem').show();
                            $('.allroom li a').removeClass('action');
                            $(this).hide();
                            $(this).parents('li').find('a').addClass('action');

                            var myDate = new Date();
                            var month = myDate.getMonth() + 1;
                            var time_info = '';
                            time_info += '<h3>今天(' + month + '-' + myDate.getDate() + ')<i>19:00-次日02:00</i>(7小时)</h3>';
                            time_info += '<h4>欢唱时间不足7小时，按7小时计算</h4>';
                            $(".order-hour").html(time_info);

                            iid = $(this).find('.iid').val();
                            var book_info = '';
                            book_info += '<p>请核对您选择的内容  </p>';
                            book_info += '<p>开机时间：' + month + "月" + myDate.getDate() + '日</p>';
                            book_info += '<p>包间类型：' + $(this).find('.name').text() + '</p>';
                            $(".selectall").prepend(book_info);
                        })
                    } else {
                         alert(d.msg);
                    }
                 }
             });
        }

         //时间选择
         $('.order-hour li').click(function(){
            $('.order-hour li span').removeClass('starttime');
            $(this).find('span').addClass('starttime');
         })

         //tips 绑定手机
         $('.selectall a').click(function(){
             if(null == iid || "" == iid){
                 return false;
             }
            checkIsBindPhone();
         })

         //校验是否绑定手机
         function checkIsBindPhone(){
            $.ajax({
                'url': "${pageContext.request.contextPath}/member/checkAndUpdateMobile.do",
                'type': 'post',
                'dataType': 'json',
                'data': {
                    type: 1
                },
                success: function success(d) {
                    if (d.status == 0) {
                        //进入下一步
                        window.location.href = packageJson.JAVA_DOMAIN  + '/order/gotoPackages.do?iid=' + iid;
                    } else {
                        //弹出绑定页面
                        $('form.notel').show();
                        $('#bg').show();
                        $('html,body').css('overflow','hidden');
                    }
                 }
            });
         }

        //取消绑定手机
        $('.notel-status span').click(function(){
            $('#bg').hide();
            $('form.notel').hide();
            $('html,body').css('overflow','auto');
        })

        //校验是否绑定手机
        function checkTel(){
            $("#f2").attr("target","rfFrame");
            var tel = $('.notel .notel-tel').val();
            if (tel == undefined || tel == null || "" == tel){
                alert("输入项不能为空");
                //弹出绑定页面
                return false;
            }else{
                if(!isNaN(tel)){
                    updatePhone(tel);
                }else{
                    alert("请输入数字");
                    //弹出绑定页面
                    return false;
                }
            }
        }

        //去更新手机号码，成功就跳转
        function updatePhone(tel){
            $.ajax({
                'url': "${pageContext.request.contextPath}/member/checkAndUpdateMobile.do",
                'type': 'post',
                'dataType': 'json',
                'data': {
                    mobile: tel ,
                    type: 2
                },
                success: function success(d) {
                    if (d.status == 0) {
                        //进入下一步
                        window.location.href = packageJson.JAVA_DOMAIN  + '/order/gotoPackages.do?iid=' + iid;
                    } else {
                        //弹出绑定页面
                        alert(d.msg);
                        return;
                    }
                }
            });
        }
     </script>
</html>
