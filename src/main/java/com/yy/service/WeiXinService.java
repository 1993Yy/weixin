package com.yy.service;

import com.yy.entity.Account;
import com.yy.util.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

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
    private AccountService accountService;

    /**
     * 定时更新token
     */
    public void scheduleUpdateToken(){
        List<Account> accounts = accountService.getAllAccount();
        accounts.forEach(a->{
            WeiXinUtil weiXinUtil=new WeiXinUtil(a,restTemplate);
            int compare = a.getExpireTime().compareTo(new Date());
            if (compare==-1){
                String accesstoken = weiXinUtil.getAccesstoken();
                a.setAccessToken(accesstoken);
                Account account = accountService.updateAccount(a);

            }
        });
    }

}
