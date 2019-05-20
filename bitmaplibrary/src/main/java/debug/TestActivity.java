package debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.any.bitmaplibrary.GlideImageLoader;
import com.any.bitmaplibrary.GlideLoadProgress;
import com.any.imagelibrary.R;

/**
 * @author any
 * @time 2019/05/18 17.28
 * @details
 */
public class TestActivity extends AppCompatActivity {

    private ImageView iv;

    private TextView pTv;

    private String url = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=348038309,3092966432&fm=26&gp=0.jpg";
//    private String url = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=545447309,2577719270&fm=26&gp=0.jpg";
//    private String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558328782620&di=3a8c2762c9652f0d27846f33c3090bcf&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150507%2Fmp13970383_1430951443796_3.jpeg";
//    private String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558328782620&di=00b1dc0ab37dd0dc5575b4a271e2ed6d&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170505%2F629b14410a1a4afe9d8a9f19f8b6aa1d_th.jpeg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmpk_test_activity);
        iv = findViewById(R.id.showImg);
        pTv = findViewById(R.id.progressImg);
//        GlideImageLoader.clearImageDiskCache(this);
//        GlideImageLoader.clearImageMemoryCache(this);


        //
//        GlideImageLoader.loadImage(url,iv);
    }

    public void downImg(View v) {



        // 注意，如果图片已存在了，不进入此函数  String key, int percent, boolean isDone
        GlideLoadProgress.loadBitmap(iv, url, (key, percent, isDone) -> {
            Log.e("msg", "key..." + key + "   percent..." + percent + "   isDone.." + isDone);

            pTv.setText("当前进度： " + percent + "%");

        });
    }
}
