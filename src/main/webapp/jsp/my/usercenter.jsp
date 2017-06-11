<!doctype html>
<html style="background: #f8f8f8;">
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
    <div class="main" style="padding-top: 0;">
        <div class="user">
            <div class="user-top">
                <span>普通会员</span> 
                <a href="message.jsp">
                    <img src="jsp/resources/img/message.png">
                    <i>2</i>
                </a>
                <a href="edit.jsp">
                    <img src="jsp/resources/img/set.png" >
                </a>
                <div class="clear"></div>
            </div>
            <div class="header-img">
                <img src="jsp/resources/img/1.png">
            </div>
            <div class="user-account">
                <ul>
                    <li>
                        <span>余额：</span>
                        <em>1234</em>
                    </li>
                    <li>
                        <span>佣金：</span>
                        <em>45353</em>
                    </li>
                    <li>
                        <span>总积分/下线积分：</span>
                        <em>789/80</em>
                    </li>
                    <div class="clear"></div>
                </ul>
            </div>
        </div>
        <div class="user-list">
            <ul>
                <li>
                    <a href="javascript:void(0);">
                        <img src="jsp/resources/img/u1.png">
                        <span>我的团队</span> 
                    </a>
                    <div class="user-list-item">
                        <em><i>●</i>一级用户(2人)</em>
                        <em><i>●</i>二级用户(3人)</em>
                    </div> 
                </li>
                 <li>
                    <a href="../order/myorder.jsp">
                        <img src="jsp/resources/img/u2.png">
                        <span>我的预定</span>
                    </a>
                </li>
                 <li>
                    <a href="../order/myscoreorder.jsp">
                        <img src="jsp/resources/img/u3.png">
                        <span>积分订单</span>
                    </a>
                </li>
                 <li>
                    <a href="../commission/commission.jsp">
                        <img src="jsp/resources/img/u4.png">
                        <span>佣金提现</span>
                    </a>
                </li>
            </ul>
            <ul>
                <li>
                    <a href="../commission/commissiondetails.jsp">
                        <img src="jsp/resources/img/u5.png">
                        <span>佣金明细</span>
                    </a>
                </li>
                 <li>
                    <a href="../shop/myshoporder.jsp">
                        <img src="jsp/resources/img/u6.png">
                        <span>我的超市订单</span>
                    </a>
                </li>
                 <li> 
                    <a href="javascript:void(0);">
                        <img src="jsp/resources/img/u7.png">
                        <span>我的抵用券</span> 
                    </a>
                    <div class="user-list-item">
                        <em><i>●</i>抽奖</em>
                        <em><i>●</i>已使用(7张)</em>
                        <em><i>●</i>未使用(7张)</em>
                    </div> 
                </li>
                 <li>
                    <a href="../ktv/code.jsp">
                        <img src="jsp/resources/img/u8.png">
                        <span>我的邀请二维码</span>
                    </a>
                </li>
            </ul>
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
                <a href="../ktv/about.jsp" class="red">
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
<script src='jsp/resources/js/rem.js'></script>
<script src='jsp/resources/js/jquery.min.js'></script>
</html>
<script>
    $('.user-list a').click(function() {
        $li=$(this).parent('li');
        if ($(this).hasClass('d-menu')) {
            $li.find('.user-list-item').slideUp();
            $(this).removeClass('d-menu');
        }else{
            $li.find('.user-list-item').slideDown();
            $(this).addClass('d-menu');
        } 
    })
</script>
