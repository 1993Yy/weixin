package com.yy.service;

import cn.hutool.core.date.DateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yy.dao.AccountDao;
import com.yy.entity.Account;
import com.yy.common.exception.ResultException;
import com.yy.common.exception.ReturnInfo;
import com.yy.entity.QAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
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

    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory query;
    @PostConstruct
    private void init(){
        query=new JPAQueryFactory(entityManager);
    }

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
    @Transactional
    public long updateAccount(Account account){
        QAccount qAccount=QAccount.account;

       return query.update(qAccount)
               .where(qAccount.appID.eq(account.getAppID()))
               .set(qAccount.accessToken,account.getAccessToken())
               .set(qAccount.expireTime, DateUtil.offsetHour(new Date(),2))
               .execute();
    }
}
