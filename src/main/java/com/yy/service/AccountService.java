package com.yy.service;

import com.yy.dao.AccountDao;
import com.yy.entity.Account;
import com.yy.exception.ResultException;
import com.yy.exception.ReturnInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Package: com.yy.service
 * @ClassName: AccountService
 * @Author: Created By Yy
 * @Date: 2019-05-09 13:52
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 查看所有服务号
     * @return
     */
    public Account getAccount(){
        Optional<Account> account = accountDao.findById(1);
        if (!account.isPresent()){
            throw new ResultException(ReturnInfo.businessError.getCode(),"无可用服务号");
        }
        return account.get();
    }

    /**
     * 更新服务号
     * @return
     */
    public Account updateAccount(Account account){
       return accountDao.save(account);
    }
}
