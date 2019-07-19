package com.geekerstar.common.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected Claims claims;

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        Object object = request.getAttribute("user_claims");
        if (object != null){
            this.claims = (Claims)object;
            this.companyId = (String)claims.get("companyId");
            this.companyName = (String)claims.get("companyName");
        }
    }

}
