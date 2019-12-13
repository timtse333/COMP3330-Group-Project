package tim.hku.comp3330.ui.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class responseAdapter  extends RecyclerView.Adapter<responseHolder>{
    Context c;
    ArrayList<Message> model;
    private DB database;
    private DatabaseReference userRef;
    private User user = new User();
    public responseAdapter(Context c, ArrayList<Message> model){
        this.c = c;
        this.model = model;
    }
    @NonNull
    @Override
    public responseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        database = new DB(c);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_response,viewGroup,false);
        return new responseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull responseHolder myHolder, int i) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        Message msg = model.get(i);
        String senderID = msg.getSenderID();
        String userName = prefs.getString("userName","");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        Query userQuery = userRef.orderByChild("userID").equalTo(senderID).limitToFirst(1);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    user= child.getValue(User.class);
                    myHolder.username.setText(user.getUserName());
                    if(user.getIcon()!= null){
                        Picasso.with(c)
                                .load(user.getIcon())
                                .fit()
                                .centerCrop()
                                .into(myHolder.profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myHolder.content.setText(model.get(i).getMessageContent());

    }
    @Override
    public int getItemCount() {
        return model.size();
    }
}
