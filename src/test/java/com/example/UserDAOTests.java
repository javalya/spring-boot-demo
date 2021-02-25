package com.example;

import com.example.domain.User;
import com.example.dao.UserDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserDAOTests {

    @Autowired
    private UserDAO userRepository;

    @Test
    public void test() throws Exception {



        userRepository.save(new User("aa1", "aa@126.com", "aa", "aa123456",new Date()));
        userRepository.save(new User("bb2", "bb@126.com", "bb", "bb123456",new Date()));
        userRepository.save(new User("cc3", "cc@126.com", "cc", "cc123456",new Date()));

        Assert.assertEquals(3, userRepository.findAll().size());
        Assert.assertEquals("bb123456", userRepository.findByUserNameOrEmail("bb2", "cc@126.com").getNickName());
        userRepository.delete(userRepository.findByUserName("aa1"));
//        userRepository.addUser("11@126.com", "1", "123", "aa123456","1aa");
    }
    @Test
    public void addUser() throws Exception{
        userRepository.addUser("aa", "1123", "12223@163.com", "aa",new Date());

    }


}
