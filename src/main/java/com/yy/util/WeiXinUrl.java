package com.yy.util;

/**
 * @Package: com.yy.util
 * @ClassName: WeiXinUrl
 * @Author: Created By Yy
 * @Date: 2019-05-09 13:56
 */
public class WeiXinUrl {

    /**
     * 获取token
     */
    public static String tokenUrl_get="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * 获取二维码
     */
    public static String qrUrl_post="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    /**
     * 获取用户信息
     */
    public static String userUrl_get="https://api.weixin.qq.com/cgi-bin/user/info?access_token=TOKEN&openid=OPENID&lang=zh_CN";
}
