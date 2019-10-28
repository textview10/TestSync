package com.bignox.testsync.andserver;

import android.content.Context;

import com.yanzhenjie.andserver.annotation.Config;
import com.yanzhenjie.andserver.annotation.Website;
import com.yanzhenjie.andserver.framework.config.Multipart;
import com.yanzhenjie.andserver.framework.config.WebConfig;
import com.yanzhenjie.andserver.framework.website.AssetsWebsite;

import java.io.File;

/**
 * @author xu.wang
 * @date 2019/10/25 15:36
 * @desc
 */
@Config
public class WebSiteConfig implements WebConfig {
    @Override
    public void onConfig(Context context, Delegate delegate) {
//        ErrorPageWebSite webSite = new ErrorPageWebSite(context);
//        delegate.addWebsite(webSite);

        delegate.addWebsite(new AssetsWebsite(context, "/test"));

        delegate.setMultipart(Multipart.newBuilder()
                .allFileMaxSize(1024 * 1024 * 20) // 20M
                .fileMaxSize(1024 * 1024 * 5) // 5M
                .maxInMemorySize(1024 * 10) // 1024 * 10 bytes
                .uploadTempDir(new File(context.getCacheDir(), "_server_upload_cache_"))
                .build());
    }
}
