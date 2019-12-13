package tim.hku.comp3330.ui.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Database.DBUtil;
import tim.hku.comp3330.R;

public class pendingAdapter extends RecyclerView.Adapter<pendingHolder> {
    Context c;
    ArrayList<Message> model;
    private DB database;
    private DBUtil dbUtil;
    private DatabaseReference userRef;
    private User user;
    public pendingAdapter(Context c, ArrayList<Message> model){
        this.c = c;
        this.model = model;
    }
    @NonNull
    @Override
    public pendingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        database = new DB(c);
        dbUtil = new DBUtil();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_pending,viewGroup,false);

        return new pendingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pendingHolder myHolder, int i) {
        Message msg = model.get(i);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        String userName = prefs.getString("userName","");
        String senderID = prefs.getString("userID","no");
        myHolder.username.setText(userName);
        myHolder.content.setText(msg.getMessageContent());
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        Query userQuery = userRef.orderByChild("userID").equalTo(senderID).limitToFirst(1);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    user= child.getValue(User.class);
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
        Project proj = database.GetProject(msg.getProjID());
        myHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cancelMsg = "Your request in joining project <" + proj.getProjectName() + "> has been cancelled";
                msg.setMessageContent(cancelMsg);
                msg.setReceiverID(msg.getSenderID());
                msg.setDeleted(true);
                dbUtil.updateMessage(msg);
                Navigation.findNavController(v).navigate(R.id.nav_message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }


}
