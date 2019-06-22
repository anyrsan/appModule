package com.any.bannerlibrary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author any
 * @time 2019/06/13 17.00
 * @details
 */
public class BannerView extends FrameLayout {

    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private int itemSpace = 20;

    private float centerScale = 1.2f;

    private int moveSpeed = 4000;

    private int indicatorMargin = 50;


    public BannerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {

        mRecyclerView = new RecyclerView(getContext());
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        vpLayoutParams.setMargins(0, 0, 0, indicatorMargin);
        vpLayoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        addView(mRecyclerView, vpLayoutParams);
        mRecyclerView.setBackgroundColor(Color.argb(120, 120, 120, 120));


//        mLayoutManager = new BannerNewLayoutManager(getContext());
        mLayoutManager = new BannerLayoutManager(getContext());
        if(mLayoutManager instanceof BannerLayoutManager){
//            ((BannerLayoutManager) mLayoutManager).setItemSpace(itemSpace);
//            ((BannerLayoutManager) mLayoutManager).setCenterScale(centerScale);
//            ((BannerLayoutManager) mLayoutManager).setMoveSpeed(moveSpeed);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
//        mLayoutManager.setInfinite(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx != 0) {
//                    setPlaying(false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                int first = mLayoutManager.getCurrentPosition();
                Log.d("xxx", "onScrollStateChanged");
//                if (currentIndex != first) {
//                    currentIndex = first;
//                }
//                if (newState == SCROLL_STATE_IDLE) {
//                    setPlaying(true);
//                }
//                refreshIndicator();
            }
        });
    }


}
