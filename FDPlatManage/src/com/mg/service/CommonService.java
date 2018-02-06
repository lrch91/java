package com.mg.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mg.entity.FDArea;
import com.mg.vo.UserAuthVo;
import com.mg.vo.UserInfoVo;


public interface CommonService {
	
	String menuAuthority(String authPath, String userId);
	UserInfoVo findUserInfo(HttpServletRequest request);
	UserAuthVo userAuth(HttpServletRequest request);
	String login(String loginStr, String password, String imgCode, String ip, HttpServletRequest request, HttpServletResponse response);
	String logout(HttpServletRequest request,HttpServletResponse response);
	ArrayList<FDArea> getArea();
	String test();
}
