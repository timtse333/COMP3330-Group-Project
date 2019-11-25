package tim.hku.comp3330.ui.projectDetails;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import tim.hku.comp3330.R;

public class BlogHolder extends RecyclerView.ViewHolder {
    TextView username, content, mDate;
    de.hdodenhof.circleimageview.CircleImageView profile;
    AppCompatImageView postPic;

    public BlogHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.user);
        this.content = itemView.findViewById(R.id.description);
        this.mDate = itemView.findViewById(R.id.date);
        this.profile = itemView.findViewById(R.id.user_image);
        this.postPic = itemView.findViewById(R.id.blogPost_image);
    }
}
