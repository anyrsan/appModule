package debug

import android.content.Context
import android.widget.ImageView
import com.any.bitmaplibrary.GlideImageLoader
import com.any.imagelibrary.loader.ImageLoader

/**
 *
 * @author any
 * @time 2019/05/20 14.57
 * @details
 */
class GlideLoadManager(private val appContext: Context) : ImageLoader {
    override fun loadImage(imageView: ImageView, imagePath: String?) {
        imagePath?.let {
            GlideImageLoader.loadImage(it, imageView,false)
        }
    }

    override fun loadPreImage(imageView: ImageView, imagePath: String) {
        GlideImageLoader.loadImage(imagePath, imageView,false)
    }

    override fun clearMemoryCache() {
        GlideImageLoader.clearImageDiskCache(appContext)
    }

}