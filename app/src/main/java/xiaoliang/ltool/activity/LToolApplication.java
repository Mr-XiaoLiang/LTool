package xiaoliang.ltool.activity;

import android.app.Application;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by liuj on 2016/9/13.
 * 应用上下文
 */
public class LToolApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    /**
     *
     "http://site.com/image.png" // from Web
     "file:///mnt/sdcard/image.png" // from SD card
     "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
     "content://media/external/images/media/13" // from content provider
     "content://media/external/video/media/13" // from content provider (video thumbnail)
     "assets://image.png" // from assets
     "drawable://" + R.drawable.img // from drawables (non-9patch images)
     */
    private void initImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    public void T(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
