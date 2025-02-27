package com.zkys.operationtool.retrofitinterface;

import com.google.gson.JsonObject;
import com.zkys.operationtool.base.HttpResponse;
import com.zkys.operationtool.bean.AlertBean;
import com.zkys.operationtool.bean.AuditItemBean;
import com.zkys.operationtool.bean.BedBean;
import com.zkys.operationtool.bean.BedOrderStateBean;
import com.zkys.operationtool.bean.CoreBean;
import com.zkys.operationtool.bean.DeviceBinderInfo;
import com.zkys.operationtool.bean.DeviceInfoBean;
import com.zkys.operationtool.bean.HomeListBean;
import com.zkys.operationtool.bean.HospitalBean;
import com.zkys.operationtool.bean.ItemStatisticBean;
import com.zkys.operationtool.bean.ItemUsageRatesBean;
import com.zkys.operationtool.bean.LockInfo;
import com.zkys.operationtool.bean.OrderDataBean;
import com.zkys.operationtool.bean.OrderTailBean;
import com.zkys.operationtool.bean.OverDueBean;
import com.zkys.operationtool.bean.RepairListBean;
import com.zkys.operationtool.bean.UnLockInfo;
import com.zkys.operationtool.bean.UserInfoBean;
import com.zkys.operationtool.bean.VolumeInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Desc:
 * Date: 2018/4/18
 * Copyright (c) 2016, dianlibian.com All Rights Reserved
 */

public interface RetrofitInterface {

    /**
     * @return
     * @Body 标签不能同时和@FormUrlEncoded、Multipart标签使用了 否则爆红
     */

    // 账号登陆
    @POST("/yw/admin/login")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoBean>> login(@Field("mobile") String username, @Field
            ("password") String password);

    // 账号登陆新接口
    @POST("/sys/login")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoBean>> newlogin(@FieldMap Map<String, Object> map);

    // 微信登陆
    @POST("/yw/admin/wxLogin")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoBean>> wechatLogin(@Field("code") String code);

    // 微信登陆
    @POST("/sys/thirdLogin")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoBean>> thirdLogin(@Field("code") String code, @Field("type")
            int type);

    // 医院列表
    @FormUrlEncoded
    @POST("/yw/hospital/list")
    Observable<HttpResponse<List<HospitalBean>>> getHospitalList(@Field("sydicId") String
                                                                         sydicId, @Field("state")
            int state);

    // 医院列表(新接口)
    @FormUrlEncoded
    @POST("/yw/hospital/list")
    Observable<HttpResponse<List<HospitalBean>>> getnewHospitalList(@Field("state") int state);

    // 科室列表
    @POST("/yw/hospital/deptList")
    @FormUrlEncoded
    Observable<HttpResponse<List<CoreBean>>> getCoreList(@Field("hospitalId") int hid, @Field
            ("state") int state);

    // 账号绑定
    @POST("/yw/admin/bindingOpenId")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoBean>> bindingAccount(@Field("openId") String openid, @Field
            ("mobile") String mobile, @Field("password") String password);

    // 激活设备
//    @POST("core/device/activate")
    @POST("/yw/deviceBinding/newActivate")
    Observable<HttpResponse<Object>> activatePlate(@Body Map<String, Object> map);

    // 床位列表

    //    @POST("hospitalBed/list")//@Field("hospitalId") int hospitalId, @Field("deptId") int
    // deptId
    @POST("/yw/deviceBinding/list")
//@Field("hospitalId") int hospitalId, @Field("deptId") int deptId
    @FormUrlEncoded
    Observable<HttpResponse<List<BedBean>>> getBedList(@FieldMap Map<String, Integer> map);


    // 根据编号获取设备绑定信息
    @POST("/yw/deviceBinding/findBindingByCode")
    @FormUrlEncoded
    Observable<HttpResponse<DeviceBinderInfo>> findBindingByCode(@Field("code") String code);

    // 更换设备
    @POST("/yw/deviceBinding/replacingDevice")
    Observable<HttpResponse<Object>> replaceDevice(@Body Map<String, Object> map);

    // 根据科室医院ID获取科室平板原有的音量配置
    @POST("/padVolume/list")
    @FormUrlEncoded
    Observable<HttpResponse<List<VolumeInfoBean>>> getPadVolume(@Field("hospitalId") int
                                                                        hospitalId, @Field
            ("deptId") int deptId);


    // 控制修改科室平板原有的音量配置
    @POST("padVolume/insert")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> controlPadVolume(@Field("hospitalId") int hospitalId, @Field
            ("deptId") int deptId, @Field("volume") int volume, @Field("id") int vId);


    // 訂單列表
    @POST("/yw/wxOrder/list")
    @FormUrlEncoded
    Observable<HttpResponse<OrderDataBean>> getOderData(@FieldMap Map<String, Object> map);

    // 获取统计图表数据
    @POST("/yw/wxOrder/wxOrderStatistics")
    @FormUrlEncoded
    Observable<HttpResponse<List<ItemStatisticBean>>> getOrderStatistics(@FieldMap Map<String,
            Object> map);

    // 获取团队审核
    @GET("/yw/teamAuthenticate/findAllUnaudited")
    Observable<HttpResponse<List<AuditItemBean>>> getTeamAuditData();

    // 审核团队认证申请
    @GET("/yw/teamAuthenticate/audit")
    Observable<HttpResponse<Object>> audit(@Query("teamId") int teamId, @Query("state") int state);

    // 所有医院的平板使用率
    @POST("/yw/padStatus/allPadUsageRates")
    @FormUrlEncoded
    Observable<HttpResponse<List<ItemUsageRatesBean>>> getAllPadUsageRates(@FieldMap Map<String,
            Object> map);

    // 指定医院所有科室的平板使用率
    @POST("/yw/padStatus/allDeptPadUsageRates")
    @FormUrlEncoded
    Observable<HttpResponse<List<ItemUsageRatesBean>>> getAllDeptPadUsageRates(@FieldMap
                                                                                       Map<String, Object> map);

    // 平板订单状态状态
    @GET("/yw/padStatus/padOrderStatus")
    Observable<HttpResponse<List<BedOrderStateBean>>> getPadOrderStatusData(@QueryMap Map<String,
            Object> map);

    // 设备详细信息
    @GET("/yw/padStatus/deviceDetailInfo")
    Observable<HttpResponse<DeviceInfoBean>> getDeviceDetailInfo(@QueryMap Map<String, Object> map);

    // 修改床位号
    @POST("/yw/deviceBinding/update")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> updateBedNumber(@FieldMap Map<String, Object> map);

    // 开锁
    @POST("lock/unlock")
    @FormUrlEncoded
    Observable<HttpResponse<UnLockInfo>> unlock(@FieldMap Map<String, Object> map);

    // 查询锁开关状态
    @POST("lock/findByDid")
    @FormUrlEncoded
    Observable<HttpResponse<LockInfo>> getLockInfo(@FieldMap Map<String, Object> map);

    // 查询锁开关状态
    @GET("/yw/alarm/list")
    Observable<HttpResponse<List<AlertBean>>> getAlertInfo();

    // 获取首页列表数据
    @GET("/yw/admin/authority")
    Observable<HttpResponse<List<HomeListBean>>> getAuthority();

    // 账号绑定
    @POST("/sys/loginOrBind")
    @FormUrlEncoded
    Observable<HttpResponse<UserInfoBean>> bindingWechat(@Field("openId") String openid, @Field
            ("mobile") String mobile, @Field("captcha") String password, @Field("type") int type);

    // 获取逾期订单数量
    @GET("yw/wxOrder/getExpireTotal")
    Observable<HttpResponse<Object>> getExpireTotal();

    // 获取警报信息订单数量
    @GET("/yw/alarm/getTotal")
    Observable<HttpResponse<Object>> getAlarmTotal();

    // 获取逾期订单
    @POST("/yw/wxOrder/list")
    @FormUrlEncoded
    Observable<HttpResponse<OverDueBean>> wxOrderList(@FieldMap Map<String, Object> map);

    // 生产工具绑定设备
    @POST("/lockDid/batch")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> bindLock(@FieldMap Map<String, Object> map);

    // 生产工具解绑设备
    @POST("/lockDid/delete")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> unbindLock(@FieldMap Map<String, Object> map);


    // 第三方解锁
    @POST("/api")
    Observable<HttpResponse<Object>> thridunLock(@Body JsonObject obj);

    // 根据订单查询订单详情
    @POST("/yw/wxOrder/findById")
    @FormUrlEncoded
    Observable<HttpResponse<OrderTailBean>> findById(@FieldMap Map<String, Object> map);

    // 订单退款
    @POST("/yw/wxOrder/refund")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> refund(@FieldMap Map<String, Object> map);


    // 维修列表
    @POST("/repair/list")
    @FormUrlEncoded
    Observable<HttpResponse<List<RepairListBean>>> repairList(@FieldMap Map<String, Object> map);


    // 更新报修事件
    @POST("/repair/update")
    @FormUrlEncoded
    Observable<HttpResponse<Object>> repairUpdate(@FieldMap Map<String, Object> map);
}
