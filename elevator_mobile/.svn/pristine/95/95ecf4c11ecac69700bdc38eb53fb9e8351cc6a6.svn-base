
package com.chinacnit.elevatorguard.mobile.api;

import com.chinacnit.elevatorguard.core.http.HttpApiAdatper;
import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.bean.CompanyListDetail.CompanyType;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequest;
import com.chinacnit.elevatorguard.mobile.http.request.params.BindTagParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.CheckVersionIsUpdateParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.CompanyDetailParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.CompanyListParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.DownloadNewVersionApkParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.LiftDetailsParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.LiftListParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.LoginParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.MaintenanceTaskListParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.QueryMaintenanceTaskIsStartParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.SettingTagListParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.StartMaintenanceTaskParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.SubmitMaintenanceItemParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.SubmitMaintenancePlanParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.VerificationParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.WeiBaoDetailParams;
import com.chinacnit.elevatorguard.mobile.http.request.params.WeiBaoRecordParams;

public class HttpApi extends HttpApiAdatper {
    public static final String BASE_URL_LOGIN = "http://app.cnitcloud.com";
    public static final String BASE_URL_LIFT = "http://lift.cnitcloud.com";
//      public static final String BASE_URL_LOGIN = "http://10.255.8.133:8084";  //wz
//      public static final String BASE_URL_LIFT = "http://10.255.8.133:8085";  // normal  测试用
//      public static final String BASE_URL_LIFT = "http://10.10.0.6:8080/lift";  //special 
//      public static final String BASE_URL_LIFT = "http://10.10.11.135:8085/lift"; //wz
    
    public HttpApi(ElevatorGuardApplication mApplication) {
        super(mApplication);
    }
    
    /**
     * 登录成功以后每次请求数据都要设置验证参数
     * @param
     * @author: ssu
     * @date: 2015-6-4 下午4:22:01
     */
    private VerificationParams getVerificationParams() {
    	LoginDetail loginDetail = ConfigSettings.getLoginDetail();
    	VerificationParams param1 = null;
    	if (null != loginDetail) {
    		param1 = new VerificationParams(loginDetail.getUid(), loginDetail.getSid());
    	}
    	return param1;
    }

   /**
    * 用户登录
    * @param
    * @author: ssu
    * @date: 2015-6-2 下午5:26:18
    */
    public String userLogin(String userName, String userPwd, String mac) throws HttpApiException, NetworkUnavailableException {
        String url = BASE_URL_LOGIN + "/pub/regUserController/appLogin/";
        LoginParams loginParams = new LoginParams(userName, userPwd, mac);
        HttpBaseRequest baseParams = new HttpBaseRequest(null, loginParams);
        return getWithString(url, baseParams.getHttpRequestParams());
    }
    
    /**
     * 获取用户所属公司类型
     * @param
     * @author: ssu
     * 
     * @date: 2015-6-4 下午4:16:26
     */
    public String getUserCompanyType(LoginDetail loginDetail) throws HttpApiException, NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findRoleByCompanyId";
    	VerificationParams param1 = new VerificationParams(loginDetail.getUid(), loginDetail.getSid());
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, null);
    	return getWithString(url, baseParams.getHttpRequestParams());
    }
    
    /**
     * 公司列表查询
     * @param
     * @author: ssu
     * @date: 2015-6-2 下午5:26:15
     */
    public String getCompanyList(CompanyType companyTypeint, int page, int rows) throws HttpApiException, NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findCompanyList";
    	VerificationParams param1 = getVerificationParams();
    	CompanyListParams param2 = new CompanyListParams(page, rows, companyTypeint);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url, baseParams.getHttpRequestParams());
    }
    
    /**
     * 公司详情查询
     * @param
     * @author: ssu
     * @date: 2015-6-5 下午8:43:06
     */
    public String getCompanyDetail(long companyId, int companyType) throws HttpApiException, NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findCompanyInfoByCompanyId";
    	VerificationParams param1 = getVerificationParams();
    	CompanyDetailParams param2 = new CompanyDetailParams(companyId, companyType);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url, baseParams.getHttpRequestParams());
    }
    
    /**
     * 电梯列表
     * 
     * @author: pyyang
     * @date 创建时间：2015年6月5日 上午9:45:21
     */
    public String getLiftList(int page, int rows) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftList/";
    	VerificationParams param1 = getVerificationParams();
    	LiftListParams param2 = new LiftListParams(page, rows);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 电梯列表 ,带模糊查询
     * 
     * @author: pyyang
     * @date 创建时间：2015年6月5日 上午9:45:21
     */
    public String getLiftList(String liftName, String locationAddress, int floor,
			String liftTrademark, String liftType,int page, int rows) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftList/";
    	VerificationParams param1 = getVerificationParams();
    	LiftListParams param2 = new LiftListParams(liftName, locationAddress,floor,liftTrademark,liftType,page, rows);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 维保记录（维保详情列表）
     * 
     * @author: pyyang
     * @date 创建时间：2015年6月11日 上午10:43:37
     */
    public String getWeiBaoRecord(long liftId, int page, int rows, String signDate, String order, String oderType) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftMaintainPlanByLiftId/";
    	VerificationParams param1 = getVerificationParams();
    	WeiBaoRecordParams param2 = new WeiBaoRecordParams(liftId, page, rows, signDate, order, oderType);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url, baseParams.getHttpRequestParams());
    }
    
    /**
     * 维保记录（维保详情列表）,带根据日期模糊查询
     * 
     * @author: pyyang
     * @date 创建时间：2015年6月11日 上午10:43:37
     */
    public String getWeiBaoRecord(String condition, long liftId, int page, int rows, String signDate, String order, String oderType) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftMaintainPlanByLiftId/";
    	VerificationParams param1 = getVerificationParams();
    	WeiBaoRecordParams param2 = new WeiBaoRecordParams(condition, liftId, page, rows, signDate, order, oderType);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url, baseParams.getHttpRequestParams());
    }
    
    
    /**
     * 获得设置标签列表
     * @param
     * @author: ssu
     * @date: 2015-6-8 下午8:29:37
     */
    public String getSettingsTagList(long liftId, int page, int rows) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftMaintainContents";
    	VerificationParams param1 = getVerificationParams();
    	SettingTagListParams param2 = new SettingTagListParams("LIFTMAINTAIN", liftId, page, rows);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 绑定标签
     * @param 
     * @author: ssu
     * @date: 2015-6-11 上午10:46:49
     */
    public String bindTag(long liftId, String keyValue, String tagInfo, String cover) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/bindTagWithMaintainContent";
    	VerificationParams param1 = getVerificationParams();
    	BindTagParams param2 = new BindTagParams(liftId, keyValue, tagInfo, cover);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 获得维保工维保任务列表
     * @param
     * @author: ssu
     * @date: 2015-6-11 下午8:36:00
     */
    public String getMaintenanceTaskList(int page, int rows, String signDate, String order, String orderType) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/queryLiftMaintainTask";
    	VerificationParams param1 = getVerificationParams();
    	MaintenanceTaskListParams param2 = new MaintenanceTaskListParams(page, rows, signDate, order, orderType);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 获得维保工维保任务列表,根据维保开始时间查询
     * @param
     * @author: ssu
     * @date: 2015-6-11 下午8:36:00
     */
    public String getMaintenanceTaskList(String condition, int page, int rows, String signDate, String order, String orderType) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/queryLiftMaintainTask";
    	VerificationParams param1 = getVerificationParams();
    	MaintenanceTaskListParams param2 = new MaintenanceTaskListParams(condition, page, rows, signDate, order, orderType);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 获取电梯详情
     * 
     * @author: pyyang
     * @date 创建时间：2015年6月11日 下午6:10:34
     */
    public String getLiftDetails(long liftId) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftBaseInfoByLiftId";
    	VerificationParams param1 = getVerificationParams();
    	LiftDetailsParams param2 = new LiftDetailsParams(liftId);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 获得维保详情
     * @param
     * @author: ssu
     * @date: 2015-6-15 上午11:05:50
     */
    public String getWeiBaoDetail(long planId) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/findLiftMaintainByPlanId";
    	VerificationParams param1 = getVerificationParams();
    	WeiBaoDetailParams param2 = new WeiBaoDetailParams(planId);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 查询维保任务是否开始
     * @param
     * @author: ssu
     * @date: 2015-6-17 下午7:36:56
     */
    public String queryMaintenanceTaskIsStart(long planId) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/isPlanStarted";
    	VerificationParams param1 = getVerificationParams();
    	QueryMaintenanceTaskIsStartParams param2 = new QueryMaintenanceTaskIsStartParams(planId);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 查询某个 planid下的维保项  bean 集合。。。
     * @param
     * @author: joychine
     * @date: 2015年9月2日 16:31:25
     */
    public String queryMaintenanceBean(long planId) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/startLiftMaintainPlan";
    	VerificationParams param1 = getVerificationParams();
    	QueryMaintenanceTaskIsStartParams param2 = new QueryMaintenanceTaskIsStartParams(planId);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    
   /**
    * 维保任务开始
    * @param
    * @author: ssu
    * @date: 2015-6-18 上午10:46:16
    */
    public String startMaintenanceTask(long planId) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/startLiftMaintainPlan";
    	VerificationParams param1 = getVerificationParams();
    	StartMaintenanceTaskParams param2 = new StartMaintenanceTaskParams(planId);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 提交维保项
     * @param
     * @author: ssu
     * @date: 2015-6-18 下午8:13:18
     */
    public String submitMaintenanceItem(long planId, String tagId, String mobile, String comments, WeiBaoItemStatus statusCode) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/commitMaintainContent";
    	VerificationParams param1 = getVerificationParams();
    	SubmitMaintenanceItemParams param2 = new SubmitMaintenanceItemParams(planId, tagId, mobile, comments, statusCode);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 提交维保计划
     * @param
     * @author: ssu
     * @date: 2015-6-19 下午2:03:33
     */
    public String submitMaintenancePlan(long planId) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/finishLiftMaintainPlan";
    	VerificationParams param1 = getVerificationParams();
    	SubmitMaintenancePlanParams param2 = new SubmitMaintenancePlanParams(planId);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 检查版本是否需要更新
     * @param
     * @author: ssu
     * @date: 2015-6-30 下午2:18:29
     */
    public String checkVersionIsUpdate(long type) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/getLatestVersionInfo";
    	VerificationParams param1 = getVerificationParams();
    	CheckVersionIsUpdateParams param2 = new CheckVersionIsUpdateParams(type);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return getWithString(url,baseParams.getHttpRequestParams());
    }
    
    /**
     * 下载最新的apk
     * @param
     * @author: ssu
     * @date: 2015-7-1 上午11:50:45
     */
    public String downloadNewVersionApk(long type) throws HttpApiException,NetworkUnavailableException {
    	String url = BASE_URL_LIFT + "/apps/biz/liftMaintainContentController/downloadLatestApp";
    	VerificationParams param1 = getVerificationParams();
    	DownloadNewVersionApkParams param2 = new DownloadNewVersionApkParams(type);
    	HttpBaseRequest baseParams = new HttpBaseRequest(param1, param2);
    	return genrateGetUrl(url,baseParams.getHttpRequestParams());
    }
    
}
