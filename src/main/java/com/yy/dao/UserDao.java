package com.yy.dao;

import com.yy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Package: com.yy.dao
 * @ClassName: UserDao
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:37
 */
@Repository
public interface UserDao extends JpaRepository<User,Integer>, QuerydslPredicateExecutor<User> {
}
