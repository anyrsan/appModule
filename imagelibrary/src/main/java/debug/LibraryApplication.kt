package debug

import android.app.Application
import android.util.Log

/**
 *
 * @author any
 * @time 2019/05/18 15.02
 * @details 主入口
 */
class LibraryApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("msg","...进入 LibraryApplication")
    }

}