package com.any.imagelibrary.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.any.imagelibrary.ImagePicker
import com.any.imagelibrary.ImagePicker.Companion.REQUEST_CODE_CAPTURE
import com.any.imagelibrary.ImagePicker.Companion.REQUEST_SELECT_IMAGES_CODE
import com.any.imagelibrary.ImagePicker.Companion.RESULT_OPEN_CODE
import com.any.imagelibrary.R
import com.any.imagelibrary.adapter.ImageFoldersAdapter
import com.any.imagelibrary.adapter.ImagePickerAdapter
import com.any.imagelibrary.executors.CommonExecutor
import com.any.imagelibrary.listener.MediaLoadCallback
import com.any.imagelibrary.manager.ConfigManager
import com.any.imagelibrary.manager.SelectionManager
import com.any.imagelibrary.model.MediaFile
import com.any.imagelibrary.model.MediaFolder
import com.any.imagelibrary.model.MediaModel
import com.any.imagelibrary.task.ImageLoadTask
import com.any.imagelibrary.task.MediaLoadTask
import com.any.imagelibrary.task.VideoLoadTask
import com.any.imagelibrary.utils.DataUtil
import com.any.imagelibrary.utils.MediaFileUtil
import com.any.imagelibrary.utils.Utils
import com.any.imagelibrary.widget.ImageFolderPopupWindow
import com.zhy.base.fileprovider.FileProvider7
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * 多图选择器主页面
 * Create by: chenWei.li
 * Date: 2018/8/23
 * Time: 上午1:10
 * Email: lichenwei.me@foxmail.com
 */
class ImagePickerActivity : BaseActivity(), ImagePickerAdapter.OnItemClickListener,
    ImageFoldersAdapter.OnImageFolderChangeListener {

    /**
     * 启动参数
     */
    private var mTitle: String? = null
    private var isShowCamera: Boolean = false
    private var isShowImage: Boolean = false
    private var isShowVideo: Boolean = false
    private var isSingleType: Boolean = false
    private var mMaxCount: Int = 0
    private var mMaxDuration: Long = 0
    private var isCompress = false
    private var ignoreBy = 0


//    private var mImagePaths: List<MediaModel>? = null

    /**
     * 界面UI
     */
    private var mTvTitle: TextView? = null
    private var mTvCommit: TextView? = null
    private var mTvImageTime: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mTvImageFolders: TextView? = null
    private var mImageFolderPopupWindow: ImageFolderPopupWindow? = null
    //    private var mProgressDialog: ProgressDialog? = null
    private var mRlBottom: RelativeLayout? = null
    private var pbar: ProgressBar? = null

    private var mGridLayoutManager: GridLayoutManager? = null
    private var mImagePickerAdapter: ImagePickerAdapter? = null

    //图片数据源
    private var mMediaFileList: MutableList<MediaFile>? = null
    //文件夹数据源
    private var mMediaFolderList: List<MediaFolder>? = null

    //是否显示时间
    private var isShowTime: Boolean = false

    private val mMyHandler = Handler()
    private val mHideRunnable = Runnable { hideImageTime() }

    /**
     * 拍照相关
     */
    private var mFilePath: String? = null

    //设置压缩后的目标目录
    private var mTargetDir: String? = null

    override fun bindLayout(): Int {
        return R.layout.imgpk_activity_imagepicker
    }


    /**
     * 初始化配置
     */
    override fun initConfig() {
        mTitle = ConfigManager.getInstance().title
        isShowCamera = ConfigManager.getInstance().isShowCamera
        isShowImage = ConfigManager.getInstance().isShowImage
        isShowVideo = ConfigManager.getInstance().isShowVideo
        isSingleType = ConfigManager.getInstance().isSingleType
        mMaxCount = ConfigManager.getInstance().maxCount
        mMaxDuration = ConfigManager.getInstance().maxDuration
        isCompress = ConfigManager.getInstance().isCompress
        ignoreBy = ConfigManager.getInstance().ignoreBy


        SelectionManager.getInstance().maxCount = mMaxCount
        //载入历史选择记录
        val mImagePaths = ConfigManager.getInstance().mediaList
        mImagePaths?.let {
            SelectionManager.getInstance().addImagePathsToSelectList(it)
        }
    }


    /**
     * 初始化布局控件
     */
    override fun initView() {


        //顶部栏相关
        mTvTitle = findViewById(R.id.tv_actionBar_title)
        if (TextUtils.isEmpty(mTitle)) {
            mTvTitle?.text = getString(R.string.imgpk_image_picker)
        } else {
            mTvTitle?.text = mTitle
        }

        pbar = findViewById(R.id.pbar)

        mTvCommit = findViewById(R.id.tv_actionBar_commit)

        //滑动悬浮标题相关
        mTvImageTime = findViewById(R.id.tv_image_time)

        //底部栏相关
        mRlBottom = findViewById(R.id.rl_main_bottom)
        mTvImageFolders = findViewById(R.id.tv_main_imageFolders)

        //列表相关
        mRecyclerView = findViewById(R.id.rv_main_images)
        mGridLayoutManager = GridLayoutManager(this, 4)
        mRecyclerView?.layoutManager = mGridLayoutManager
        //注释说当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小。
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.setItemViewCacheSize(60)

        mMediaFileList = ArrayList()
        mImagePickerAdapter = ImagePickerAdapter(this, mMediaFileList)
        mImagePickerAdapter?.setOnItemClickListener(this)
        mRecyclerView?.adapter = mImagePickerAdapter


    }

    /**
     * 初始化控件监听事件
     */
    override fun initListener() {

        findViewById<View>(R.id.iv_actionBar_back).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        mTvCommit?.setOnClickListener { commitSelection() }

        mTvImageFolders?.setOnClickListener {
            if (mImageFolderPopupWindow != null) {
                setLightMode(LIGHT_OFF)
                mImageFolderPopupWindow?.showAsDropDown(mRlBottom, 0, 0)
            }
        }

        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                updateImageTime()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateImageTime()
            }
        })

    }

    /**
     * 获取数据源
     */
    override fun getData() {
        // 获取请求码
        if (ConfigManager.getInstance().isOpenCamera) {
            showCamera(RESULT_OPEN_CODE)
        } else {
            showPbar(true)
            startScannerTask()
        }

    }


    private fun showPbar(isShow: Boolean) {
        pbar?.visibility = if (isShow) View.VISIBLE else View.GONE
    }


    /**
     * 开启扫描任务
     */
    private fun startScannerTask() {
        var mediaLoadTask: Runnable? = null

        Log.e("msg", "-->isShowVideo...$isShowVideo")

        //照片、视频全部加载
        if (isShowImage && isShowVideo) {
            mediaLoadTask = MediaLoadTask(this, MediaLoader())
        }

        //只加载视频
        if (!isShowImage && isShowVideo) {
            mediaLoadTask = VideoLoadTask(this, MediaLoader())
        }

        //只加载图片
        if (isShowImage && !isShowVideo) {
            mediaLoadTask = ImageLoadTask(this, MediaLoader())
        }

        //不符合以上场景，采用照片、视频全部加载
        if (mediaLoadTask == null) {
            mediaLoadTask = MediaLoadTask(this, MediaLoader())
        }

        CommonExecutor.getInstance().execute(mediaLoadTask)
    }


    /**
     * 处理媒体数据加载成功后的UI渲染
     */
    internal inner class MediaLoader : MediaLoadCallback {

        override fun loadMediaSuccess(mediaFolderList: List<MediaFolder>) {
            runOnUiThread {
                if (mediaFolderList.isNotEmpty()) {
                    //默认加载全部照片
                    mMediaFileList?.addAll(mediaFolderList[0].mediaFileList!!)
                    mImagePickerAdapter?.notifyDataSetChanged()
                    //图片文件夹数据
                    mMediaFolderList = ArrayList(mediaFolderList)

                    mMediaFolderList?.let {
                        mImageFolderPopupWindow = ImageFolderPopupWindow(this@ImagePickerActivity, it)
                        mImageFolderPopupWindow?.apply {
                            animationStyle = R.style.imgpk_imageFolderAnimator
                            getAdapter()?.setOnImageFolderChangeListener(this@ImagePickerActivity)
                            setOnDismissListener { setLightMode(LIGHT_ON) }
                        }
                        updateCommitButton()
                    }
                }
                showPbar(false)
            }
        }
    }

//Suppress: Add @SuppressLint("ObjectAnimatorBinding") annotation
    /**
     * 隐藏时间
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun hideImageTime() {
        if (isShowTime) {
            isShowTime = false
            ObjectAnimator.ofFloat(mTvImageTime, "alpha", 1F, 0F).setDuration(300).start()
        }
    }

    /**
     * 显示时间
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun showImageTime() {
        if (!isShowTime) {
            isShowTime = true
            ObjectAnimator.ofFloat(mTvImageTime, "alpha", 0F, 1F).setDuration(300).start()
        }
    }

    /**
     * 更新时间
     */
    private fun updateImageTime() {
        val position = mGridLayoutManager!!.findFirstVisibleItemPosition()
        val mediaFile = mImagePickerAdapter!!.getMediaFile(position)
        if (mediaFile != null) {

            mTvImageTime?.let {
                if (it.visibility != View.VISIBLE) {
                    it.visibility = View.VISIBLE
                }
                val time = Utils.getImageTime(mediaFile.getDateToken())
                it.text = time
            }
            showImageTime()
            mMyHandler.removeCallbacks(mHideRunnable)
            mMyHandler.postDelayed(mHideRunnable, 1500)
        }
    }

    /**
     * 设置屏幕的亮度模式
     *
     * @param lightMode
     */
    private fun setLightMode(lightMode: Int) {
        val layoutParams = window.attributes
        when (lightMode) {
            LIGHT_OFF -> layoutParams.alpha = 0.7f
            LIGHT_ON -> layoutParams.alpha = 1.0f
        }
        window.attributes = layoutParams
    }

    /**
     * 点击图片
     *
     * @param view
     * @param position
     */
    override fun onMediaClick(view: View, position: Int) {
        if (isShowCamera) {
            if (position == 0) {
                if (!SelectionManager.getInstance().isCanChoose) {
                    ConfigManager.getInstance()
                        .imageLoader?.showToast(String.format(getString(R.string.imgpk_select_image_max), mMaxCount))
                    return
                }
                showCamera(REQUEST_CODE_CAPTURE)
                return
            }
        }


        mMediaFileList?.let {
            DataUtil.getInstance().mediaData = it
            val intent = Intent(this, ImagePreActivity::class.java)
            if (isShowCamera) {
                intent.putExtra(ImagePreActivity.IMAGE_POSITION, position - 1)
            } else {
                intent.putExtra(ImagePreActivity.IMAGE_POSITION, position)
            }
            startActivityForResult(intent, REQUEST_SELECT_IMAGES_CODE)
        }
    }

    /**
     * 选中/取消选中图片
     *
     * @param view
     * @param position
     */
    override fun onMediaCheck(view: View, position: Int) {
        if (isShowCamera) {
            if (position == 0) {
                if (!SelectionManager.getInstance().isCanChoose) {
//                    Toast.makeText(
//                        this,
//                        String.format(getString(R.string.imgpk_select_image_max), mMaxCount),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    ConfigManager.getInstance()
                        .imageLoader?.showToast(String.format(getString(R.string.imgpk_select_image_max), mMaxCount))
                    return
                }
                showCamera(REQUEST_CODE_CAPTURE)
                return
            }
        }

        //执行选中/取消操作
        val mediaFile = mImagePickerAdapter?.getMediaFile(position)
        if (mediaFile != null) {
            val imagePath = mediaFile.getPath()
            if (isSingleType) {
                //单类型选取，判断添加类型
                val selectPathList = SelectionManager.getInstance().selectMap.keys
                if (selectPathList.isNotEmpty()) {
                    //判断选中集合中第一项是否为视频
                    val isVideo = SelectionManager.getInstance().checkPathVideoFileType()
                    //不合格
                    if (isVideo && mediaFile.getDuration() > mMaxDuration) {
                        //类型不同
                        ConfigManager.getInstance().imageLoader?.showToast(getString(R.string.imgpk_max_support_choose))
                        return
                    } else if (!isVideo && mediaFile.getDuration() != 0L || isVideo && mediaFile.getDuration() == 0L) {
                        //类型不同
                        ConfigManager.getInstance().imageLoader?.showToast(getString(R.string.imgpk_single_type_choose))
                        return
                    }
                }
            }

            imagePath?.let {
                val isVideo = MediaFileUtil.isVideoFileType(it)
                //不可以选
                if (isVideo && mediaFile.getDuration() > mMaxDuration) {
                    //类型不同
                    ConfigManager.getInstance().imageLoader?.showToast(getString(R.string.imgpk_max_support_choose))
                    return@let
                }

                val addSuccess = SelectionManager.getInstance().addImageToSelectList(it)
                if (addSuccess) {
                    mImagePickerAdapter?.notifyItemChanged(position)
                } else {
                    ConfigManager.getInstance().imageLoader?.showToast(getString(R.string.imgpk_select_image_max))
                }
            }
        }
        updateCommitButton()
    }

    /**
     * 更新确认按钮状态
     */
    private fun updateCommitButton() {
        //改变确定按钮UI
        val selectCount = SelectionManager.getInstance().selectMap.size
        if (selectCount == 0) {
            mTvCommit?.isEnabled = false
            mTvCommit?.text = getString(R.string.imgpk_confirm)
            return
        }
        if (selectCount < mMaxCount) {
            mTvCommit?.isEnabled = true
            mTvCommit?.text = String.format(getString(R.string.imgpk_confirm_msg), selectCount, mMaxCount)
            return
        }
        if (selectCount == mMaxCount) {
            mTvCommit?.isEnabled = true
            mTvCommit?.text = String.format(getString(R.string.imgpk_confirm_msg), selectCount, mMaxCount)
            return
        }
    }

    /**
     * 跳转相机拍照
     */
    private fun showCamera(requestCode: Int) {
        //拍照存放路径
        val rootPath = Environment.getExternalStorageDirectory().absolutePath
        val fileDir = File(rootPath, "DCIM/Take")

        //压缩目录
        mTargetDir = applicationContext.getExternalFilesDir("compress").absolutePath

        val fileImg = File(fileDir, System.currentTimeMillis().toString() + ".jpg")
        if (!fileDir.exists()) {
            val bool = fileDir.mkdir()
            Log.e("msg", "bool...$bool")
        }
        mFilePath = fileImg.absolutePath
        Log.e("msg", "filePath...$mFilePath")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileUri = FileProvider7.getUriForFile(this, fileImg)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, requestCode)
    }

    /**
     * 当图片文件夹切换时，刷新图片列表数据源
     *
     * @param view
     * @param position
     */
    override fun onImageFolderChange(view: View, position: Int) {
        val (_, folderName, _, _, mediaFileList) = mMediaFolderList!![position]
        //更新当前文件夹名
        if (!TextUtils.isEmpty(folderName)) {
            mTvImageFolders?.text = folderName
        }
        //更新图片列表数据源
        mMediaFileList?.clear()
        mMediaFileList?.addAll(mediaFileList!!)
        mImagePickerAdapter?.notifyDataSetChanged()
        mImageFolderPopupWindow?.dismiss()
    }

    /**
     * 拍照回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAPTURE || requestCode == RESULT_OPEN_CODE) {  // 点击拍照/直接打开拍照
                showPbar(true)
                mFilePath?.let {
                    //通知媒体库刷新
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$it")))
                    val mediaModel = MediaModel(it)
                    //处理压缩图片
                    if (!isCompress) {
                        takeSuccess(mediaModel)
                    } else {
                        Luban.with(applicationContext).load(it).ignoreBy(ignoreBy).setTargetDir(mTargetDir)
                            .setCompressListener(object : OnCompressListener {
                                override fun onSuccess(file: File) {
                                    Log.e("msg", "end....+${SystemClock.currentThreadTimeMillis()}")
                                    //成功回传地址
                                    mediaModel.compressPath = file.absolutePath
                                    takeSuccess(mediaModel)
                                }

                                override fun onError(e: Throwable?) {
                                    Log.e("msg", "err....+${SystemClock.currentThreadTimeMillis()}")
                                    takeSuccess(mediaModel)
                                }

                                override fun onStart() {
                                    Log.e("msg", "start....+${SystemClock.currentThreadTimeMillis()}")
                                }

                            }).launch()
                    }


                }
            } else if (requestCode == REQUEST_SELECT_IMAGES_CODE) {
                commitSelection()
            }
        } else if (requestCode == RESULT_OPEN_CODE) {
            finish()
        }
    }


    /**
     * 拍照回调成功
     */
    private fun takeSuccess(mediaModel: MediaModel) {
        val list = ArrayList<MediaModel>()
        list.add(mediaModel)
        val intent = Intent()
        intent.putParcelableArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES, list)
        setResult(RESULT_OK, intent)
        finish()
    }


    /**
     * 提交成功
     */
    private fun commitSuccess(list: ArrayList<MediaModel>) {
        val intent = Intent()
        intent.putParcelableArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES, list)
        setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * 选择图片完毕，返回
     */
    private fun commitSelection() {
        val map = SelectionManager.getInstance().selectMap
        //没有选择内容
        if (map.isEmpty()) {
            finish()
            return
        }
        val list = ArrayList<MediaModel>()
        val isMedia = SelectionManager.getInstance().checkPathVideoFileType()
        //如果为视频 或者 为不压缩时，都直接显示返回
        if (!isCompress && isMedia) {
            map.values.forEach {
                list.add(it)
            }
            commitSuccess(list)
        } else {
            showPbar(true)
            val compressList = ArrayList<String>()
            val list = ArrayList<MediaModel>()
            // 压缩过，就不存进行压缩
            map.values.forEach {
                list.add(it)
                it.path?.let { path ->
                    compressList.add(path)
                }
            }
            Observable.just(compressList).map {
                Luban.with(applicationContext).load(it).get()
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { cList ->
                    Log.e("msg", "--->$compressList")
                    if (cList.size == compressList.size) {
                        list.clear() //清除之前添加的，重新处理数据
                        cList.forEachIndexed { index, file ->
                            list.add(MediaModel(compressList[index], file.absolutePath))
                        }
                    }
                    commitSuccess(list)
                }
        }
    }


    override fun onResume() {
        super.onResume()
        mImagePickerAdapter?.notifyDataSetChanged()
        updateCommitButton()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            showPbar(false)
            //清除数据
            SelectionManager.getInstance().removeAll()
            //清除缓存
            ConfigManager.getInstance().imageLoader?.clearMemoryCache()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        //表示屏幕亮暗
        private const val LIGHT_OFF = 0
        private const val LIGHT_ON = 1

    }

}
