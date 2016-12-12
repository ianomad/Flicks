package codepath.flicks.view.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import codepath.flicks.R;

public class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.rootView)
    public View rootView;

    public ViewHolder(View itemView) {
        super(itemView);
    }
}
