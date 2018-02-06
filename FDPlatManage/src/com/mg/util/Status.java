package com.mg.util;

public final class Status {

	//FDLoginSession
	public static final String LOGINSESSION_ONLINE = "ONLINE";
	public static final String LOGINSESSION_OFFLINE = "OFFLINE";
	
	//FDUser
	public static final String USER_NORMAL = "NORMAL";
	public static final String USER_LOCKED = "LOCKED";
	
	public static final String USER_VERIFY_VERIFIED = "UNVERIFY";
	public static final String USER_VERIFY_UNVERIFY = "UNVERIFY";
	
	public static final Integer USER_ISMEMBER_NO = 0;
	public static final Integer USER_ISMEMBER_YES = 1;
	
	//FDManagerInfo
	public static final Integer MANANGER_STATE_NORMAL = 0;
	public static final Integer MANANGER_STATE_LOCKED = 1;
	public static final Integer MANANGER_SUPERMANAGER_NO = 0;
	public static final Integer MANANGER_SUPERMANAGER_YES = 1;
	
	//FDMerchantInfo
	public static final Integer MERCHANT_STATUS_NORMAL = 0;
	public static final Integer MERCHANT_STATUS_LOCKED = 1;
	
	//FDOrder  已订购(ordered),已改价(priced)，已付款(payed)，已受理(accepted), 已取消(cancel)
	public static final String ORDER_STATUS_ORDERED = "ORDERED";
	public static final String ORDER_STATUS_PRICED = "PRICED";
	public static final String ORDER_STATUS_PAYED = "PAYED";
	public static final String ORDER_STATUS_ACCEPTED = "ACCEPTED";
	public static final String ORDER_STATUS_CANCEL = "CANCEL";
	
	public static final String ORDER_PAYWAY_BALANCE = "BALANCE";
	public static final String ORDER_PAYWAY_ALIPAY = "ALIPAY";

	public static final Integer ORDER_USERFRONTSTATE_SHOW = 0;
	public static final Integer ORDER_USERFRONTSTATE_HIDE = 1;
	
	public static final Integer ORDER_MERCHANTFRONTSTATE_SHOW = 0;
	public static final Integer ORDER_MERCHANTUSERFRONTSTATE_HIDE = 1;
	
	//FDResource
	public static final Integer RESOURCE_RESOURCETYPE_WX = 0;  			//微信公众号 0，微博 1，QQ公众号 2
	public static final Integer RESOURCE_RESOURCETYPE_WB = 1; 
	public static final Integer RESOURCE_RESOURCETYPE_QQ = 2;
	
	public static final Integer RESOURCE_CATEGORY_MINGREN = 0; 			//名人 0, 草根 1
	public static final Integer RESOURCE_CATEGORY_CAOGEN = 1; 		
	
	public static final Integer RESOURCE_STATUS_PASS = 0; 			//0 通过审核 1 未通过审核 2待审核
	public static final Integer RESOURCE_STATUS_UNPASS = 1; 
	public static final Integer RESOURCE_STATUS_WAITVERIFY = 2; 
	
	public static final Integer RESOURCE_ISSALE_ON = 0; 			//0上架 1下架
	public static final Integer RESOURCE_ISSALE_OFF = 1; 			
	
	public static final Integer RESOURCE_GENDER_MALE = 0;             //性别 男
	public static final Integer RESOURCE_GENDER_FEMALE = 1;           //性别 女

	//FDResourceSpe
	public static final String RESOURCESPE_RESOURCESPE_ATONE = "ATONE";            //多图头条，多图次条，第3-N条 等(投放位置)
	public static final String RESOURCESPE_RESOURCESPE_ATTWO = "ATTWO";
	public static final String RESOURCESPE_RESOURCESPE_ATTHREE = "ATTHREE";
	
	public static final String RESOURCESPE_RESOURCESPE_DIRECT = "DIRECT";			//直发报价，转发报价
	public static final String RESOURCESPE_RESOURCESPE_INDIRECT = "INDIRECT";		
	
	public static final Integer RESOURCESPE_SPECATEGORY_HARD = 0;            //硬广，软广
	public static final Integer RESOURCESPE_SPECATEGORY_SOFT = 1; 
	
	//FDUserRecharge
	public static final Integer USERRECHARGE_STATUS_WAITVERIFY = 0;   		//待审核0 ,  充值成功 1 ,充值失败 2
	public static final Integer USERRECHARGE_STATUS_SUCCESS = 1;
	public static final Integer USERRECHARGE_STATUS_FAILURE = 2;
	
	public static final String USERRECHARGE_RECHARGETYPE_AUTO = "AUTO";         //自动充值
	public static final String USERRECHARGE_RECHARGETYPE_MANUAL = "MANUAL";       //手动充值
	
	//FDApplyWithdraw
	public static final Integer APPLYWITHDRAW_APPLYSTATUS_WAITVERIFY = 0;   //(未审核 wait_verify，已通过 pass，未通过unpass,已完成 finished)
	public static final Integer APPLYWITHDRAW_APPLYSTATUS_PASS = 1; 
	public static final Integer APPLYWITHDRAW_APPLYSTATUS_UNPASS = 2; 
	public static final Integer APPLYWITHDRAW_APPLYSTATUS_FINISHED = 3; 
	
	//FDAppeal
	public static final Integer APPEAL_STATUS_WAITHANDLE = 0;   //待处理
	public static final Integer APPEAL_STATUS_HANDLED = 1;      //已处理
	
	//FDBalanceChange
	public static final String BALANCECHANGE_CHANGETYPE_RECHARGE = "RECHARGE";   		
	public static final String BALANCECHANGE_CHANGETYPE_ORDERPAY = "ORDERPAY";   		
	public static final String BALANCECHANGE_CHANGETYPE_ORDERCOMPLETE = "ORDERCOMPLETE";   		
	public static final String BALANCECHANGE_CHANGETYPE_ORDERCANCEL = "ORDERCANCEL";   		
	public static final String BALANCECHANGE_CHANGETYPE_WITHDRAW = "WITHDRAW";   
	
	//FDCarousel
	public static final Integer CAROUSEL_ISSHOW_SHOW = 0;   
	public static final Integer CAROUSEL_ISSHOW_HIDE = 1;   

	public static final String APPLYFERTILIZE_STATUS_APPLIED = "APPLIED";   
	public static final String APPLYFERTILIZE_STATUS_HANDLED = "HANDLED";   
	
	public static final String APPLYSOILTEST_STATUS_APPLIED = "APPLIED";   
	public static final String APPLYSOILTEST_STATUS_HANDLED = "HANDLED";   
	
	public static final String SYSTEMPARAM_DEFAULT_ID = "00000000";
	public static final String SYSTEMPARAM_ISCATCOINDEDUCTOPEN_YES = "YES";
	public static final String SYSTEMPARAM_ISCATCOINDEDUCTOPEN_NO = "NO";

	public static final String INTEGRALCHANGE_CHANGETYPE_INTEGRALTOCATCOIN = "INTEGRALTOCATCOIN";
	public static final String INTEGRALCHANGE_CHANGETYPE_DETAILADDRESS = "DETAILADDRESS";
	public static final String INTEGRALCHANGE_CHANGETYPE_MYCROPS = "MYCROPS";
	public static final String INTEGRALCHANGE_CHANGETYPE_TRUENAME = "TRUENAME";
	public static final String INTEGRALCHANGE_CHANGETYPE_GENDER = "GENDER";
	public static final String INTEGRALCHANGE_CHANGETYPE_SIGNIN = "SIGNIN";
	public static final String INTEGRALCHANGE_CHANGETYPE_PHOTOSPOTS = "PHOTOSPOTS";
	public static final String INTEGRALCHANGE_CHANGETYPE_ORDERCONSUMEADD = "ORDERCONSUMEADD";
	
	public static final String CATCOINCHANGE_CHANGETYPE_RECHARGECATCOIN = "RECHARGECATCOIN";
	public static final String CATCOINCHANGE_CHANGETYPE_CATCOINDEDUCT = "CATCOINDEDUCT";
	public static final String CATCOINCHANGE_CHANGETYPE_INTEGRALTOCATCOIN = "INTEGRALTOCATCOIN";
	public static final String CATCOINCHANGE_CHANGETYPE_DONATEFROM = "DONATEFROM";
	public static final String CATCOINCHANGE_CHANGETYPE_DONATETO = "DONATETO";

	public static final Integer EVALUATE_TYPE_USER = 0;
	public static final Integer EVALUATE_TYPE_ADMIN = 1;
	
	public static final String EVALUATE_ISSHOW_YES = "YES";
	public static final String EVALUATE_ISSHOW_NO = "NO";
	
	public static final String ADV_STATUS_ON = "ON";
	public static final String ADV_STATUS_OFF = "OFF";

	public static final String ARTICLE_STATUS_ON = "ON";
	public static final String ARTICLE_STATUS_OFF = "OFF";

	public static final String GOODS_STATUS_ON = "ON";
	public static final String GOODS_STATUS_OFF = "OFF";
	public static final String GOODS_STATUS_DELETED = "DELETED";
	
	public static final String CLASSREQUEST_STATUS_WAITHANDLE = "WAITHANDLE";
	public static final String CLASSREQUEST_STATUS_PASS = "PASS";
	public static final String CLASSREQUEST_STATUS_REJECT = "REJECT";
	
	public static final Integer FORUMPOST_ISSHOW_YES = 0;
	public static final Integer FORUMPOST_ISSHOW_NO = 1;
	
	public static final Integer FORUMCOMMENT_ISSHOW_YES = 0;
	public static final Integer FORUMCOMMENT_ISSHOW_NO = 1;
	
	public static final String MOBACCOUNT_TYPE_SALE = "SALE";
	public static final String MOBACCOUNT_TYPE_ORDER = "ORDER";
	public static final String MOBACCOUNT_TYPE_SOILTEST = "SOILTEST";
	public static final String MOBACCOUNT_TYPE_FERTILIZE = "FERTILIZE";
	public static final String MOBACCOUNT_TYPE_NORMAL = "NORMAL";
	public static final String MOBACCOUNT_TYPE_CHATROOM = "CHATROOM";
	
	public static final String NOTICE_TYPE_SYSTEM = "SYSTEM";
	public static final String NOTICE_TYPE_FORUMCOMMENT = "FORUMCOMMENT";
	
	public static final String NOTICE_STATUS_UNREAD = "UNREAD";
	public static final String NOTICE_STATUS_READED = "READED";
	
	public static final String OPERATELOG_OPERATETYPE_USER = "USER";
	public static final String OPERATELOG_OPERATETYPE_SERVE = "SERVE";
	public static final String OPERATELOG_OPERATETYPE_GOODS = "GOODS";
	public static final String OPERATELOG_OPERATETYPE_ORDER = "ORDER";
	public static final String OPERATELOG_OPERATETYPE_GAME = "GAME";
	public static final String OPERATELOG_OPERATETYPE_SOCIAL = "SOCIAL";
	public static final String OPERATELOG_OPERATETYPE_MOBACCOUNT = "MOBACCOUNT";
	public static final String OPERATELOG_OPERATETYPE_SYSTEM = "SYSTEM";
	public static final String OPERATELOG_OPERATETYPE_LOGIN = "LOGIN";
	
}
