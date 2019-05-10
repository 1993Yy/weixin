package com.yy.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.yy.common.util.ImgUtil;
import com.yy.entity.Account;
import com.yy.common.util.WeiXinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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
import java.util.Objects;

/**
 * @Package: com.yy.service
 * @ClassName: WeiXinService
 * @Author: Created By Yy
 * @Date: 2019-05-09 13:59
 */
@Service
@Slf4j
public class WeiXinService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        WeiXinUtil weiXinUtil = getWeiXinUtil();
        weiXinUtil.authorize(request, response);

    }

    /**
     * 获取二维码
     *
     * @param expireTime
     * @param senceID
     * @return
     */
    public Map<String, String> getQR(Integer expireTime, String senceID, HttpServletResponse response) {
        WeiXinUtil weiXinUtil = getWeiXinUtil();
        Map<String, String> qr = weiXinUtil.getQr(expireTime, senceID);
        String url = qr.get("url");
        try {
            String headImg = userService.getHeadImg();
            BufferedImage read = ImageIO.read(new URL(headImg));
            int raduis = read.getWidth() / 4;
            read = ImgUtil.drawRadius(read, raduis);
            read = ImgUtil.drawBoder(read, 10, raduis, Color.white);
            QrConfig config = new QrConfig();
            config.setMargin(0);
            BufferedImage generate = QrCodeUtil.generate(url, config);
            BufferedImage image = ImgUtil.drawLogo(generate, read);
            ImageIO.write(image, "png", response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return qr;
    }

    /**
     * 定时更新token
     */
    public void scheduleUpdateToken() {
        Account account = accountService.getAccount();
        WeiXinUtil weiXinUtil = getWeiXinUtil();
        if (Objects.isNull(account.getExpireTime())
                || account.getExpireTime().compareTo(DateUtil.offsetMinute(new Date(), -5)) == -1) {
            String accesstoken = weiXinUtil.getAccesstoken();
            account.setAccessToken(accesstoken);
            account.setExpireTime(DateUtil.offsetHour(new Date(), 2));
            accountService.updateAccount(account);

        }
    }

    /**
     * 获取素材
     *
     * @return
     */
    public Map<String, Object> getMaterialBatch() {
        WeiXinUtil weiXinUtil = getWeiXinUtil();
        Map<String, Object> image = weiXinUtil.getMaterialBatch("image", 0, 1);
        return image;
    }

    //===============================================================
    private WeiXinUtil getWeiXinUtil() {
        Account account = accountService.getAccount();
        WeiXinUtil weiXinUtil = new WeiXinUtil(account, restTemplate, redisTemplate, userService);
        return weiXinUtil;
    }
}
