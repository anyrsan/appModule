package com.any.bitmaplibrary;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.any.bitmaplibrary.http.ICallBack;
import com.any.bitmaplibrary.http.OnProgressListener;
import com.any.bitmaplibrary.http.ProgressManager;
import com.any.bitmaplibrary.http.ProgressRunnable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


/**
 * @author any
 * @date 2017/12/8
 * 带加载进度的Glide
 */

public class GlideLoadProgress {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 加载图片 带进度
     * @param view
     * @param url
     * @param callBack
     */
    public static void loadBitmap(ImageView view, final String url, final ICallBack callBack) {

        final ProgressRunnable runnable = new ProgressRunnable(callBack);

        final OnProgressListener onProgressListener = (key, percent, isDone, ex) -> {
            runnable.key = key;
            runnable.percent = percent;
            mainHandler.post(runnable);
        };

        ProgressManager.addOnProgressListener(url, onProgressListener);
        GlideApp.with(view.getContext()).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                ProgressManager.removeOnProgressListener(url);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                ProgressManager.testLists();
                return false;
            }
        }).dontAnimate().into(view);
    }
}
