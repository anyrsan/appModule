package debug

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.any.component.R

/**
 *
 * @author any
 * @time 2019/06/10 10.49
 * @details
 */
class MainAdapter : RecyclerView.Adapter<ViewAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cppk_item_layout, null)
        return ViewAdapter(view)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: ViewAdapter, position: Int) {

    }


}