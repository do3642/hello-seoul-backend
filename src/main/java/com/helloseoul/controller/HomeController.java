package com.helloseoul.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.helloseoul.domain.TestVO;

@RestController
public class HomeController {

    @GetMapping("/test")
    public String helloSeoul() {
        return "Hello, Seoul!";
    }
    
    
	@GetMapping("/test2")
	public TestVO test2() {
		TestVO vo = new TestVO();
		vo.setId("abcd");
		vo.setPw("1234");
		vo.setAge(20);
		return vo;
	}
	@PostMapping("/test3/{no}")
	public void test3(@RequestBody TestVO vo, String msg, @PathVariable int no) {
		System.out.println(vo);
		System.out.println(msg);
		System.out.println(no);
	}
}
