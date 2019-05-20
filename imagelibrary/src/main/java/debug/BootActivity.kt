package debug

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.any.imagelibrary.R
import com.any.likenessanko.startActivity
import kotlinx.android.synthetic.main.imgpk_boot_activity.*

/**
 *
 * @author any
 * @time 2019/05/18 15.02
 * @details app开启页
 */
class BootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imgpk_boot_activity)

        gotoTest.setOnClickListener {
            startActivity<TestActivity>()
        }
    }
}