package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.BlogPicture;
import tim.hku.comp3330.DataClass.BlogPost;
import tim.hku.comp3330.R;

public class BlogAdapter extends RecyclerView.Adapter<BlogHolder> {
    private Context c;
    private ArrayList<BlogPost> model;
    private DatabaseReference databaseRef;

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

        if(model.get(i).getBlogPostPic().equals("True")){
            int postID = model.get(i).getBlogPostID();
            databaseRef = FirebaseDatabase.getInstance().getReference("BlogPic");
            databaseRef.orderByChild("blogId").equalTo(postID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                        BlogPicture picture = postSnapShot.getValue(BlogPicture.class);
                        myHolder.username.setText("Kaori");
                        myHolder.content.setText(model.get(i).getContent());
                        myHolder.mDate.setText(model.get(i).getCreated());
                        Picasso.with(c)
                                .load(picture.getImageUrl())
                                .fit()
                                .centerCrop()
                                .into(myHolder.postPic);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else {
            myHolder.username.setText("Kaori");
            myHolder.content.setText(model.get(i).getContent());
            myHolder.mDate.setText(model.get(i).getCreated());


        }
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
