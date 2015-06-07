package com.sky.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

/**
 * Created by zhgn on 15-6-5.
 */
public final class PicassoCache implements Cache {

    /**
     * Cache Instance.
     */
    private static class Instance {
        private static PicassoCache instance = new PicassoCache();
    }

    /**
     * Get singleton.
     * @return this
     */
    public static PicassoCache getInstance() {
        return Instance.instance;
    }

    /**
     * Get customization Picasso
     * @return
     */
    public static Picasso getPicasso() {
        return getInstance().mPicasso;
    }

    /**
     * Clear memory cache.
     * We can't get bitmap key. So we don't need clear key method.
     */
    public static void clearMemCache() {
        getInstance().clear();
    }

    /**
     * Shutdown Picasso.
     */
    public static void shutdown() {
        getInstance().mPicasso.shutdown();
    }

    private Picasso mPicasso;
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * Constructor.
     */
    private PicassoCache() {
        mPicasso = new Picasso.Builder(App.getAppContext())
                .memoryCache(this)
                .indicatorsEnabled(true)
                .build();

        mMemoryCache = new LruCache<String, Bitmap>(getMemoryCacheSize()) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * Get memory cache size.
     * @return size;
     */
    private int getMemoryCacheSize() {
        ActivityManager am = (ActivityManager) App.getAppContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        boolean largeHeap = (App.getAppContext().getApplicationInfo().flags
                & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap) {
            memoryClass = am.getLargeMemoryClass();
        }
        // Target ~15% of the available heap.
        return 1024 * 1024 * memoryClass / 7;
    }

    @Override
    public Bitmap get(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void set(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    @Override
    public int size() {
        return mMemoryCache.size();
    }

    @Override
    public int maxSize() {
        return mMemoryCache.maxSize();
    }

    @Override
    public void clear() {
        mMemoryCache.evictAll();
    }

    @Override
    public void clearKeyUri(String keyPrefix) {
        // Be careful I don't implements this method.
        // So don't call Picasso invalidate(*) method.
        // Maybe the method is not necessary.
    }
}
