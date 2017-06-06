package com.st.controller.mclient.constant;

import com.st.constant.Constant;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * 
 * 
#======================= 商户开通的商户号
merchant.num=22294531

#======================= 商户DES密钥
merchant.desKey=ta4E/aspLA3lgFGKmNDNRYU92RkZ4w2t

#=======================商户MD5密钥
merchant.md5Key=test

#=======================商户支付请求/交易查询/退款 RSA加密私钥(由商户生成,并将对应的公钥发给网银)
merchant.pay.rsaPrivateKey=MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALXf6twUqul1TATO+5nA66p2wjnRd+g96IXpfV6Sf8WXxwizGj+L19LQYRBXpZHmRh82prJ48d0FcHboCiN8pKutnuZrrKYhvORysOc5bVli0hcCn1TfYDoUWJ1UhjUQloqZKWjUz6LV9QY6bIZ1W4+Hmw6HK1bfFwUq0WzIGkJNAgMBAAECgYBlIFQeev9tP+M86TnMjBB9f/sO2wGpCIM5slIbO6n/3By3IZ7+pmsitOrDg3h0X22t/V1C7yzMkDGwa+T3Rl7ogwc4UNVj0ZQorOTx3OEPx3nP1yT3zmJ9djKaHKAmee4XmhQHdqqIuMT2XQaqatBzcsnP+Jnw/WVOsIJIqMeFAQJBAP9yq4hE+UfM/YSXZ5JR33k9RolUUq8S/elmeJIDo/3N2qDmzLjOr9iEZHxioc8JOxubtZ0BxA+NdfKz4v0BSpkCQQC2RIrAPRj9vOk6GfT9W1hbJ4GdnzTb+4vp3RDQQ3x9JGXzWFlg8xJT1rNgM8R95Gkxn3KGnYHJQTLlCsIy2FnVAkAWXolM3pVhxz6wHL4SHx9Ns6L4payz7hrUFIgcaTs0H5G0o2FsEZVuhXFzPwPiaHGHomQOAriTkBSzEzOeaj2JAkEAtYUFefZfETQ2QbrgFgIGuKFboJKRnhOif8G9oOvU6vx43CS8vqTVN9G2yrRDl+0GJnlZIV9zhe78tMZGKUT2EQJAHQawBKGlXlMe49Fo24yOy5DvKeohobjYqzJAtbqaAH7iIQTpOZx91zUcL/yG4dWS6r+wGO7Z1RKpupOJLKG3lA==

#=======================商户交易查询/退款 RSA验签公钥 (由网银生成,并将公钥提供给商户)
merchant.common.rsaPublicKey=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDH0fNZ4M4i/mqSYF2CmsA8Sc2whUCqTnvI4rOkOiEBZVlK97LItIsrHvqJaf0fSq9Cfi0c+gNbqsZbdwTaxUS1oebe6303wG4xVr4bLq6y+hKLKMwBS5RBTW8o8uUEKW19O7uuoBuAeF+SaDJrdsnDHXbhA8y2vaVV6Iq4aWT3QwIDAQAB

#=======================网银支付服务地址
wangyin.wepay.server.pay.url=https://m.jdpay.com/wepay/web/pay

#=======================网银查询服务地址
wangyin.wepay.server.query.url=https://m.jdpay.com/wepay/query

#=======================网银退款服务地址
wangyin.wepay.server.refund.url=https://m.jdpay.com/wepay/refund

#=======================支付成功 商户展示地址
successCallbackUrl=http://www.jd.com

#=======================支付失败 商户展示地址
failCallbackUrl=http://www.baidu.com

#=======================接收异步通知地址
notifyUrl=http://www.jd.com
 * 
 * 
 * 
 * 
 * 
 * 
 */
/**
 * 商户常量类
 * 常量值在spring配置文件中从资源文件获取
 * Created by wywangzhenlong on 14-8-4.
 */
@Component
public class MerchantConstant {
    /**
     * 商户号
     */
    private String merchantNum="110108656001";

    /**
     * 商户md5密钥
     */
//    private String merchantMD5Key="quVlUJzqkohDgmsQEmBCvKcsV9rSaHdH";

    /**
     * 商户des密钥
     */
//    private String merchantDESKey="MmEHQ4Vig8IQTPQBwtNSEN/NugFdJa1t";

    /**
     * 商户支付请求时的 rsa加密私钥
     */
    private String payRSAPrivateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKs1lWZW1m2t3BYp"
    		+"UzVEtnpirjoVDB+la+DQep8vhMLDlSkEL44qxKgRPW4YamfZnkTOA3FyZ8fGO1Fs"
+"vO9c1AqohLfHTB+9ZoiqAX3XeILeP5kRiXnbr0n294NrKizYUOFGOphJZxZYirtY"
+"1aTpbbKVwg0ZVveoxtVVuAaRtxcfAgMBAAECgYAmM4ONXSzlNhjYfQoK5bUNo70a"
+"Sp305bUnaX0izbiCuJ8skrmFsDdHBOm/CgWb/GubEJimFI/hfHK53Vhe1EifZrk9"
+"N3UMV049ipxiXf997ttDnm19zUaxl1rMTRcwXgyGQgk8QlaG+ngJ0woOO8jwjMJC"
+"ohfwuczCYApjPnQHKQJBANeJQoKzLYqs3Ugo0vLGqFUtfIFNsZCNoCm+4f2JYUdK"
+"rwNKTP0NwPacFsX24LgPDNt5sKCsW2iOYp3Ikvllr30CQQDLWfXKKy7Mzww7UMOA"
+"Qu6/4XPE/ycdGQjqHi8clMcQKNYvIUXKhfPV4OmZ5HgBsi7uxV6VXgoTIeEDlfhQ"
+"2dvLAkB4+xCcMFrVxiYHBPMzVpLSDNAf/C45B2XpSL2tBFnYHHGUFTaVrAyt3/tg"
+"byy/46LB5tJfvZ8pVxK/of/tgUg5AkEAo88HAWHB5HhReSK5KgRBAU8jDCkiH/1/"
+"weCowNRUev76cqLR7q/zWXJBm5eA2JTp26wgYiCL6xeahGH102N7+wJAMG10Cd4h"
+"yDBMUIjLo+8ImqcXl/wSRiWdxxXTFXfeJj8L9YKF8j8j4w1Jz9CfNtM04LQ6gHEm"
+"rkdon3K16cR/ig==";

    /**
     * 交易查询/退款 rsa验签公钥
     */
    private String commonRSAPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKE5N2xm3NIrXON8Zj19GNtLZ8xwEQ6uDIyrS3S03UhgBJMkGl4msfq4Xuxv6XUAN7oU1XhV3/xtabr9rXto4Ke3d6WwNbxwXnK5LSgsQc1BhT5NcXHXpGBdt7P8NMez5qGieOKqHGvT0qvjyYnYA29a8Z4wzNR7vAVHp36uD5RwIDAQAB";


    /**
     * 支付成功 商户展示地址
     */
//   private String successCallbackUrl="http://c.joyoll.com/jsp/my/myorderall.jsp";
//   private String successCallbackUrl=Constant.url+"/jsp/my/myorderall.jsp";
    private String successCallbackUrl=Constant.URL+"/personorder/saveToken.do";

    /**
     * 支付失败 商户展示地址
     */
    private String failCallbackUrl=Constant.URL+"/jsp/my/orderlistunpay.jsp";

    /**
     *接收异步通知地址
     */
    private String notifyUrl=Constant.URL+"/mclient/pay/notify.do";

    /**
     * 网银支付服务地址
     */
    private String wangyinServerPayUrl="https://m.jdpay.com/wepay/web/pay";

    /**
     * 网银查询服务地址
     */
    private String wangyinServerQueryUrl="https://m.jdpay.com/wepay/query";

    /**
     * 网银退款服务地址
     */
    private String wangyinServerRefundUrl="https://m.jdpay.com/wepay/refund";


    public String getMerchantNum() {
        return merchantNum;
    }

    public void setMerchantNum(String merchantNum) {
        this.merchantNum = merchantNum;
    }

    public String getPayRSAPrivateKey() {
        return payRSAPrivateKey;
    }

    public void setPayRSAPrivateKey(String payRSAPrivateKey) {
        this.payRSAPrivateKey = payRSAPrivateKey;
    }

    public String getCommonRSAPublicKey() {
        return commonRSAPublicKey;
    }

    public void setCommonRSAPublicKey(String commonRSAPublicKey) {
        this.commonRSAPublicKey = commonRSAPublicKey;
    }

    public String getSuccessCallbackUrl() {
        return successCallbackUrl;
    }

    public void setSuccessCallbackUrl(String successCallbackUrl) {
        this.successCallbackUrl = successCallbackUrl;
    }

    public String getFailCallbackUrl() {
        return failCallbackUrl;
    }

    public void setFailCallbackUrl(String failCallbackUrl) {
        this.failCallbackUrl = failCallbackUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getWangyinServerPayUrl() {
        return wangyinServerPayUrl;
    }

    public void setWangyinServerPayUrl(String wangyinServerPayUrl) {
        this.wangyinServerPayUrl = wangyinServerPayUrl;
    }

    public String getWangyinServerQueryUrl() {
        return wangyinServerQueryUrl;
    }

    public void setWangyinServerQueryUrl(String wangyinServerQueryUrl) {
        this.wangyinServerQueryUrl = wangyinServerQueryUrl;
    }

    public String getWangyinServerRefundUrl() {
        return wangyinServerRefundUrl;
    }

    public void setWangyinServerRefundUrl(String wangyinServerRefundUrl) {
        this.wangyinServerRefundUrl = wangyinServerRefundUrl;
    }
}

