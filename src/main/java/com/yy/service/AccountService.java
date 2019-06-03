package com.yy.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yy.dao.AccountDao;
import com.yy.entity.Account;
import com.yy.entity.QAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @Package: com.yy.service
 * @ClassName: AccountService
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:41
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private EntityManager entityManager;

    private JPAQueryFactory query;

    @PostConstruct
    public void setQuery(){
        query=new JPAQueryFactory(entityManager);
    }

    /**
     * 获取公众号
     * @return
     */
    public Account getAccount(){
        Optional<Account> account = accountDao.findById(1);
        if (account.isPresent()){
            return account.get();
        }
        return null;
    }
    /**
     * 更新token
     * @param account
     * @return
     */
    @Transactional
    public long updateAccount(Account account){
        QAccount qaccount = QAccount.account;
        return query.update(qaccount)
                .set(qaccount.accessToken,account.getAccessToken())
                .set(qaccount.expireTime, account.getExpireTime())
                .where(qaccount.id.eq(account.getId()))
                .execute();
    }
}
