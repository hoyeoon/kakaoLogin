package com.softsquared.kakaoAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class KakaoAPIController {
    private KakaoAPIService kakaoAPIService;

    @Autowired
    public KakaoAPIController(KakaoAPIService kakaoAPIService){
        this.kakaoAPIService = kakaoAPIService;
    }

    @RequestMapping(value="/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/login")
    public String login(@RequestParam("code") String code, HttpSession session) {
        String accessToken = kakaoAPIService.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoAPIService.getUserInfo(accessToken);

        System.out.println("1. code = " + code);
        System.out.println("2. accessToken = " + accessToken);
        System.out.println("3. userInfo = " + userInfo);

        //    클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
        if (userInfo.get("email") != null) {
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("access_Token", accessToken);
        }
        return "index";
    }

    @RequestMapping(value="/logout")
    public String logout(HttpSession session) {
        kakaoAPIService.kakaoLogout((String)session.getAttribute("access_Token"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        return "index";
    }
}