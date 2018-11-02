package com.example.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface UserDAO extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByUserNameOrEmail(String username, String email);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying
    @Query(value = "insert into user(user_name,pass_word,email,nick_name,reg_time) values(?1,?2,?3,?4,?5)",nativeQuery = true)
    int addUser(String userName, String passWord, String email, String nickName, String regTime);

}
