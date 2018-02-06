package com.mg.service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mg.dao.AreaDao;
import com.mg.dao.IntegralChangeDao;
import com.mg.dao.LoginIpFailDao;
import com.mg.dao.ManagerInfoDao;
import com.mg.dao.MobAccountDao;
import com.mg.dao.SignInRecordDao;
import com.mg.dao.UserDao;
import com.mg.dao.UserLoginSessionDao;
import com.mg.entity.FDArea;
import com.mg.entity.FDIntegralChange;
import com.mg.entity.FDLoginIpFail;
import com.mg.entity.FDManagerInfo;
import com.mg.entity.FDMobAccount;
import com.mg.entity.FDSignInRecord;
import com.mg.entity.FDSystemParam;
import com.mg.entity.FDUser;
import com.mg.entity.FDUserLoginSession;
import com.mg.util.Status;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;
import com.mg.vo.UserInfoVo;

@Service
@Transactional 
public class CommonServiceImpl implements CommonService {

	@Autowired
	UserDao userDao;
	@Autowired
	MobAccountDao mobAccountDao;
	@Autowired
	ManagerInfoDao managerInfoDao;
	@Autowired
	AreaDao areaDao;
	@Autowired
	UserLoginSessionDao userLoginSessionDao;
	@Autowired
	IntegralChangeDao integralChangeDao;
	@Autowired
	SignInRecordDao signInRecordDao;
	@Autowired
	LoginIpFailDao loginIpFailDao;
	
	@Override
	public String register(String msgCode,String phone,String password, String city, String area, String street, HttpServletRequest request, HttpServletResponse response){
		try {
			phone = Utils.base64_decode(phone);
			System.out.println("-=-----"+phone);
			phone = phone.substring(4, phone.length()-4);
			password = Utils.base64_decode(password);
			System.out.println("-=-----"+password);
			password = password.substring(4, password.length()-4);
			
			if(Utils.getCookieValue(request, "vCode")==null)
				return "短信验证码为空";
			if(Utils.getCookieValue(request, "validateNum")==null)
				return "未获取到验证次数";
			if(Utils.getCookieValue(request, "mobilenumber")==null)
				return "手机号为空";
			String vCode = Utils.getCookieValue(request, "vCode");
			Integer validateNum = Integer.parseInt(Utils.getCookieValue(request, "validateNum"));
			if(validateNum>=3)
				return "验证次数过多，请重新获取短信验证码";
			String mobilenumber = Utils.getCookieValue(request, "mobilenumber");
			if(!phone.equals(mobilenumber))
				return "验证手机号不正确";
			if(!vCode.equals(Utils.getPassWord(msgCode,"7480"))){
				return "短信验证码不正确";
			}
			FDUser mGUser = new FDUser();
			mGUser.setId(UUID.randomUUID().toString());
			Integer salt = (int)(Math.random()*9000+1000);
			mGUser.setSalt(salt.toString());
			mGUser.setPhone(phone);
			mGUser.setCity(city);
			mGUser.setArea(area);
			mGUser.setStreet(street);
			mGUser.setRegisterDate(new Date());
			mGUser.setStatus(Status.USER_NORMAL); //注册用户直接可用
			mGUser.setPassword(Utils.getPassWord(password, mGUser.getSalt()));
			mGUser.setPayPwd(mGUser.getPassword());
			mGUser.setCatcoin(0);
			mGUser.setIntegral(0);
			mGUser.setHistoryBalance(0.0);
			mGUser.setHistoryIntegral(0);
			mGUser.setIsMember(Status.USER_ISMEMBER_NO);
			mGUser.setIntegralName(0);
			mGUser.setIntegralGender(0);
			mGUser.setIntegralDetailAddress(0);
			mGUser.setIntegralCrop(0);
			mGUser.setVerifyState(Status.USER_VERIFY_VERIFIED);//注册用户无需审核
			List<FDUser> list1 =userDao.findBycondition(" where phone='"+phone+"' ");
			if(list1!=null&&list1.size()>0)
				return "手机号已占用";
			userDao.add(mGUser);
			
			//删除cookie
			Utils.delCookie(request, response, "vCode");
			Utils.delCookie(request, response, "validateNum");
			Utils.delCookie(request, response, "mobilenumber");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统异常";
		}
	}
	
	@Override
	public String login(String loginStr, String password, String imgCode, String ip, HttpServletRequest request, HttpServletResponse response){
//		if(Utils.getCookieValue(request, "imgCode")==null)
//			return "图形验证码为空";
//		if(!Utils.getCookieValue(request, "imgCode").equals(Utils.getPassWord(imgCode.toUpperCase(), "7480")))
//			return "图形验证码不匹配";
		loginStr = Utils.base64_decode(loginStr);
		loginStr = loginStr.substring(4, loginStr.length()-4);
		password = Utils.base64_decode(password);
		password = password.substring(4, password.length()-4);
		
//		String ip = Utils.getIpAddr(request);
		System.out.println("================LOGIN_IP:"+ip);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -1);
		ArrayList<FDLoginIpFail> rList = (ArrayList<FDLoginIpFail>) loginIpFailDao.findBycondition(" where address = '"+ip+"' and loginDate > '"+Utils.getSimpleDateFormat().format(cal.getTime())+"' ");
		Integer count = 0;
		if(rList!=null&&rList.size()>0){
			count = rList.get(0).getFailCount();
			if(count>=5){
				Calendar c = Calendar.getInstance();
				c.setTime(rList.get(0).getLoginDate());
				c.add(Calendar.HOUR_OF_DAY, 1);
				return "登录失败次数过多，请于"+Utils.getSimpleDateFormat().format(c.getTime())+"后重试!";
			}
		}
		
		List<FDUser> uList0 = userDao.findBycondition(" where phone='"+loginStr+"' and status = '"+Status.USER_NORMAL+"'");
		if(uList0!=null&&uList0.size()>0){
			if(uList0.get(0).getPassword().equals(Utils.getPassWord(password, uList0.get(0).getSalt()))){
				List<FDUserLoginSession> ulsList2 = userLoginSessionDao.findBycondition(" where userId= '"+uList0.get(0).getId()+"'");
				if(ulsList2!=null&&ulsList2.size()>0){
					for(int i=0;i<ulsList2.size();i++){
						userLoginSessionDao.deleteById(ulsList2.get(i).getId());
					}
				}
				//SESSION写入数据库中
				FDUserLoginSession mGUserLoginSession = new FDUserLoginSession();
				mGUserLoginSession.setId(UUID.randomUUID().toString());
				mGUserLoginSession.setUserId(uList0.get(0).getId());
				mGUserLoginSession.setLoginStatus(Status.LOGINSESSION_ONLINE);
				mGUserLoginSession.setLoginTime(new Date());
				userLoginSessionDao.add(mGUserLoginSession);
				//写入COOKIE
				Utils.resetCookie(request, response, "FDSESSIONID", mGUserLoginSession.getId());
				return null;
			}
		}
		List<FDUser> uList1 = userDao.findBycondition(" where loginName='"+loginStr+"' and status = '"+Status.USER_NORMAL+"'");
		if(uList1!=null&&uList1.size()>0){
			if(uList1.get(0).getPassword().equals(Utils.getPassWord(password, uList1.get(0).getSalt()))){
				List<FDUserLoginSession> ulsList1 = userLoginSessionDao.findBycondition(" where userId= '"+uList1.get(0).getId()+"'");
				if(ulsList1!=null&&ulsList1.size()>0){
					for(int i=0;i<ulsList1.size();i++){
						userLoginSessionDao.deleteById(ulsList1.get(i).getId());
					}
				}
				//SESSION写入数据库中
				FDUserLoginSession mGUserLoginSession = new FDUserLoginSession();
				mGUserLoginSession.setId(UUID.randomUUID().toString());
				mGUserLoginSession.setUserId(uList1.get(0).getId());
				mGUserLoginSession.setLoginStatus(Status.LOGINSESSION_ONLINE);
				mGUserLoginSession.setLoginTime(new Date());
				userLoginSessionDao.add(mGUserLoginSession);
				//写入COOKIE
				Utils.resetCookie(request, response, "FDSESSIONID", mGUserLoginSession.getId());
				return null;
			}
		}
		if(count>0){
			FDLoginIpFail lif = rList.get(0);
			lif.setLoginDate(new Date());
			lif.setFailCount(lif.getFailCount()+1);
			loginIpFailDao.update(lif);
		}else{
			ArrayList<FDLoginIpFail> list1 = (ArrayList<FDLoginIpFail>) loginIpFailDao.findBycondition(" where address = '"+ip+"' ");
			if(list1!=null&&list1.size()>0){
				FDLoginIpFail lif = list1.get(0);
				lif.setLoginDate(new Date());
				lif.setFailCount(1);
				loginIpFailDao.update(lif);
			}else{
				FDLoginIpFail lif = new FDLoginIpFail();
				lif.setId(UUID.randomUUID().toString());
				lif.setAddress(ip);
				lif.setFailCount(1);
				lif.setLoginDate(new Date());
				loginIpFailDao.add(lif);
			}
		}
		return "账号或密码错误";
	}
	
	@Override
	public String logout(HttpServletRequest request,HttpServletResponse response){
		String csId = Utils.getCookieValue(request, "FDSESSIONID");
		if(csId==null)
			return "未找到登录信息";
		FDUserLoginSession uls = userLoginSessionDao.findById(csId);
		if(uls==null)
			return "未找到登录记录";
		userLoginSessionDao.deleteById(uls.getId());
		Utils.delCookie(request, response, "FDSESSIONID");
		return null;
	}
	
	@Override
	public void getImgCode(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
		BufferedImage imge = Utils.getImagCode(request, response);
		try {
            ImageIO.write(imge, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public String getMessageCode(String phone,String imgCode,HttpServletRequest request, HttpServletResponse response){
		try {
			response.setHeader("Content-type","application/json; charset=UTF-8");
			if(Utils.isEmpty(phone)){
				return "请输入手机号";
			}
			if(Utils.isEmpty(imgCode)){
				return "请输入图形验证码";
			}
			if(Utils.getCookieValue(request, "imgCode")==null){
				return "图片验证码为空";
			}
			if(!(Utils.getPassWord(imgCode.toUpperCase(), "7480")).equals(Utils.getCookieValue(request, "imgCode"))){
				return "图片验证码填写错误";
			}
			String code = this.getVCode();
			String msgContent ="【富岛】您的验证码是：" + code + "，请勿将验证码泄露给其他人。如非本人操作，请忽略本短信。";
			Utils.sendmsg(phone, msgContent);
			Utils.resetCookie(request, response, "vCode", Utils.getPassWord(code,"7480"));
			Utils.resetCookie(request, response, "validateNum", "0");
			Utils.resetCookie(request, response, "mobilenumber", phone);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统异常";
		}
	}
	
	@Override
	public String bindPhone(String msgCode,HttpServletRequest request,HttpServletResponse response){
		if(Utils.getCookieValue(request, "vCode")==null)
			return "短信验证码为空";
		if(Utils.getCookieValue(request, "validateNum")==null)
			return "短信验证次数为空";
		if(Utils.getCookieValue(request, "mobilenumber")==null)
			return "手机号为空";
		String phone = Utils.getCookieValue(request, "mobilenumber");
		Integer validateNum = Integer.parseInt(Utils.getCookieValue(request, "validateNum"));
		if(validateNum>=3){
			Utils.delCookie(request, response, "vCode");
			Utils.delCookie(request, response, "validateNum");
			return "验证次数大于等于3次";
		}
		String vCode = Utils.getCookieValue(request, "vCode");
		if(!vCode.equals(Utils.getPassWord(msgCode,"7480"))){
			Utils.resetCookie(request, response, "validateNum", String.valueOf(validateNum+1));
			return "短信验证码不正确";
		}
		List<FDUser> uList = userDao.findBycondition(" where phone = '"+phone+"' ");
		if(uList!=null&&uList.size()>0)
			return "该手机号已被绑定";
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		FDUser u = userDao.findById(uls.getUserId());
		if(!Utils.isEmpty(u.getPhone()))
			return "当前用户已绑定手机号";
		u.setPhone(phone);
		userDao.update(u);
		Utils.delCookie(request, response, "vCode");
		Utils.delCookie(request, response, "validateNum");
		return null;	
	}
	
	@Override
	public String updatePwdByPhone(String msgCode,String phone,String password, HttpServletRequest request,HttpServletResponse response){
		if(Utils.getCookieValue(request, "vCode")==null)
			return "短信验证码为空";
		if(Utils.getCookieValue(request, "validateNum")==null)
			return "短信验证次数为空";
		if(Utils.getCookieValue(request, "mobilenumber")==null)
			return "手机号为空";
		if(!phone.equals(Utils.getCookieValue(request, "mobilenumber")))
			return "手机号不一致";
		Integer validateNum = Integer.parseInt(Utils.getCookieValue(request, "validateNum"));
		if(validateNum>=3){
			Utils.delCookie(request, response, "vCode");
			Utils.delCookie(request, response, "validateNum");
			return "验证次数大于等于3次";
		}
		String vCode = Utils.getCookieValue(request, "vCode");
		if(!vCode.equals(Utils.getPassWord(msgCode,"7480"))){
			Utils.resetCookie(request, response, "validateNum", String.valueOf(validateNum+1));
			return "短信验证码不正确";
		}
		List<FDUser> list = userDao.findBycondition(" where phone='"+phone+"' ");
		if(list==null||list.size()<1)
			return "未找到用户信息";
		if(list.size()>1)
			return "手机号被多人绑定，请联系管理员";
		FDUser u = list.get(0);
		u.setPassword(Utils.getPassWord(password, u.getSalt()));
		userDao.update(u);
		Utils.delCookie(request, response, "vCode");
		Utils.delCookie(request, response, "validateNum");
		return null;
	}
	
	@Override
	public String updatePaypwdByPhone(String msgCode,String phone,String paypwd, HttpServletRequest request,HttpServletResponse response){
		if(Utils.getCookieValue(request, "vCode")==null)
			return "短信验证码为空";
		if(Utils.getCookieValue(request, "validateNum")==null)
			return "短信验证次数为空";
		if(Utils.getCookieValue(request, "mobilenumber")==null)
			return "手机号为空";
		if(!phone.equals(Utils.getCookieValue(request, "mobilenumber")))
			return "手机号不一致";
		Integer validateNum = Integer.parseInt(Utils.getCookieValue(request, "validateNum"));
		if(validateNum>=3){
			Utils.delCookie(request, response, "vCode");
			Utils.delCookie(request, response, "validateNum");
			return "验证次数大于等于3次";
		}
		String vCode = Utils.getCookieValue(request, "vCode");
		if(!vCode.equals(Utils.getPassWord(msgCode,"7480"))){
			Utils.resetCookie(request, response, "validateNum", String.valueOf(validateNum+1));
			return "短信验证码不正确";
		}
		List<FDUser> list = userDao.findBycondition(" where phone='"+phone+"' ");
		if(list==null||list.size()<1)
			return "未找到用户信息";
		if(list.size()>1)
			return "手机号被多人绑定，请联系管理员";
		FDUser u = list.get(0);
		u.setPayPwd(Utils.getPassWord(paypwd, u.getSalt()));
		userDao.update(u);
		Utils.delCookie(request, response, "vCode");
		Utils.delCookie(request, response, "validateNum");
		return null;
	}
	
	@Override
	public String updatePwdByPwd(String oldpwd, String newpwd, HttpServletRequest request){
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		FDUser u = userDao.findById(uls.getUserId());
		if(!u.getPassword().equals(Utils.getPassWord(oldpwd, u.getSalt())))
			return "旧密码不匹配";
		u.setPassword(Utils.getPassWord(newpwd, u.getSalt()));
		userDao.update(u);
		return null;
	}
	
	@Override
	public String updateUserName(HttpServletRequest request,String userName) {
		List<FDUser> list = userDao.findBycondition(" where loginName ='"+userName+"' ");
		if(list!=null&&list.size()>0)
			return "用户名已占用";
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		FDUser u = userDao.findById(uls.getUserId());
		u.setLoginName(userName);
		userDao.update(u);
		return null;
	}
	
	@Override
	public UserInfoVo findUserInfo(HttpServletRequest request, List<Map<String, Object>> csList, List<Map<String, Object>> gsList ){
		try{
			FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
			FDUser u = userDao.findById(uls.getUserId());
			UserInfoVo ui = new UserInfoVo();
			ui.setId(u.getId());
			ui.setCatcoin(u.getCatcoin());
			ui.setIntegral(u.getIntegral());
			ui.setHistoryBalance(u.getHistoryBalance());
			ui.setHistoryIntegral(u.getHistoryIntegral());
			ui.setImagePath(u.getImagePath());
			ui.setIntegral(u.getIntegral());
			ui.setLoginName(u.getLoginName());
			ui.setPhone(u.getPhone());
			ui.setRegisterDate(u.getRegisterDate());
			ui.setStatus(u.getStatus());
//			ui.setTrueName(u.getTrueName());
			ui.setIsMember(u.getIsMember());
			Integer memberLevelTag = 0;
			String memberLevelName = "";
			if(Status.USER_ISMEMBER_YES.equals(u.getIsMember())){
				if(csList!=null&&csList.size()>0){
					Double historyBalance = u.getHistoryBalance();
					for(int i=0;i<csList.size();i++){
						if((int)csList.get(i).get("LEVEL")==u.getManualMemberLevel()){
							historyBalance += (int)csList.get(i).get("VALUE");
						}
					}
					for(int i=0;i<csList.size();i++){
						if(historyBalance>=(int)csList.get(i).get("VALUE")){
							memberLevelTag++;
						}
					}
					ui.setMemberLevel(memberLevelTag);
					for(int i=0;i<csList.size();i++){
						if(memberLevelTag==(int)csList.get(i).get("LEVEL")){
							memberLevelName = (String)csList.get(i).get("NAME");
						}
					}
					ui.setMemberLevelName(memberLevelName);
				}
			}
			Integer userLevelTag = 0;
			String userLevelName = "";
			if(gsList!=null&&gsList.size()>0){
				for(int i=0;i<gsList.size();i++){
					if(u.getHistoryIntegral()>=(int)gsList.get(i).get("VALUE")){
						userLevelTag++;
					}
				}
				ui.setUserLevel(userLevelTag);
				for(int i=0;i<gsList.size();i++){
					if(userLevelTag==(int)gsList.get(i).get("LEVEL")){
						userLevelName = (String)gsList.get(i).get("NAME");
					}
				}
				ui.setUserLevelName(userLevelName);
			}
			if(u.getVerifyDate()!=null){
				ui.setVerifyDate(u.getVerifyDate());
			}
			if(u.getGender()!=null){
				ui.setGender(u.getGender());
			}
			ui.setVerifyState(u.getVerifyState());
			ui.setCity(u.getCity());
			FDArea a1 = areaDao.findOne(" where id='"+u.getCity()+"' ");
			if(a1!=null){
				ui.setCityName(a1.getName());
			}
			ui.setArea(u.getArea());
			FDArea a2 = areaDao.findOne(" where id='"+u.getArea()+"' ");
			if(a2!=null){
				ui.setAreaName(a2.getName());
			}
			ui.setStreet(u.getStreet());
			FDArea a3 = areaDao.findOne(" where id='"+u.getStreet()+"' ");
			if(a3!=null){
				ui.setStreetName(a3.getName());
			}
			if(u.getDetailAddress()!=null){
				ui.setDetailAddress(u.getDetailAddress());
			}
			if(u.getMyCrops()!=null){
				ui.setMyCrops(u.getMyCrops());
			}
			ui.setIntegralCrop(u.getIntegralCrop());
			ui.setIntegralDetailAddress(u.getIntegralDetailAddress());
			ui.setIntegralGender(u.getIntegralGender());
			ui.setIntegralName(u.getIntegralName());
			
			FDSignInRecord sir = signInRecordDao.findOne(" where userId='"+u.getId()+"' and signDate='"+Utils.getCurrentTimeString().substring(0,10)+"' ");
			if(sir!=null){
				ui.setIsSignIn(1);
				FDIntegralChange sic = integralChangeDao.findOne(" where userId='"+u.getId()+"' and attachId='"+sir.getId()+"' ");
				if(sic!=null){
					ui.setSignInIntegral(sic.getChangeSum());
				}
			}else{
				ui.setIsSignIn(0);
			}
			return ui;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public String updateUserInfo(FDUser u, FDSystemParam sp){
		try{
			FDUser tempUser = userDao.findById(u.getId());
			FDIntegralChange ic1 = new FDIntegralChange();
			FDIntegralChange ic2 = new FDIntegralChange();
			FDIntegralChange ic3 = new FDIntegralChange();
			FDIntegralChange ic4 = new FDIntegralChange();
			if(!Utils.isEmpty(u.getLoginName())){
				tempUser.setLoginName(u.getLoginName());
				if(tempUser.getIntegralName().equals(0)){
					tempUser.setIntegralName(1);
					tempUser.setIntegral(tempUser.getIntegral()+sp.getCompleteName());
					tempUser.setHistoryIntegral(tempUser.getHistoryIntegral()+sp.getCompleteName()); 
					
					ic1.setId(UUID.randomUUID().toString());
					ic1.setUserId(tempUser.getId());
					ic1.setChangeDate(new Date());
					ic1.setChangeSum(sp.getCompleteName());
					ic1.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_TRUENAME);
					ic1.setNowIntegral(tempUser.getIntegral());
					integralChangeDao.add(ic1);
				}
			}
			if(!Utils.isEmpty(u.getGender())){
				tempUser.setGender(u.getGender());
				if(tempUser.getIntegralGender().equals(0)){
					tempUser.setIntegralGender(1);
					tempUser.setIntegral(tempUser.getIntegral()+sp.getCompleteGender()); 
					tempUser.setHistoryIntegral(tempUser.getHistoryIntegral()+sp.getCompleteGender()); 
					
					ic2.setId(UUID.randomUUID().toString());
					ic2.setUserId(tempUser.getId());
					ic2.setChangeDate(new Date());
					ic2.setChangeSum(sp.getCompleteGender());
					ic2.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_GENDER);
					ic2.setNowIntegral(tempUser.getIntegral());
					integralChangeDao.add(ic2);
				}
			}
			if(!Utils.isEmpty(u.getCity())){
				tempUser.setCity(u.getCity());
			}
			if(!Utils.isEmpty(u.getArea())){
				tempUser.setArea(u.getArea());
			}
			if(!Utils.isEmpty(u.getStreet())){
				tempUser.setStreet(u.getStreet());
			}
			if(!Utils.isEmpty(u.getMyCrops())){
				tempUser.setMyCrops(u.getMyCrops());
				if(tempUser.getIntegralCrop().equals(0)){
					tempUser.setIntegralCrop(1);
					tempUser.setIntegral(tempUser.getIntegral()+sp.getCompleteCrop()); 
					tempUser.setHistoryIntegral(tempUser.getHistoryIntegral()+sp.getCompleteCrop());
					
					ic3.setId(UUID.randomUUID().toString());
					ic3.setUserId(tempUser.getId());
					ic3.setChangeDate(new Date());
					ic3.setChangeSum(sp.getCompleteCrop());
					ic3.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_MYCROPS);
					ic3.setNowIntegral(tempUser.getIntegral());
					integralChangeDao.add(ic3);
				}
			}
			if(!Utils.isEmpty(u.getDetailAddress())){
				tempUser.setDetailAddress(u.getDetailAddress());
				if(tempUser.getIntegralDetailAddress().equals(0)){
					tempUser.setIntegralDetailAddress(1);
					tempUser.setIntegral(tempUser.getIntegral()+sp.getCompleteDetailAddress()); 
					tempUser.setHistoryIntegral(tempUser.getHistoryIntegral()+sp.getCompleteDetailAddress()); 
					
					ic4.setId(UUID.randomUUID().toString());
					ic4.setUserId(tempUser.getId());
					ic4.setChangeDate(new Date());
					ic4.setChangeSum(sp.getCompleteDetailAddress()); 
					ic4.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_DETAILADDRESS);
					ic4.setNowIntegral(tempUser.getIntegral());
					integralChangeDao.add(ic4);
				}
			}
			userDao.update(tempUser);
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("系统异常");
		}
	}
	
	@Override
	public String updatemobilestemcode(HttpServletRequest request, HttpServletResponse response,String imgCode){
		try {
			if(Utils.getCookieValue(request, "imgCode")==null)
				return "图片验证码为空";
			if(!Utils.getCookieValue(request, "imgCode").equals(Utils.getPassWord(imgCode.toUpperCase(), "7480")))
				return "图片验证码不匹配";
			FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
			FDUser u = userDao.findById(uls.getUserId());
			if(Utils.isEmpty(u.getPhone()))
				return "未绑定手机号";
			String code = this.getVCode();
			String msgContent ="【富岛】您的验证码是：" + code + "，请勿将验证码泄露给其他人。如非本人操作，请忽略本短信。";
			Utils.sendmsg(u.getPhone(), msgContent);
			Utils.resetCookie(request, response, "vCode", Utils.getPassWord(code,"7480"));
			Utils.resetCookie(request, response, "validateNum", "0");
			Utils.resetCookie(request, response, "vpwd", Utils.getPassWord(u.getPhone(),Utils.getPassWord(code,"7480")));
			Utils.delCookie(request, response, "imgCode");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统异常";
		}
	}
	
	private String getVCode() {
		String vCode = "";
		while (vCode.length() < 6) {
			vCode += (int) (Math.random() * 10);
		}
		return vCode;
	}
	
	@Override
	public String updateMobileStepOne(HttpServletRequest request, HttpServletResponse response,String msgCode){
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		FDUser u = userDao.findById(uls.getUserId());
		if(Utils.getCookieValue(request, "vpwd")==null)
			return "密码为空";
		if(Utils.getCookieValue(request, "vCode")==null)
			return "短信验证码为空";
		if(Utils.getCookieValue(request, "validateNum")==null)
			return "验证次数为空";
		String vCode = Utils.getCookieValue(request, "vCode");
		String vpwd = Utils.getCookieValue(request, "vpwd");
		Integer validateNum = Integer.parseInt(Utils.getCookieValue(request, "validateNum"));
		if(validateNum>=3){
			Utils.resetCookie(request, response, "validateNum", String.valueOf(validateNum+1));
			return "验证次数大于等于3次";
		}
		if(!vCode.equals(Utils.getPassWord(msgCode,"7480")) && vpwd.equals(Utils.getPassWord(u.getPhone(),Utils.getPassWord(msgCode,"7480"))))
			return "短信验证码验证失败";
		Utils.resetCookie(request, response, "vpwdsecond",  Utils.getPassWord(u.getPhone(),"7480"));
		Utils.delCookie(request, response, "vCode");
		Utils.delCookie(request, response, "validateNum");
		return null;
	}
	
	@Override
	public String updateMobileStepTwo(HttpServletRequest request, HttpServletResponse response,String msgCode,String newPhone){
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		FDUser u = userDao.findById(uls.getUserId());
		if(Utils.getCookieValue(request, "vpwdsecond")==null)
			return "密码为空";
		if(Utils.getCookieValue(request, "vCode")==null)
			return "短信验证码为空";
		if(Utils.getCookieValue(request, "validateNum")==null)
			return "验证次数为空";
		String vCode = Utils.getCookieValue(request, "vCode");
		String vpwdsecond = Utils.getCookieValue(request, "vpwdsecond");
		Integer validateNum = Integer.parseInt(Utils.getCookieValue(request, "validateNum"));
		if(validateNum>=3){
			Utils.resetCookie(request, response, "validateNum", String.valueOf(validateNum+1));
			return "验证次数大于等于3次";
		}
		if(!vCode.equals(Utils.getPassWord(msgCode,"7480")) && vpwdsecond.equals(Utils.getPassWord(u.getPhone(),Utils.getPassWord(msgCode,"7480"))))
			return "短信验证码验证失败";
		u.setPhone(newPhone);
		userDao.update(u);
		Utils.resetCookie(request, response, "vpwdsecond",  Utils.getPassWord(u.getPhone(),"7480"));
		Utils.delCookie(request, response, "vCode");
		Utils.delCookie(request, response, "validateNum");
		return null;
	}
	
	@Override
	public UserAuthVo userAuth(HttpServletRequest request){
		if(Utils.getCookieValue(request, "FDSESSIONID")==null)
			return null;
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		if(uls==null)
			return null;
//	    long diff = (new Date()).getTime() - uls.getLoginTime().getTime();
//	    long minutes = diff / (1000 * 60);
//	    if(minutes>=60){
//	    	//超过1小时
//	    	userLoginSessionDao.deleteById(uls.getId());
//	    	return null;
//	    }else if(minutes>=10){
//	    	//超过10分钟重写loginSession时间
//	    	uls.setLoginTime(new Date());
//	    	userLoginSessionDao.update(uls);
//	    }
		FDUser u = userDao.findById(uls.getUserId());
		if(u==null)
			return null;
		if(!Status.USER_NORMAL.equals(u.getStatus()))
			return null;
		UserAuthVo ua = new UserAuthVo();
		ua.setId(u.getId());
		ua.setLoginName(u.getLoginName());
		ua.setPhone(u.getPhone());
//		ua.setTrueName(u.getTrueName());
		ua.setCommon("YES");
		FDManagerInfo mi = managerInfoDao.findOne(" where userId = '"+u.getId()+"' and state="+Status.MANANGER_STATE_NORMAL);
		if(mi!=null){
			ua.setManager("YES");
			if(Status.MANANGER_SUPERMANAGER_YES.equals((mi.getIsSuper()))){
				ua.setSuperManager("YES");
			}else{
				ua.setSuperManager("NO");
			}
		}else{
			ua.setManager("NO");
		}
		return ua;
	}
	
	@Override
	@Cacheable(value="areaCache")
	public ArrayList<FDArea> getArea(){
		ArrayList<FDArea> aList1 = (ArrayList<FDArea>) areaDao.findBycondition(" where parentId='450000' and level = 2 ");
		for(FDArea a1:aList1){
			ArrayList<FDArea> aList2 = (ArrayList<FDArea>) areaDao.findBycondition(" where parentId='"+a1.getId()+"' and level = 3 ");
			for(FDArea a2:aList2){
				ArrayList<FDArea> aList3 = (ArrayList<FDArea>) areaDao.findBycondition(" where parentId='"+a2.getId()+"' and level = 4 ");
				a2.setSubAreaList(aList3);
			}
			a1.setSubAreaList(aList2);
		}
		return aList1;
	}
	
	@Override
	public HashMap<String, HashMap<String, String>> getLoginNameByPhones(String[] phones, List<Map<String, Object>> csList){
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		for(String phone:phones){
			List<FDUser> userList = userDao.findBycondition(" where phone='"+phone+"' ");
			if(userList!=null&&userList.size()>0){
				FDUser u = userList.get(0);
				HashMap<String, String> tm = new HashMap<String, String>();
				tm.put("loginName", userList.get(0).getLoginName());
				
				Integer memberLevelTag = 0;
				String memberLevelName = "";
				if(Status.USER_ISMEMBER_YES.equals(u.getIsMember())){
					if(csList!=null&&csList.size()>0){
						Double historyBalance = u.getHistoryBalance();
						for(int i=0;i<csList.size();i++){
							if((int)csList.get(i).get("LEVEL")==u.getManualMemberLevel()){
								historyBalance += (int)csList.get(i).get("VALUE");
							}
						}
						for(int i=0;i<csList.size();i++){
							if(historyBalance>=(int)csList.get(i).get("VALUE")){
								memberLevelTag++;
							}
						}
						for(int i=0;i<csList.size();i++){
							if(memberLevelTag==(int)csList.get(i).get("LEVEL")){
								memberLevelName = (String)csList.get(i).get("NAME");
							}
						}
					}
				}
				tm.put("levelName", memberLevelName);
				map.put(phone, tm);
			}
		}
		return map;
	}
	
	@Override
	public HashMap<String, String> getMobNameByAccounts(String[] accounts){
		HashMap<String, String> map = new HashMap<String, String>();
		for(String account:accounts){
			List<FDMobAccount> mobAccountList = mobAccountDao.findBycondition(" where account='"+account+"' ");
			if(mobAccountList!=null&&mobAccountList.size()>0){
				map.put(account, mobAccountList.get(0).getName());
			}else{
				map.put(account, account);
			}
		}
		return map;
	}
}
