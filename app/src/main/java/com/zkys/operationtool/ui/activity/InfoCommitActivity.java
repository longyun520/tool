package com.zkys.operationtool.ui.activity;

import android.os.Bundle;

import com.zkys.operationtool.R;
import com.zkys.operationtool.base.BaseActivityOld;
import com.zkys.operationtool.base.HttpResponseOld;
import com.zkys.operationtool.baseImpl.BasePresenter;

/**
 * 信息提交
 */
public class InfoCommitActivity extends BaseActivityOld {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTvTitleText("信息提交");
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_info_commit;
    }

    @Override
    protected int getTitleViewType() {
        return BaseActivityOld.DEFAULT_TITLE_VIEW;
    }

    @Override
    public void setData(HttpResponseOld result) {

    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
