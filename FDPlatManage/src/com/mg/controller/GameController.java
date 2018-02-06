package com.mg.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDPhotoSpots;
import com.mg.service.CommonService;
import com.mg.service.TradeService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/game")
public class GameController {
	@Autowired
	TradeService tradeService;
	@Autowired
	CommonService commonService;
	
	@ResponseBody
	@RequestMapping("/editPhotoSpots")
	public String editPhotoSpots(String id, String imagePath, Integer integralValue, String[] spots, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(imagePath))
			return Utils.getErrorStatusJson("图片路径为空").toJSONString();
		if(spots==null||spots.length<1)
			return Utils.getErrorStatusJson("设置点位为空").toJSONString();
		if(integralValue==null||integralValue<0)
			return Utils.getErrorStatusJson("积分值过小").toJSONString();
		String r = tradeService.editPhotoSpots(id, imagePath, integralValue, spots, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("设置找茬图片成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getPhotoSpotsList")
	public String getPhotoSpotsList(){
		List<FDPhotoSpots> psList = tradeService.getPhotoSpotsList();
		if(psList==null)
			return Utils.getErrorStatusJson("获取列表失败").toJSONString();
		return Utils.getSuccessEntityJson(psList).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getPhotoSpotsDetail")
	public String getPhotoSpotsDetail(String photoSpotsId){
		if(Utils.isEmpty(photoSpotsId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		FDPhotoSpots ps = tradeService.getPhotoSpotsDetail(photoSpotsId);
		if(ps==null)
			return Utils.getErrorStatusJson("获取详情失败").toJSONString();
		return Utils.getSuccessEntityJson(ps).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/delPhotoSpots")
	public String delPhotoSpots(String photoSpotsId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(photoSpotsId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = tradeService.delPhotoSpots(photoSpotsId, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("删除成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/setPhotoSpotsTop")
	public String setPhotoSpotsTop(String photoSpotsId, HttpServletRequest request){
		if(Utils.isEmpty(photoSpotsId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = tradeService.setPhotoSpotsTop(photoSpotsId, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("置顶成功").toJSONString();
	}
	
}
