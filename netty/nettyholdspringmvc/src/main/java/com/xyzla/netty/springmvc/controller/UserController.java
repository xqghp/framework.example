package com.xyzla.netty.springmvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/user", produces = "text/json;charset=utf-8")
public class UserController {//extends BaseController{

    @RequestMapping("/login")
    @ResponseBody
    public String login(String username, String pwd) {
        JSONObject resultJson = new JSONObject();
        Map<String, String> loginResult = new HashMap<String, String>();
        loginResult.put("username", username);
        loginResult.put("age", "20");
        loginResult.put("sex", "boy");

        resultJson.put("code", 200);
        resultJson.put("msg", "登录成功");
        resultJson.put("result", loginResult);

        return JSONObject.toJSONString(resultJson);
    }


    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public void main(String username, String pwd) {
        System.out.println("TTT");
    }
}