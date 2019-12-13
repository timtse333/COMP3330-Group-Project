package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.util.Log;
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
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.R;

public class BlogAdapter extends RecyclerView.Adapter<BlogHolder> {
    private Context c;
    private ArrayList<BlogPost> model;
    private DatabaseReference userRef;
    private User poster;
    public BlogAdapter(Context c, ArrayList<BlogPost> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.blog_post,viewGroup,false);

        return new BlogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogHolder myHolder, int i) {
        String userID = model.get(i).getOwnerID();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("userID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    poster = postSnapShot.getValue(User.class);
                }
                if(model.get(i).getBlogPostPic()!= null){
                    Log.d("myTag", "Not null: " + model.get(i).getBlogPostPic());
                    myHolder.username.setText(poster.getUserName());
                    myHolder.content.setText(model.get(i).getContent());
                    myHolder.mDate.setText(model.get(i).getCreated());
                    Picasso.with(c)
                            .load(model.get(i).getBlogPostPic())
                            .fit()
                            .centerCrop()
                            .into(myHolder.postPic);
                }
                else {
                    myHolder.postPic.getLayoutParams().height = 0;
                    myHolder.postPic.requestLayout();
                    myHolder.username.setText(poster.getUserName());
                    myHolder.content.setText(model.get(i).getContent());
                    myHolder.mDate.setText(model.get(i).getCreated());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
