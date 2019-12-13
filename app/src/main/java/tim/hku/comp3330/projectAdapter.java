package tim.hku.comp3330;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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
import tim.hku.comp3330.DataClass.UserProjectRelation;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Database.DBUtil;
import tim.hku.comp3330.ui.home.HomeFragment;
import tim.hku.comp3330.ui.login.LoginFragment;
import tim.hku.comp3330.ui.myprojects.myProjectsAdapter;
import tim.hku.comp3330.ui.projectDetails.projectDetail;

public class projectAdapter extends RecyclerView.Adapter<projectHolder> {
    Context c;
    ArrayList<Project> model;
    DBUtil dbUtil;
    DatabaseReference userRef;
    User user = new User();
    DatabaseReference relationRef;
    public projectAdapter(Context c, ArrayList<Project> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public projectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        dbUtil = new DBUtil();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);

        return new projectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull projectHolder myHolder, int i) {
        myHolder.mTitle.setText(model.get(i).getProjectName());
        myHolder.mDes.setText(model.get(i).getProjectDescription());
        if(!model.get(i).getProjectPic().equals("project_test")){
            Picasso.with(c)
                    .load(model.get(i).getProjectPic())
                    .fit()
                    .centerCrop()
                    .into(myHolder.mImageView);
        }
        else {
            int img = myHolder.itemView.getContext().getResources().getIdentifier(model.get(i).getProjectPic(),"drawable","tim.hku.comp3330");
            myHolder.mImageView.setImageResource(img);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        String userID = prefs.getString("userID","");
        Project proj = model.get(i);
        relationRef = FirebaseDatabase.getInstance().getReference("UserProjectRelation");
        Query projQuery = relationRef.orderByChild("projectID").equalTo(proj.getProjectID());
        projQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    UserProjectRelation temp = child.getValue(UserProjectRelation.class);
                    if(temp.getUserID().equals(userID)){
                        myHolder.join.setVisibility(View.GONE);
                        myHolder.join.setEnabled(false);
                    }else{
                        myHolder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ConstructRequestMessage(userID, proj.getOwnerID(), proj);
                                myHolder.join.setVisibility(View.GONE);
                                myHolder.join.setEnabled(false);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("projID", proj.getProjectID());
                Navigation.findNavController(v).navigate(R.id.nav_project_test,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void ConstructRequestMessage(String senderID, String receiverID, Project project){
        Message msg = new Message();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        Query userQuery = userRef.orderByChild("userID").equalTo(senderID).limitToFirst(1);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    user = child.getValue(User.class);
                    msg.setProjID(project.getProjectID());
                    msg.setSenderID(senderID);
                    msg.setReceiverID(receiverID);
                    msg.setMessageContent(user.getUserName() + " wants to join the project <" + project.getProjectName() + ">");
                    dbUtil.CreateMessage(msg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
