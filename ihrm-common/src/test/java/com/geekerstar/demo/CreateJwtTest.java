package com.geekerstar.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.swing.*;
import java.util.Date;

public class CreateJwtTest {

    /**
     * 通过jjwt创建token
     */
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("88")
                .setSubject("Geekerstar")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "ihrm")
                .claim("companyId", "猿码律动")
                .claim("companyName", "成都");
        String token = jwtBuilder.compact();
        System.out.println(token);
    }
}
