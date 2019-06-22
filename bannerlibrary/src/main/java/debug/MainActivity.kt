package debug

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.any.bannerlibrary.BannerView
import com.any.bannerlibrary.R

/**
 *
 * @author any
 * @time 2019/06/12 17.15
 * @details
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bnpk_main_activity)


        val bnView:BannerView = findViewById(R.id.banner_view)

        val adapter = BannerAdapter()

        bnView.setAdapter(adapter)


        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this)

        val recyclerView:RecyclerView = RecyclerView(this)

        recyclerView.layoutManager = linearLayoutManager
    }
}