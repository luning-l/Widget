package com.eryanet.widget;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.helper.base.BaseActivity;
import com.helper.utils.BarUtils;
import com.helper.utils.ColorUtils;
import com.helper.utils.ToastUtils;
import com.helper.widgets.brvah.BaseQuickAdapter;
import com.helper.widgets.brvah.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public void setBar(boolean isLightMode, @ColorInt int color, boolean isDecor) {
        BarUtils.setStatusBarLightMode(this, isLightMode);
        View fakeStatusBar = findViewById(R.id.statusBarCustomFakeStatusBar);
        fakeStatusBar.setBackgroundColor(color);
        BarUtils.setStatusBarCustom(fakeStatusBar, isDecor);
    }

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    private BaseQuickAdapter baseQuickAdapter;
    private List<String> list;

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        ButterKnife.bind(this);
        setBar(true, ColorUtils.getRandomColor(), false);
    }

    @Override
    public void doBusiness() {
        list = new ArrayList<>();
        initRecyclerView();

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            strings.add(String.valueOf(i));
        }
        updateRecycleView(strings);
    }

    private void initRecyclerView() {
//        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main_rv, list) {
            @Override
            protected void convert(@NonNull BaseViewHolder holder, String item) {
                holder.setText(R.id.tv_text, item);
            }
        };
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> ToastUtils.showShort((String) adapter.getItem(position)));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.main_rv_decoration));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
//        mRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mRecyclerView.setAdapter(baseQuickAdapter);
    }

    private void updateRecycleView(List<String> list) {
        if (list == null || list.isEmpty()) return;
        this.list.clear();
        this.list.addAll(list);
        baseQuickAdapter.notifyDataSetChanged();
    }
}
