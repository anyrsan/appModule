package com.any.bitmaplibrary;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * @author any
 * @time 2019/05/20 10.05
 * @details 处理图片加载的类
 */
public class GlideImageLoader {


    public static void loadImage(String url, ImageView view) {
        GlideApp.with(view).asBitmap().load(url).dontAnimate().into(view);
    }

    public static void loadImage(String url, ImageView view, @DrawableRes int rId) {
        GlideApp.with(view).asBitmap().load(url).placeholder(rId).dontAnimate().into(view);
    }


    /**
     * 不缓存 ，是否使用内存
     * @param filePath
     * @param view
     * @param isCache  false不使用内存，每次加载最新
     * @return
     */
    public static boolean loadImage(String filePath, ImageView view, boolean isCache) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            GlideApp.with(view.getContext()).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(isCache).into(view);
            return true;
        }
        return false;
    }


    /**
     * PS:  运行在 backThread
     * 根据网络图片的链接，获取本地图片文件的路径 string
     *
     * @param context 上下文对象
     * @param url     网络路径
     * @return String
     */
    public static String getFilePathByUrl(Context context, String url) {
        try {
            File file = GlideApp.with(context).downloadOnly().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (file != null) {
                return file.getAbsolutePath();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(() -> GlideApp.get(context).clearDiskCache()).start();
            } else {
                GlideApp.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                GlideApp.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
