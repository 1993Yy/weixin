package com.yy.service;

import cn.hutool.core.date.DateUtil;
import com.yy.entity.Account;
import com.yy.util.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class WeiXinService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountService accountService;

    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        WeiXinUtil weiXinUtil = getWeiXinUtil();
        weiXinUtil.authorize(request, response);

    }

    /**
     * 获取二维码
     * @param expireTime
     * @param senceID
     * @return
     */
    public Map<String,String> getQR(Integer expireTime,String senceID){
        WeiXinUtil weiXinUtil = getWeiXinUtil();
        Map<String, String> qr = weiXinUtil.getQr(expireTime, senceID);
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
//===============================================================
    private WeiXinUtil getWeiXinUtil() {
        Account account = accountService.getAccount();
        WeiXinUtil weiXinUtil=new WeiXinUtil(account,restTemplate,redisTemplate);
        return weiXinUtil;
    }
}
