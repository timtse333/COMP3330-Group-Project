package tim.hku.comp3330;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class projectHolder extends RecyclerView.ViewHolder {
    ImageView mImageView;
    TextView mTitle, mDes;

    public projectHolder(@NonNull View itemView) {
        super(itemView);
        this.mImageView = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleTv);
        this.mDes= itemView.findViewById(R.id.descriptionTv);
    }
}
