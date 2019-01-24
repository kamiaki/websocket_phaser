package com.aki.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControllerMain {

    @RequestMapping(value = "/")
    public String testwebsocket(HttpServletRequest request){
        String a0 = request.getScheme();
        String a1 = request.getServerName();
        String a2 = request.getLocalAddr();
        int a3 = request.getLocalPort();
        String a4 = request.getContextPath();
        return "thymeleaf/testwebsocket";
    }
}
