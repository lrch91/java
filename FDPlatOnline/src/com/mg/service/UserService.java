package com.mg.service;

import com.alibaba.fastjson.JSONObject;
import com.mg.entity.FDSystemParam;



public interface UserService {

	String signIn(String userId, FDSystemParam sp);
	JSONObject integralToCatcoinInfo(String userId,  FDSystemParam sp);
	String integralToCatcoin(String userId, Integer num, FDSystemParam sp);
	String donateCatcoin(String userId, String accepter, Integer num);
}
