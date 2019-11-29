package tim.hku.comp3330.ui.myprojects;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import tim.hku.comp3330.R;

public class myProjectsHolder extends RecyclerView.ViewHolder {
    ImageView mImageView;
    TextView mTitle, mDes;

    public myProjectsHolder(@NonNull View itemView) {
        super(itemView);
        this.mImageView = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleTv);
        this.mDes= itemView.findViewById(R.id.descriptionTv);
    }
}
