package debug

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.any.component.R

/**
 *
 * @author any
 * @time 2019/06/10 10.28
 * @details
 */
class MainActivity :AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView

    private val adapter by lazy { MainAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cppk_view_layout)
        recyclerView = findViewById(R.id.recyclerView)
        val layout = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
    }

}