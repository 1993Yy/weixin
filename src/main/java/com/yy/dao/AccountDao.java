package com.yy.dao;

import com.yy.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Package: com.yy.dao
 * @ClassName: AccountDao
 * @Author: Created By Yy
 * @Date: 2019-05-09 10:15
 */
public interface AccountDao extends JpaRepository<Account,Integer>, JpaSpecificationExecutor<Account> {

}
