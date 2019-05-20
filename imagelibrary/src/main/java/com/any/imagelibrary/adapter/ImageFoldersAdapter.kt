package com.any.imagelibrary.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.any.imagelibrary.R
import com.any.imagelibrary.manager.ConfigManager
import com.any.imagelibrary.model.MediaFolder


/**
 * 图片文件夹列表适配器
 * Create by: chenWei.li
 * Date: 2018/8/25
 * Time: 上午1:36
 * Email: lichenwei.me@foxmail.com
 */
class ImageFoldersAdapter(
    private val mContext: Context,
    private val mMediaFolderList: List<MediaFolder>?,
    private var mCurrentImageFolderIndex: Int
) : RecyclerView.Adapter<ImageFoldersAdapter.ViewHolder>() {

    /**
     * 接口回调，Item点击事件
     */
    private var mImageFolderChangeListener: OnImageFolderChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.imgpk_item_recyclerview_folder, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val (_, folderName, folderCover, _, mediaFileList) = mMediaFolderList!![position]
        val imageSize = mediaFileList!!.size

        if (!TextUtils.isEmpty(folderName)) {
            holder.mFolderName.text = folderName
        }

        holder.mImageSize.text = String.format(mContext.getString(R.string.imgpk_image_num), imageSize)

        if (mCurrentImageFolderIndex == position) {
            holder.mImageFolderCheck.visibility = View.VISIBLE
        } else {
            holder.mImageFolderCheck.visibility = View.GONE
        }
        //加载图片
        try {
            ConfigManager.getInstance().imageLoader?.loadImage(holder.mImageCover, folderCover)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mImageFolderChangeListener?.let {
            holder.itemView.setOnClickListener { view ->
                mCurrentImageFolderIndex = position
                notifyDataSetChanged()
                it.onImageFolderChange(view, position)
            }
        }

    }

    override fun getItemCount(): Int {
        return mMediaFolderList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageCover: ImageView = itemView.findViewById(R.id.iv_item_imageCover)
        val mFolderName: TextView = itemView.findViewById(R.id.tv_item_folderName)
        val mImageSize: TextView = itemView.findViewById(R.id.tv_item_imageSize)
        val mImageFolderCheck: ImageView = itemView.findViewById(R.id.iv_item_check)

    }

    fun setOnImageFolderChangeListener(onItemClickListener: ImageFoldersAdapter.OnImageFolderChangeListener) {
        this.mImageFolderChangeListener = onItemClickListener
    }

    interface OnImageFolderChangeListener {
        fun onImageFolderChange(view: View, position: Int)
    }
}
