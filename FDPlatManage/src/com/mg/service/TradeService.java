package com.mg.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mg.entity.FDPhotoSpots;
import com.mg.entity.FDSystemParam;

public interface TradeService {
	String getIntegralList(String nameTel, String changeType, Integer page, Integer pageSize);
	String getCatcoinList(String nameTel, String changeType, Integer page, Integer pageSize);
	
	FDSystemParam getSystemParam();
	String updateParam(FDSystemParam sp, HttpServletRequest request, String adminId);
	
	String editPhotoSpots(String id, String imagePath ,Integer integralValue , String[] spots, HttpServletRequest request, String adminId);
	List<FDPhotoSpots> getPhotoSpotsList();
	FDPhotoSpots getPhotoSpotsDetail(String photoSpotsId);
	String delPhotoSpots(String photoSpotsId, HttpServletRequest request, String adminId);
	String setPhotoSpotsTop(String photoSpotsId, HttpServletRequest request);
	
	String updateSetFreightFeeDesc(String desc, HttpServletRequest request, String adminId);
}
