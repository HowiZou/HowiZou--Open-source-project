package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@EnableConfigurationProperties(JwtProperties.class)
//@Controller 这里不能放Controller、Service等注解了，其他的有@EnableConfigurationProperties注解的也一样，不能再次注入了
public class AuthController {
    @Autowired
    private AuthService authservice;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("login")
    public ResponseEntity login(@RequestParam("username")String username, @RequestParam("password")String password,
                                HttpServletRequest request, HttpServletResponse response){
        //调用service方法生成jwt
            String token = this.authservice.login(username,password);

            if(StringUtils.isBlank(token)){
                return ResponseEntity.badRequest().build();
            }
        //使用cookieUtils.setCookie方法，就可以把jwt类型的token设置进cookie
        CookieUtils.setCookie(request, response, token, jwtProperties.getCookieName(),60 * 30,"utf-8",true);
        return ResponseEntity.ok().build();
    }
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LEYOU_TOKEN")String token, HttpServletRequest request, HttpServletResponse response){


        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

            //刷新token   用户预测  缓式写入  20min
            String newToken = JwtUtils.generateToken(user, jwtProperties.getPrivateKey(), jwtProperties.getExpire());

            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),newToken,jwtProperties.getCookieMaxAge() * 60,null,true);//最后这个true表明无法用浏览器解析出cookie，能有效防止xss攻击
            if (null == user){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
