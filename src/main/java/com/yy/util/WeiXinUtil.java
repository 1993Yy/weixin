package com.yy.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.yy.entity.Account;
import com.yy.exception.ResultException;
import com.yy.exception.ReturnInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @Package: com.yy.util
 * @ClassName: WeiXinUtil
 * @Author: Created By Yy
 * @Date: 2019-05-09 13:54
 */
@Slf4j
public class WeiXinUtil {
    private Account account;
    private RestTemplate restTemplate;

    public WeiXinUtil(Account account,RestTemplate restTemplate){
        this.account=account;
        this.restTemplate=restTemplate;
    }

    /**
     * 获取token
     * @return
     */
    public String getAccesstoken(){
        if (Objects.isNull(account)){
            throw new ResultException(ReturnInfo.businessError.getCode(),"无可用公众号");
        }
        String url=WeiXinUrl.tokenUrl_get.replace("APPID",account.getAppID())
                .replace("APPSECRET",account.getAccessToken());
        String dataStr = restTemplate.getForObject(url, String.class);
        JSONObject object = JSONObject.parseObject(dataStr);
        if (object.containsKey("errcode")){
            throw new ResultException(ReturnInfo.businessError.getCode(),(String) object.get("errmsg"));
        }
        return object.getString("access_token");
    }
}
