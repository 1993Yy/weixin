package com.yy.dao;

import com.yy.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Package: com.yy.dao
 * @ClassName: Account
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:38
 */
@Repository
public interface AccountDao extends JpaRepository<Account,Integer>, QuerydslPredicateExecutor<Account> {
}
