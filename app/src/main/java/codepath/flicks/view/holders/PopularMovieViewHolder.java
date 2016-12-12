package codepath.flicks.view.holders;

import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.flicks.R;

public class PopularMovieViewHolder extends ViewHolder {

    @BindView(R.id.imageView)
    public ImageView imageView;

    public PopularMovieViewHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }
}

