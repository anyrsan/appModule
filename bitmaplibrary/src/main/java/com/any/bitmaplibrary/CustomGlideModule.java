package com.any.bitmaplibrary;

import android.content.Context;
import com.any.bitmaplibrary.http.ProgressManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream;

/**
 * @author any
 * @date 2017/12/8
 */

@GlideModule
public class CustomGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置外部缓存
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.configOkHttpClient()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}