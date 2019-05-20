package com.any.imagelibrary.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.any.imagelibrary.R
import com.any.imagelibrary.manager.ConfigManager
import com.any.imagelibrary.manager.SelectionManager
import com.any.imagelibrary.model.ItemType
import com.any.imagelibrary.model.MediaFile
import com.any.imagelibrary.utils.Utils
import com.any.imagelibrary.widget.SquareImageView
import com.any.imagelibrary.widget.SquareRelativeLayout

/**
 * 列表适配器
 * Create by: chenWei.li
 * Date: 2018/8/23
 * Time: 上午1:18
 * Email: lichenwei.me@foxmail.com
 */
class ImagePickerAdapter(private val mContext: Context, private val mMediaFileList: List<MediaFile>?) :
    RecyclerView.Adapter<ImagePickerAdapter.BaseHolder>() {
    private val isShowCamera: Boolean = ConfigManager.getInstance().isShowCamera
    /**
     * 接口回调，将点击事件向外抛
     */
    private var mOnItemClickListener: OnItemClickListener? = null


    override fun getItemViewType(position: Int): Int {
        var position = position
        if (isShowCamera) {
            if (position == 0) {
                return ItemType.ITEM_TYPE_CAMERA
            }
            //如果有相机存在，position位置需要-1
            position--
        }
        return if (mMediaFileList!![position].getDuration() > 0) {
            ItemType.ITEM_TYPE_VIDEO
        } else {
            ItemType.ITEM_TYPE_IMAGE
        }
    }

    override fun getItemCount(): Int {
        if (mMediaFileList == null) {
            return 0
        }
        return if (isShowCamera) mMediaFileList.size + 1 else mMediaFileList.size
    }

    /**
     * 获取item所对应的数据源
     *
     * @param position
     * @return
     */
    fun getMediaFile(position: Int): MediaFile? {
        return if (isShowCamera) {
            if (position == 0) {
                null
            } else mMediaFileList!![position - 1]
        } else mMediaFileList!![position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val view: View
        return when (viewType) {
            ItemType.ITEM_TYPE_CAMERA -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.imgpk_item_recyclerview_camera, null)
                BaseHolder(view)
            }
            ItemType.ITEM_TYPE_IMAGE -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.imgpk_item_recyclerview_image, null)
                ImageHolder(view)
            }
            else -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.imgpk_item_recyclerview_video, null)
                VideoHolder(view)
            }
        }

    }


    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val itemType = getItemViewType(position)
        val mediaFile = getMediaFile(position)
        when (itemType) {
            //图片、视频Item
            ItemType.ITEM_TYPE_IMAGE, ItemType.ITEM_TYPE_VIDEO -> {
                val mediaHolder = holder as MediaHolder
                bindMedia(mediaHolder, mediaFile!!)
            }
            //相机Item
            else -> {
            }
        }

        //设置点击事件监听
        mOnItemClickListener?.let {
            holder.mSquareRelativeLayout.setOnClickListener { view ->
                it.onMediaClick(
                    view,
                    position
                )
            }
            if (holder is MediaHolder) {
                holder.mImageCheck.setOnClickListener { view -> it.onMediaCheck(view, position) }
            }
        }

    }


    /**
     * 绑定数据（图片、视频）
     *
     * @param mediaHolder
     * @param mediaFile
     */
    private fun bindMedia(mediaHolder: MediaHolder, mediaFile: MediaFile) {

        val imagePath = mediaFile.getPath()
        //选择状态（仅是UI表现，真正数据交给SelectionManager管理）
        if (SelectionManager.getInstance().isImageSelect(imagePath)) {
            mediaHolder.mImageView.setColorFilter(Color.parseColor("#77000000"))
            mediaHolder.mImageCheck.setImageDrawable(mContext.resources.getDrawable(R.mipmap.icon_image_checked))
        } else {
            mediaHolder.mImageView.colorFilter = null
            mediaHolder.mImageCheck.setImageDrawable(mContext.resources.getDrawable(R.mipmap.icon_image_check))
        }

        try {
            ConfigManager.getInstance().imageLoader?.loadImage(mediaHolder.mImageView, imagePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mediaHolder is ImageHolder) {
            //如果是gif图，显示gif标识
            val suffix = imagePath!!.substring(imagePath.lastIndexOf(".") + 1)
            if (suffix.toUpperCase() == "GIF") {
                mediaHolder.mImageGif.visibility = View.VISIBLE
            } else {
                mediaHolder.mImageGif.visibility = View.GONE
            }
        }

        if (mediaHolder is VideoHolder) {
            //如果是视频，需要显示视频时长
            val duration = Utils.getVideoDuration(mediaFile.getDuration())
            mediaHolder.mVideoDuration.text = duration
        }

    }

    /**
     * 图片Item
     */
    internal inner class ImageHolder(itemView: View) : MediaHolder(itemView) {
        var mImageGif: ImageView = itemView.findViewById(R.id.iv_item_gif)
    }

    /**
     * 视频Item
     */
    internal inner class VideoHolder(itemView: View) : MediaHolder(itemView) {
        val mVideoDuration: TextView = itemView.findViewById(R.id.tv_item_videoDuration)
    }

    /**
     * 媒体Item
     */
    internal open inner class MediaHolder(itemView: View) : BaseHolder(itemView) {
        var mImageView: SquareImageView = itemView.findViewById(R.id.iv_item_image)
        var mImageCheck: ImageView = itemView.findViewById(R.id.iv_item_check)
    }

    /**
     * 基础Item
     */
    open inner class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mSquareRelativeLayout: SquareRelativeLayout = itemView.findViewById(R.id.srl_item)

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onMediaClick(view: View, position: Int)

        fun onMediaCheck(view: View, position: Int)
    }
}
