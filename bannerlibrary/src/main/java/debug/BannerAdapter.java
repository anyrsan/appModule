package debug;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.any.bannerlibrary.R;

/**
 * @author any
 * @time 2019/06/13 17.11
 * @details
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerView> {


    private int[] colors = new int[]{Color.BLACK, Color.WHITE, Color.GREEN, Color.YELLOW, Color.BLUE};

    @NonNull
    @Override
    public BannerView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bnpk_item_layout, null);
        return new BannerView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerView bannerView, int i) {
        int color = getItem(i);
        bannerView.tv.setText("我是第" + i + "个View");
        bannerView.tv.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public int getItem(int position) {
        return colors[position];
    }


    public static class BannerView extends RecyclerView.ViewHolder {

        TextView tv;

        public BannerView(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item);
        }
    }

}
