package com.notatracer.sandbox.app.websocket.web.controller;

import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

//    @MessageMapping("/foo") 
//    public String handle(String greeting) {
//        return "foo";
//    }
    
    @RequestMapping(value = "/")
	public String index() {
		return "index";
	}
    
    @SubscribeMapping("/sqf")
    public String sqf() {
    	System.out.println("sqf");
    	return "inside sqf";
    }

    @SubscribeMapping("/topic/sqf")
    public String sqf2() {
    	System.out.println("sqf2");
    	return "inside sqf2";
    }

//    @SubscribeMapping("/app/topic")
    
}
