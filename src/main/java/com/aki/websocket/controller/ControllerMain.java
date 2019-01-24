package com.aki.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControllerMain {

    @RequestMapping(value = "/")
    public String testwebsocket(HttpServletRequest request, Model model){

        String a0 = request.getScheme();
        String ServerName = request.getServerName();
        String a2 = request.getLocalAddr();
        int Port = request.getLocalPort();
        String a4 = request.getContextPath();
        model.addAttribute("Ip",ServerName);
        model.addAttribute("Port",Port);
        return "thymeleaf/testwebsocket";
    }
}
