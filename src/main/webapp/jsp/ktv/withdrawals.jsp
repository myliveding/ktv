<%@ page language="java" import="com.st.utils.Constant" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html >
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
    <div id="header">
         <a href="javascript:history.go(-1);">
             <img src="jsp/resources/img/b3.png">
         </a>
         <h1 class="color-red">优惠活动/一网打尽</h1>
     </div> 
    <div class="main withdrawals-html" >
         <div class="withdrawals">
            <h2>提现</h2>
            <h1>可提<i>5600</i>元</h1>
         </div>
         <form action="" class="withdrawals-info">
              <div class="withdrawals-group">
                  <label for="money">提现金额:</label>
                  <i class="less">-</i>
                  <input type="text" id="money" value="800" >
                  <i class="add">+</i>
              </div>
              <div class="withdrawals-group">
                  <label for="tel">手机号码:</label> 
                  <input type="tel" id="tel"> 
              </div>
              <div class="withdrawals-group">
                  <label for="number">验证码:</label> 
                  <input type="tel" id="number" > 
              </div>
              <p>温馨提醒：提现金额必须大于（包含）1元，否则将提现失败。</p>
              <button class="withdrawals-submit" type="submit">提&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;交</button>
         </form>
    </div>
   
    <div class="footer">
        <ul>
            <li>
                <a href="jsp/index.jsp" class="footer-now red">
                    <img src="jsp/resources/img/h1.png">
                    <span>在线定包</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
                    <img src="jsp/resources/img/h2.png">
                    <span>关于盛世</span>
                </a>
            </li>
            <li>
                <a href="" class="red">
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
</body> 
<script src='jsp/resources/js/jquery.min.js'></script>
<script src='jsp/resources/js/rem.js'></script>
<script>
    
    $('.withdrawals-group i').click(function() {
      var money=parseFloat($('#money').val());
      if ($(this).hasClass('add')) {
         $('#money').val(money+1);
      }else{
        if (money<1) {
          $('#money').val('');
           alert('提现金额不能小于1元');
        }else{
          $('#money').val(money-1);
        }
      }
    })

    $('#money').keyup(function() {
      var money=parseFloat($('#money').val());
      var re=/^\d*\.{0,1}\d{0,1}$/;
      if (re.test($("#money").val())) {
         if (money<1) {
          $("#money").val(''); 
           alert('提现金额不能小于1元');
         }
      }else{
        $("#money").val(''); 
        alert('您输入的格式不正确！');
      }
    })
</script>
</html>
