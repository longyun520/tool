package com.zkys.operationtool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zkys.operationtool.R;
import com.zkys.operationtool.adapter.UsageRatesAdapter;
import com.zkys.operationtool.base.BaseActivity;
import com.zkys.operationtool.base.HttpResponse;
import com.zkys.operationtool.bean.ItemUsageRatesBean;
import com.zkys.operationtool.presenter.PlateStatusPresenter;
import com.zkys.operationtool.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 平板状态界面
 */
public class PlateStatusActivity extends BaseActivity<PlateStatusPresenter> {

    @BindView(R.id.rcv_list)
    RecyclerView rcvList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_network_exception)
    RelativeLayout rlNetworkException;
    private List<ItemUsageRatesBean> usageRatesBeans = new ArrayList<>();
    private List<ItemUsageRatesBean> tempusageRatesBeans = new ArrayList<>();
    private int pageNum = 0;
    private UsageRatesAdapter usageRatesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTvTitleText("平板状态");
        usageRatesAdapter = new UsageRatesAdapter(usageRatesBeans);
        presenter.getAllPadUsageRates(getStringObjectMap(pageNum));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 0;
                presenter.getAllPadUsageRates(getStringObjectMap(pageNum));
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                presenter.getAllPadUsageRates(getStringObjectMap(pageNum));
            }
        });
    }

    @NonNull
    private Map<String, Object> getStringObjectMap(int pageNum) {
        final Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        return map;
    }


    @Override
    public PlateStatusPresenter initPresenter() {
        return new PlateStatusPresenter(this);
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_plate_status;
    }

    @Override
    protected int getTitleViewType() {
        return BaseActivity.DEFAULT_TITLE_VIEW;
    }

    @Override
    public void setData(HttpResponse result) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (result != null && result.getCode() == 200) {
            if (result.getData() instanceof List) {
                if (pageNum == 0) {
                    if(tempusageRatesBeans!=null){
                        tempusageRatesBeans.clear();
                    }
                    usageRatesBeans = (List<ItemUsageRatesBean>) result.getData();
                    if (usageRatesBeans.size() < 15) {
                        refreshLayout.setEnableLoadMore(false);
                    } else {
                        refreshLayout.setEnableLoadMore(true);
                    }
                } else {
                    tempusageRatesBeans = (List<ItemUsageRatesBean>) result.getData();
                    if (tempusageRatesBeans.size() < 15) {
                        refreshLayout.setEnableLoadMore(false);
                    } else {
                        refreshLayout.setEnableLoadMore(true);
                    }
                    usageRatesBeans.addAll(tempusageRatesBeans);
                }

                usageRatesAdapter.setNewData(usageRatesBeans);
                rcvList.setAdapter(usageRatesAdapter);
                initData();
            } else {
                ToastUtil.showShort(result.getMsg());
            }
        } else {
            ToastUtil.showShort("数据获取失败");
        }

    }

    @Override
    public void onError_(Throwable e) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    private void initData() {
        usageRatesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(PlateStatusActivity.this, PlateCoreListActivity.class)
                        .putExtra("hid", usageRatesBeans.get(position).getHospitalId())
                        .putExtra("HospitalName", usageRatesBeans.get(position).getHospitalName()));
            }
        });
    }

}
