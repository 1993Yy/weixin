package com.yy.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONObject;
import com.yy.common.bean.WXURL;
import com.yy.common.enums.ReturnInfo;
import com.yy.common.exception.ResultException;
import com.yy.common.redis.RedisDao;
import com.yy.common.util.ImgUtil;
import com.yy.common.util.WXUtil;
import com.yy.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * @Package: com.yy.service
 * @ClassName: WeiXinService
 * @Author: Created By Yy
 * @Date: 2019-05-31 10:07
 */
@Service
@Slf4j
public class WeiXinService {
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    /**
     * 微信回调
     * @param request
     * @param response
     */
    public void authorize(HttpServletRequest request, HttpServletResponse response){
        WXUtil wxUtil = getWXUtil();
        wxUtil.authorize(request,response);
    }
    /**
     * 获取二维码
     *
     * @param expireTime
     * @param senceID
     * @return
     */
    public Map<String, Object> getQr(Integer expireTime, String senceID, HttpServletResponse response) {
        WXUtil wxUtil = getWXUtil();
        Map<String, Object> qr = wxUtil.getQr(expireTime, senceID);
        String url = (String) qr.get("url");
        try {
            String headImg = userService.getLogo();
            QrConfig config = new QrConfig();
            config.setMargin(0);
            config.setForeColor(new Color(11,217,182).getRGB());
            BufferedImage generate = QrCodeUtil.generate(url, config);
            if(StrUtil.isNotBlank(headImg)){
                BufferedImage read = ImageIO.read(new URL(headImg));
                int raduis = read.getWidth() / 4;
                read = ImgUtil.drawRadius(read, raduis);
                read = ImgUtil.drawBoder(read, 10, raduis, Color.white);
                BufferedImage image = ImgUtil.drawLogo(generate, read);
                ImageIO.write(image, "png", response.getOutputStream());

            }else {
                ImageIO.write(generate, "png", response.getOutputStream());
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return qr;
    }
    /**
     *
     * ===============================================================================================================
     *
     *
     *
     *
     * ===============================================================================================================
     */
    private WXUtil getWXUtil(){
        Account account = accountService.getAccount();
        Assert.notNull(account,"无可用公众号");
        //查看Redistoken是否过期
        String accessToken = redisDao.getAccessToken();
        if (StrUtil.isEmpty(accessToken)){
            String url = WXURL.token_get.replace("APPID", account.getAppID())
                    .replace("APPSECRET", account.getAppSecret());
            String data = restTemplate.getForObject(url, String.class);
            log.info(data);
            if (data.contains("errcode")){
                throw new ResultException(ReturnInfo.businessError.getCode(),"获取token错");
            }
            Map map = (Map) JSONObject.parse(data);
            String token = (String)map.get("access_token");
            Integer expire=(Integer)map.get("expires_in");
            account.setAccessToken(token).setExpireTime(DateUtil.offsetSecond(new Date(),expire));
            //更新数据库
            accountService.updateAccount(account);
            //更新Redis
            redisDao.addAccessToken(token,expire);
        }

        WXUtil wxUtil=new WXUtil(account,restTemplate,redisDao,userService);
        return wxUtil;
    }
}
