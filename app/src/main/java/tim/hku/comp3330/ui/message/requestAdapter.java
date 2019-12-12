package tim.hku.comp3330.ui.message;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.DataClass.UserProjectRelation;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Database.DBUtil;
import tim.hku.comp3330.R;
import tim.hku.comp3330.ui.myprojects.myProjectsAdapter;

public class requestAdapter extends RecyclerView.Adapter<requestHolder> {
    Context c;
    ArrayList<Message> model;
    private DB database;
    private DBUtil dbUtil;
    private DatabaseReference associatonRef;
    private DatabaseReference projRef;
    private Project proj = new Project();
    private UserProjectRelation relation = new UserProjectRelation();
    public requestAdapter(Context c, ArrayList<Message> model){
        this.c = c;
        this.model = model;
    }
    @NonNull
    @Override
    public requestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        database = new DB(c);
        dbUtil = new DBUtil();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_request,viewGroup,false);

        return new requestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull requestHolder myHolder, int i) {
        Message msg = model.get(i);
        myHolder.username.setText("Kaori");
        myHolder.content.setText(msg.getMessageContent());
        int proPic = myHolder.itemView.getContext().getResources().getIdentifier("test","drawable","tim.hku.comp3330");
        myHolder.profile.setImageResource(proPic);

        myHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projRef = FirebaseDatabase.getInstance().getReference("Projects");
                Query projQuery = projRef.orderByChild("projectID").equalTo(msg.getProjID()).limitToFirst(1);
                projQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child: dataSnapshot.getChildren()) {
                            proj= child.getValue(Project.class);
                        }
                        String accepttMsg = "Your request in joining project <" + proj.getProjectName() + "> has been accepted";
                        msg.setMessageContent(accepttMsg);
                        String receiverID = msg.getReceiverID();
                        String senderID = msg.getSenderID();
                        relation.setProjectID(proj.getProjectID());
                        relation.setUserID(senderID);
                        dbUtil.CreateRelation(relation);
                        msg.setReceiverID(senderID);//Send the rejected msg back to sender
                        msg.setSenderID(receiverID);
                        msg.setDeleted(true);
                        dbUtil.updateMessage(msg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        myHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projRef = FirebaseDatabase.getInstance().getReference("Projects");
                Query projQuery = projRef.orderByChild("projectID").equalTo(msg.getProjID()).limitToFirst(1);
                projQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child: dataSnapshot.getChildren()) {
                            proj= child.getValue(Project.class);
                        }
                        String rejectMsg = "Your request in joining project <" + proj.getProjectName() + "> has been rejected";
                        msg.setMessageContent(rejectMsg);
                        String receiverID = msg.getReceiverID();
                        String senderID = msg.getSenderID();
                        msg.setReceiverID(senderID);//Send the rejected msg back to sender
                        msg.setSenderID(receiverID);
                        msg.setDeleted(true);
                        dbUtil.updateMessage(msg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
