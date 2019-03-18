package com.zkys.operationtool.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.zkys.operationtool.R;
import com.zkys.operationtool.application.MyApplication;
import com.zkys.operationtool.base.BaseActivity;
import com.zkys.operationtool.base.HttpResponse;
import com.zkys.operationtool.baseImpl.BasePresenter;
import com.zkys.operationtool.ui.dialog.SimpleDialogFragment;
import com.zkys.operationtool.util.ActivityManager;
import com.zkys.operationtool.util.JpushUtil;
import com.zkys.operationtool.util.NotificationsUtils;
import com.zkys.operationtool.util.ToastUtil;
import com.zkys.operationtool.util.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主菜单页面
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_binder_desc)
    TextView tvBinderDesc;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    private boolean isBackPressed;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImmersionBar.titleBar(R.id.ll_root)
                .init();
        tvName.setText(UIUtils.getRegards() + "-" + MyApplication.getInstance().getUserInfo().getName());
        tvBinderDesc.setText(tvBinderDesc.getText().toString().replace("0", MyApplication.getInstance().getUserInfo().getDeviceActiveCount() + ""));
        JpushUtil.setJPush();// 设置极光推送
        checkNotificationIsOpen();

        /** 新版本 **/
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制更新
                .setUserCanRetry(false)         //失败后是否提示重新下载
                .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk
                .register();
        try {
            String versionName = "版本：" + getPackageManager().getPackageInfo(getPackageName(),0).versionName;
            tvVersionName.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测应用消息通知是否开启
     */
    private void checkNotificationIsOpen() {
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            // 未开启的情况，提示开启
            new SimpleDialogFragment().show("温馨提示", "您的消息通知未开启，将不能及时收到消息推送", "开启", "下次开启", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermission(0b00111);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, getSupportFragmentManager());
        }
    }

    // 进入系统设置界面
    protected void requestPermission(int requestCode) {
        // TODO Auto-generated method stub
        // 6.0以上系统才可以判断权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivityForResult(intent, requestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_home;
    }

    @OnClick({R.id.iv_active_plate, R.id.iv_replace_device, R.id.iv_check_order, R.id.iv_plate_status,
            R.id.iv_volume_control, R.id.iv_free_time, R.id.iv_binder_bar_code,
            R.id.iv_inspection_feedback, R.id.iv_info_commit, R.id.iv_login_out, R.id.iv_alert_info,
            R.id.iv_question_answer, R.id.iv_team_audit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_active_plate:
                startActivity(new Intent(this, ActivePlateActivity.class));// 激活设备
                break;
            case R.id.iv_replace_device:
                startActivity(new Intent(this, ReplaceDeviceActivity.class));// 更换设备
                break;
            case R.id.iv_check_order:
                startActivity(new Intent(this, CheckOrderActivity.class));// 查看订单
                break;
            case R.id.iv_plate_status:
                startActivity(new Intent(this, PlateStatusActivity.class));// 设备状态
                break;
            case R.id.iv_volume_control:
                startActivity(new Intent(this, VolumeControlActivity.class));// 音量控制
                break;
            case R.id.iv_free_time:
                startActivity(new Intent(this, ScanOnTimeActivity.class));// 免费时长（隐藏）
                break;
            case R.id.iv_binder_bar_code:
                startActivity(new Intent(this, BinderBarCodeActivity.class));// 绑定条形码
                break;
            case R.id.iv_inspection_feedback:
                startActivity(new Intent(this, InspectionFeedbackActivity.class));// 巡检反馈（隐藏）
                break;
            case R.id.iv_info_commit:
                startActivity(new Intent(this, InfoCommitActivity.class));// 信息提交（隐藏）
                break;
            case R.id.iv_login_out:// 退出登录

                new SimpleDialogFragment().show("", "是否退出登录", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyApplication.getInstance().clearUserInfo();
                        MyApplication.getInstance().restartApplication();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, getSupportFragmentManager());

                break;
            case R.id.iv_alert_info:// 警报信息
                startActivity(new Intent(this, AlertInfoActivity.class));
                break;
            case R.id.iv_question_answer:// 疑问解答
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra("url", "http://test.hp.zgzkys.com/#/answer"));
                break;
            case R.id.iv_team_audit:
                startActivity(new Intent(this, TeamAuditActivity.class));
                break;
        }
    }

    @Override
    public void setData(HttpResponse result) {

    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            ActivityManager.getAppInstance().finishActivity(LoginActivity.class);
            ActivityManager.getAppInstance().finishAllActivity();
            return;
        }
        isBackPressed = true;
        ToastUtil.showShort("再按一次退出程序");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }
}
