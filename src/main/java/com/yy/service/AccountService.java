package com.yy.service;

import com.yy.dao.AccountDao;
import com.yy.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Account> getAllAccount(){
        return accountDao.findAll();
    }

    /**
     * 更新服务号
     * @return
     */
    public Account updateAccount(Account account){
       return accountDao.save(account);
    }
}
