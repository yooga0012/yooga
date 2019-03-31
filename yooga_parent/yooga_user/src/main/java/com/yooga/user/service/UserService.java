package com.yooga.user.service;

import com.yooga.user.pojo.Users;
import com.yooga.user.repository.UsersRepository;
import com.yooga.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    IdWorker idWorker;

    @Autowired
    UsersRepository usersRepository;


    public void add(Users user) {
        user.setId(idWorker.nextId()+""); //主键值
        //密码加密
        String newpassword = encoder.encode(user.getPassword());//加密后的密码
        user.setPassword(newpassword);
        usersRepository.save(user);
    }

    /**
     * 根据登陆名和密码查询
     * @param username
     * @param password
     * @return
     */
    public Users findByLoginnameAndPassword(String username, String password){
        Users user = usersRepository.findByUsername(username);
        if( user!=null && encoder.matches(password,user.getPassword())){
            return user;
        }else{
            return null;
        }
    }

}
