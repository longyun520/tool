package com.zkys.operationtool.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.crash.PgyerCrashObservable;
import com.pgyersdk.crash.PgyerObserver;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zkys.operationtool.bean.UserInfoBean;
import com.zkys.operationtool.canstant.SharedConstant;

import org.json.JSONException;

import cn.jpush.android.api.JPushInterface;

/**
 * Desc:
 * Author:
 * Date: 2018/4/18
 * Copyright (c) 2016, dianlibian.com All Rights Reserved
 */

public class MyApplication extends Application {

    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wx96eeba77767289aa";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI mWxApi;

    public IWXAPI getmWxApi() {
        return mWxApi;
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return (MyApplication) getContext();
    }

    public static Context context;

    public SharedPreferences mainPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mainPreferences = getSharedPreferences(SharedConstant.MAIN_PREFERENCE, MODE_PRIVATE);
        registToWX();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // 蒲公英自動更新SDK初始化註冊
        PgyCrashManager.register();
        PgyerCrashObservable.get().attach(new PgyerObserver() {
            @Override
            public void receivedCrash(Thread thread, Throwable throwable) {
                Log.e("MyApplication", throwable.getMessage());
            }
        });

    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(APP_ID);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private UserInfoBean userInfo;

    public UserInfoBean getUserInfo() {
        String jsonStr = mainPreferences.getString(SharedConstant.USERINFO, "");
        if (userInfo == null) {
            UserInfoBean userInfo = new Gson().fromJson(jsonStr, UserInfoBean.class);
            this.userInfo = userInfo;
            setUserInfo(userInfo);// 设置现有的数据模型
        }
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }


    public static SharedPreferences getMainPreferences() {
        return getInstance().mainPreferences;
    }

    /**
     * 存储用户数据到本地
     * @throws JSONException
     */
    public void saveUserInfo(UserInfoBean userInfo) {
        if (userInfo != null) {
//            UserInfo info = new UserInfo();
//            info.setToken(loginResult.getToken());
//            info.setAvatarUrl(loginResult.getAvatarUrl());
//            info.setId(loginResult.getId());
//            info.setName(loginResult.getName());
//            info.setUsername(loginResult.getUsername());
//            info.setTag(loginResult.getTag());
//            info.setRoleInfo(loginResult.getRoleInfo());
            String json = new Gson().toJson(userInfo);
            mainPreferences.edit().clear().apply();
            mainPreferences.edit().putString(SharedConstant.USERINFO, json).apply();
            setUserInfo(userInfo);
        } else {
            Log.d(this.getClass().getSimpleName(),"用户数据保存失败！");
        }
    }


    public boolean isLogin() {
        userInfo = getUserInfo();
        if (userInfo == null) {
            return false;
        }
        if (!TextUtils.isEmpty(userInfo.getName()) && userInfo.getId() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 清除用户数据
     */
    public void clearUserInfo() {
        mainPreferences.edit().clear().apply();
        setUserInfo(null);
    }

    public  void restartApplication() {
        final Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(MyApplication.getContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyApplication.getContext().startActivity(intent);
    }


}
