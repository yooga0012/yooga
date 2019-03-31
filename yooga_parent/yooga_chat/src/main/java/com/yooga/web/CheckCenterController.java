package com.yooga.web;

import com.yooga.entity.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {

	//页面请求
	@GetMapping("/socket/{uid}")
	public ModelAndView socket(@PathVariable String uid) {
		ModelAndView mav=new ModelAndView("/socket");
		mav.addObject("uid", uid);
		return mav;
	}
	//推送数据接口
	@ResponseBody
	@RequestMapping("/socket/push/{uid}")
	public Result pushToWeb(@PathVariable String uid, String message) {
		try {
			WebSocketServer.sendInfo(message,uid);
		} catch (IOException e) {

		}  
		return new Result(true, 200,"successs" ,uid);
	} 
}