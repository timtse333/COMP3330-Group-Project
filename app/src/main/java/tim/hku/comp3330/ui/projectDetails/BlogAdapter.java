package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.BlogPost;
import tim.hku.comp3330.R;

public class BlogAdapter extends RecyclerView.Adapter<BlogHolder> {
    Context c;
    ArrayList<BlogPost> model;

    public BlogAdapter(Context c, ArrayList<BlogPost> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_post,viewGroup,false);

        return new BlogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogHolder myHolder, int i) {
        myHolder.username.setText("Kaori");
        myHolder.content.setText(model.get(i).getContent());
        myHolder.mDate.setText(model.get(i).getCreated());
        int proPic = myHolder.itemView.getContext().getResources().getIdentifier("test","drawable","tim.hku.comp3330");
        myHolder.profile.setImageResource(proPic);
        int postPic = myHolder.itemView.getContext().getResources().getIdentifier(model.get(i).getBlogPostPic(),"drawable","tim.hku.comp3330");
        myHolder.postPic.setImageResource(postPic);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
