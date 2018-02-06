package com.mg.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.mg.dao.AreaDao;
import com.mg.dao.ArticleDao;
import com.mg.dao.AuthorityGroupDao;
import com.mg.dao.AuthorityRelationDao;
import com.mg.dao.LoginIpFailDao;
import com.mg.dao.ManagerInfoDao;
import com.mg.dao.UserDao;
import com.mg.dao.UserLoginSessionDao;
import com.mg.dao.VManagerDao;
import com.mg.entity.FDArea;
import com.mg.entity.FDArticle;
import com.mg.entity.FDAuthorityGroup;
import com.mg.entity.FDAuthorityRelation;
import com.mg.entity.FDLoginIpFail;
import com.mg.entity.FDManagerInfo;
import com.mg.entity.FDUser;
import com.mg.entity.FDUserLoginSession;
import com.mg.entity.VFDManager;
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
	AreaDao areaDao;
	@Autowired
	ManagerInfoDao managerInfoDao;
	@Autowired
	UserLoginSessionDao userLoginSessionDao;
	@Autowired
	VManagerDao vManagerDao;
	@Autowired
	AuthorityRelationDao authorityRelationDao;
	@Autowired
	AuthorityGroupDao authorityGroupDao;
	@Autowired
	LoginIpFailDao loginIpFailDao;
	@Autowired
	ArticleDao articleDao;
	
	@Override
	public String menuAuthority(String authPath, String userId){
		VFDManager m = vManagerDao.findOne(" where userId='"+userId+"' and managerState="+Status.MANANGER_STATE_NORMAL+" and userStatus='"+Status.USER_NORMAL+"'");
		if(m!=null&&m.getId()!=null){
			FDAuthorityRelation ar = authorityRelationDao.findOne(" where authorityGroupId='"+m.getAuthorityGroupId()+"' ");
			if(ar==null||ar.getMenu()==null||ar.getMenu().equals("")){
				return "权限组无对应菜单";
			}
			String[] subMenus = ar.getMenu().split(",");
			if(subMenus==null||subMenus.length==0){
				return "无权限访问此路径";
			}
			Properties pps = Utils.getMenuAccessProperties();
			for(String subMenu:subMenus){
				System.out.println("==menuAuthority======subMenu:"+subMenu);
				String[] subMenuPaths = ((String) pps.get(subMenu)).split(",");
				for(String subMenuPath:subMenuPaths){
					if(authPath.equals(subMenuPath)){
						return null;
					}
				}
			}
			return "无权限访问此路径";
		}else{
			return "未获取到对应权限组信息";
		}
	}
	
	@Override
	public UserInfoVo findUserInfo(HttpServletRequest request){
		try{
			FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
			FDUser u = userDao.findById(uls.getUserId());
			FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+u.getId()+"' and state="+Status.MANANGER_STATE_NORMAL);
			if(mi==null)
				return null;
			UserInfoVo ui = new UserInfoVo();
			ui.setId(u.getId());
			ui.setImagePath(u.getImagePath());
			ui.setLoginName(u.getLoginName());
			ui.setPhone(u.getPhone());
			ui.setRegisterDate(u.getRegisterDate());
			ui.setStatus(u.getStatus());
			
			ui.setDepartment(mi.getDepartment());
			ui.setPosition(mi.getPosition());
			ui.setNumber(mi.getNumber());
			if(mi.getAuthorityGroupId()!=null){
				ui.setAuthorityGroupId(mi.getAuthorityGroupId());
				FDAuthorityGroup ag = authorityGroupDao.findOne(" where id='"+mi.getAuthorityGroupId()+"' ");
				if(ag!=null){
					ui.setAuthorityGroupName(ag.getGroupName());
					FDAuthorityRelation ar = authorityRelationDao.findOne(" where authorityGroupId='"+ag.getId()+"'");
					if(ar!=null&&ar.getMenu()!=null&&!ar.getMenu().equals("")){
						List<String> menus = new ArrayList<String>();
						for(String menu:ar.getMenu().split(",")){
							menus.add(menu);
						}
						if(menus!=null&&menus.size()>0){
							ui.setMenus(menus.toArray(new String[menus.size()]));
						}
					}
				}
			}
			ui.setState(mi.getState());
			ui.setIsSuper(mi.getIsSuper());
			return ui;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public UserAuthVo userAuth(HttpServletRequest request){
		if(Utils.getCookieValue(request, "FDSESSIONID")==null)
			return null;
		String fd = Utils.getCookieValue(request, "FDSESSIONID");
		FDUserLoginSession uls = userLoginSessionDao.findById(Utils.getCookieValue(request, "FDSESSIONID"));
		if(uls==null)
			return null;
	    long diff = (new Date()).getTime() - uls.getLoginTime().getTime();
	    long minutes = diff / (1000 * 60);
	    if(minutes>=60){
	    	//超过1小时
	    	userLoginSessionDao.deleteById(uls.getId());
	    	return null;
	    }else if(minutes>=10){
	    	//超过10分钟重写loginSession时间
	    	uls.setLoginTime(new Date());
	    	userLoginSessionDao.update(uls);
	    }
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
				FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+uList0.get(0).getId()+"' and state='"+Status.MANANGER_STATE_NORMAL+"' ");
				if(mi==null){
					return "非管理员账号";
				}
				
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
				System.out.println("------------- "+mGUserLoginSession.getId());
				Utils.resetCookie(request, response, "FDSESSIONID", mGUserLoginSession.getId());
				return null;
			}
		}
		List<FDUser> uList1 = userDao.findBycondition(" where loginName='"+loginStr+"' and status = '"+Status.USER_NORMAL+"'");
		if(uList1!=null&&uList1.size()>0){
			if(uList1.get(0).getPassword().equals(Utils.getPassWord(password, uList1.get(0).getSalt()))){
				FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+uList0.get(0).getId()+"' and state='"+Status.MANANGER_STATE_NORMAL+"' ");
				if(mi==null){
					return "非管理员账号";
				}
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
	public String test(){
		FDArticle a = new FDArticle();
		a.setId(UUID.randomUUID().toString());
		articleDao.add(a);
		if(1==1){
			TransactionStatus ts = TransactionAspectSupport.currentTransactionStatus();
			ts.setRollbackOnly();
		}
		
		FDUser u = new FDUser();
		u.setId(UUID.randomUUID().toString());
		userDao.add(u);
		return null;
	}
	
}
