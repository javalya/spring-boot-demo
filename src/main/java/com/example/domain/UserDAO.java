package com.example.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public interface UserDAO extends JpaRepository<User, Long> {

    User findByUserName(String userName);
    User findByUserNameOrEmail(String username, String email);

    @Query(value = "select * from user  where nickName like %?1% or userName like %?1%",nativeQuery = true)
    List<User> findByUserNameLike(String userName);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying
    @Query(value = "insert into user(userName,passWord,email,nickName,regTime) values(?1,?2,?3,?4,?5)",nativeQuery = true)
    int addUser(String userName, String passWord, String email, String nickName, Date regTime);

}
