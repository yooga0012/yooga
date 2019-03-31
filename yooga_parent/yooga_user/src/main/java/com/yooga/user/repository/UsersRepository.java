package com.yooga.user.repository;

import com.yooga.user.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,String> {
    Users findByUsername(String loginname);
}
