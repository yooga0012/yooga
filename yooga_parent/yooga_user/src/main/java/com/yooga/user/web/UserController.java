package com.yooga.user.web;

import com.yooga.entity.result.Result;
import com.yooga.entity.result.StatusCode;
import com.yooga.user.pojo.Users;
import com.yooga.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     * @return
     */
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap){
        Users user = userService.findByLoginnameAndPassword(loginMap.get("username"), loginMap.get("password"));
        if(user!=null){
            return new Result(true,StatusCode.OK,"登陆成功",user.getId());
        }else{
            return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
        }
    }

    /**
     * 用户登陆
     * @return
     */
    @RequestMapping(value="/register",method=RequestMethod.POST)
    public Result register(@RequestBody Map<String,String> loginMap){
         Users users = new Users();
        users.setUsername(loginMap.get("username"));
        users.setPassword(loginMap.get("password"));
        userService.add(users);
            return new Result(true,StatusCode.OK,"成功");
    }
}
