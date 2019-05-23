package debug

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.any.imagelibrary.ImagePicker
import com.any.imagelibrary.ImagePicker.Companion.RESULT_OPEN_CODE
import com.any.imagelibrary.ImagePicker.Companion.RESULT_SELECT_CODE
import com.any.imagelibrary.R
import com.any.imagelibrary.model.MediaModel
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.imgpk_test_activity.*
import java.util.ArrayList

/**
 *
 * @author any
 * @time 2019/05/18 15.02
 * @details 测试activity
 */
class TestActivity : AppCompatActivity() {


    private var mTextView: TextView? = null
    private val mImagePaths = ArrayList<MediaModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imgpk_test_activity)
        mTextView = showImages


        val rxPermissions = RxPermissions(this)

        rxPermissions.request(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {

            } else {

            }
        }


        gotoDcim.setOnClickListener {
            ImagePicker.getInstance()
                .setTitle("标题")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setIsCompress(true)
                .setMaxCount(3)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                .setVideoMaxDuration(15000)
                .setImagePaths(mImagePaths)//设置历史选择记录
                .setImageLoader(GlideLoadManager(applicationContext))//设置自定义图片加载器
                .start(this@TestActivity, RESULT_SELECT_CODE)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode

        }

        gotoTake.setOnClickListener {
            ImagePicker.getInstance()
                .setTitle("标题")//设置标题
                .setOpenCamera(true)
                .setIsCompress(false)
                .setImageLoader(GlideLoadManager(applicationContext))//设置自定义图片加载器
                .start(this@TestActivity, RESULT_OPEN_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_SELECT_CODE -> {
                    var temps =
                        ImagePicker.getInstance().getDataFromIntent(data)
                    //清除数据
                    mImagePaths.clear()
                    //重新添加
                    temps?.let {
                        mImagePaths.addAll(it)
                    }
                }
                RESULT_OPEN_CODE -> {
                    var temps =
                        ImagePicker.getInstance().getDataFromIntent(data)
                    temps?.let {
                        mImagePaths.addAll(it)
                    }
                }
            }
        }

        val stringBuffer = StringBuffer()
        stringBuffer.append("当前选中图片路径：\n\n")
        for (i in mImagePaths.indices) {
            stringBuffer.append(mImagePaths[i].path + "\n\n")
            stringBuffer.append(mImagePaths[i].compressPath + "\n\n")
        }
        mTextView?.text = stringBuffer.toString()
    }
}