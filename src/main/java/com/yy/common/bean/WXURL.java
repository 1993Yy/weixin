package com.yy.common.bean;

/**
 * @Package: com.yy.common.bean
 * @ClassName: WXURL
 * @Author: Created By Yy
 * @Date: 2019-05-29 17:59
 */
public class WXURL {
    /**
     * 获取token
     */
    public static final String token_get="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * 获取二维码
     */
    public static String qr_post="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    /**
     * 获取用户信息
     */
    public static String user_get="https://api.weixin.qq.com/cgi-bin/user/info?access_token=TOKEN&openid=OPENID&lang=zh_CN";
    /**
     * 聊天
     */
    public static String chat_post="https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
}
