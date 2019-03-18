package com.zkys.operationtool.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkys.operationtool.R;
import com.zkys.operationtool.base.BaseActivity;
import com.zkys.operationtool.base.HttpResponse;
import com.zkys.operationtool.bean.UserInfoBean;
import com.zkys.operationtool.presenter.LoginPresenterOld;
import com.zkys.operationtool.util.ToastUtil;
import com.zkys.operationtool.widget.AfterTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定账号
 */
public class BinderAccountActivity extends BaseActivity<LoginPresenterOld> {

    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    private UserInfoBean loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etAccount.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        loginResult = getIntent().getParcelableExtra("LoginResult");
    }

    @Override
    public LoginPresenterOld initPresenter() {
        return new LoginPresenterOld(this);
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_binder_account;
    }

    @Override
    protected int getTitleViewType() {
        return BaseActivity.DEFAULT_TITLE_VIEW;
    }

    @Override
    public void setData(HttpResponse result) {

        if (result.getCode() == 200) {
            ToastUtil.showShort("绑定成功");
            // @TODO 暂时去掉 xq
//                MyApplication.getInstance().saveUserInfo((LoginResult) result.getData());
//                startActivity(new Intent(this, HomeActivity.class));
//                ActivityManager.getAppInstance().finishActivity(LoginActivity.class);
                finish();
        } else {
            ToastUtil.showShort("" + result.getMsg());
        }
    }


    @OnClick({R.id.iv_delete, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                etAccount.setText("");
                break;
            case R.id.tv_confirm:
                String account = etAccount.getText().toString().trim().replace(" ", "");
                String password = etPassword.getText().toString().trim().replace(" ", "");
                presenter.bindingAccount(loginResult.getOpenId(),
                        account, password);
                break;
        }
    }

    class MyTextWatcher extends AfterTextWatcher {

        @Override
        public void afterTextChanged(Editable editable) {
            String account = etAccount.getText().toString().trim().replace(" ", "");
            String password = etPassword.getText().toString().trim().replace(" ", "");
            tvConfirm.setEnabled(account.length() == 11 && password.length() >= 6);
            ivDelete.setVisibility(account.length() > 0? View.VISIBLE : View.INVISIBLE);
        }
    }
}
