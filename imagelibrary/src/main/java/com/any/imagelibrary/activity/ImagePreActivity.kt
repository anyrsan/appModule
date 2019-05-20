package com.any.imagelibrary.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.any.imagelibrary.R
import com.any.imagelibrary.adapter.ImagePreViewAdapter
import com.any.imagelibrary.manager.SelectionManager
import com.any.imagelibrary.model.MediaFile
import com.any.imagelibrary.utils.DataUtil
import com.zhy.base.fileprovider.FileProvider7

import java.io.File

/**
 * 大图预览界面
 * Create by: chenWei.li
 * Date: 2018/10/3
 * Time: 下午11:32
 * Email: lichenwei.me@foxmail.com
 */
class ImagePreActivity : BaseActivity() {
    private var mMediaFileList: List<MediaFile>? = null
    private var mPosition = 0

    private var mTvTitle: TextView? = null
    private var mTvCommit: TextView? = null
    private var mIvPlay: ImageView? = null
    private var mViewPager: ViewPager? = null
    private var mLlPreSelect: LinearLayout? = null
    private var mIvPreCheck: ImageView? = null
    private var mImagePreViewAdapter: ImagePreViewAdapter? = null


    override fun bindLayout(): Int {
        return R.layout.imgpk_activity_pre_image
    }

    override fun initView() {
        mTvTitle = findViewById(R.id.tv_actionBar_title)
        mTvCommit = findViewById(R.id.tv_actionBar_commit)
        mIvPlay = findViewById(R.id.iv_main_play)
        mViewPager = findViewById(R.id.vp_main_preImage)
        mLlPreSelect = findViewById(R.id.ll_pre_select)
        mIvPreCheck = findViewById(R.id.iv_item_check)
    }

    override fun initListener() {

        findViewById<View>(R.id.iv_actionBar_back).setOnClickListener { finish() }

        mViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mTvTitle!!.text = String.format("%d/%d", position + 1, mMediaFileList!!.size)
                setIvPlayShow(mMediaFileList!![position])
                updateSelectButton(mMediaFileList!![position].getPath())
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        mLlPreSelect?.setOnClickListener {
            val addSuccess = SelectionManager.getInstance()
                .addImageToSelectList(mMediaFileList!![mViewPager!!.currentItem].getPath()!!)
            if (addSuccess) {
                updateSelectButton(mMediaFileList!![mViewPager!!.currentItem].getPath())
                updateCommitButton()
            } else {
                Toast.makeText(
                    this@ImagePreActivity,
                    String.format(getString(R.string.imgpk_select_image_max), SelectionManager.getInstance().maxCount),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mTvCommit?.setOnClickListener {
            setResult(RESULT_OK, Intent())
            finish()
        }

        mIvPlay!!.setOnClickListener {
            //实现播放视频的跳转逻辑(调用原生视频播放器)
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider7.getUriForFile(
                this@ImagePreActivity,
                File(mMediaFileList!![mViewPager!!.currentItem].getPath())
            )
            intent.setDataAndType(uri, "video/*")
            //给所有符合跳转条件的应用授权
            val resInfoList = packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            startActivity(intent)
        }

    }

    override fun getData() {
        mMediaFileList = DataUtil.getInstance().mediaData
        mPosition = intent.getIntExtra(IMAGE_POSITION, 0)
        mTvTitle?.text = String.format("%d/%d", mPosition + 1, mMediaFileList!!.size)
        mImagePreViewAdapter = ImagePreViewAdapter(this, mMediaFileList)
        mViewPager?.adapter = mImagePreViewAdapter
        mViewPager?.currentItem = mPosition
        //更新当前页面状态
        mMediaFileList?.let {
            setIvPlayShow(it[mPosition])
            updateSelectButton(it[mPosition].getPath())
        }
        updateCommitButton()
    }

    /**
     * 更新确认按钮状态
     */
    private fun updateCommitButton() {

        val maxCount = SelectionManager.getInstance().maxCount

        //改变确定按钮UI
        val selectCount = SelectionManager.getInstance().selectPaths.size
        if (selectCount == 0) {
            mTvCommit?.isEnabled = false
            mTvCommit?.text = getString(R.string.imgpk_confirm)
            return
        }
        if (selectCount < maxCount) {
            mTvCommit?.isEnabled = true
            mTvCommit?.text = String.format(getString(R.string.imgpk_confirm_msg), selectCount, maxCount)
            return
        }
        if (selectCount == maxCount) {
            mTvCommit?.isEnabled = true
            mTvCommit?.text = String.format(getString(R.string.imgpk_confirm_msg), selectCount, maxCount)
            return
        }
    }

    /**
     * 更新选择按钮状态
     */
    private fun updateSelectButton(imagePath: String?) {
        val isSelect = SelectionManager.getInstance().isImageSelect(imagePath)
        if (isSelect) {
            mIvPreCheck?.setImageDrawable(resources.getDrawable(R.mipmap.icon_image_checked))
        } else {
            mIvPreCheck?.setImageDrawable(resources.getDrawable(R.mipmap.icon_image_check))
        }
    }

    /**
     * 设置是否显示视频播放按钮
     * @param mediaFile
     */
    private fun setIvPlayShow(mediaFile: MediaFile) {
        if (mediaFile.getDuration() > 0) {
            mIvPlay?.visibility = View.VISIBLE
        } else {
            mIvPlay?.visibility = View.GONE
        }
    }

    companion object {

        val IMAGE_POSITION = "imagePosition"
    }

}
