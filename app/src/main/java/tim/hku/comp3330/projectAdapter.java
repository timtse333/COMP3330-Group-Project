package tim.hku.comp3330;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.ui.home.HomeFragment;
import tim.hku.comp3330.ui.login.LoginFragment;
import tim.hku.comp3330.ui.projectDetails.projectDetail;

public class projectAdapter extends RecyclerView.Adapter<projectHolder> {
    Context c;
    ArrayList<Project> model;
    DB database;
    public projectAdapter(Context c, ArrayList<Project> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public projectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        database = new DB(c);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);

        return new projectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull projectHolder myHolder, int i) {
        myHolder.mTitle.setText(model.get(i).getProjectName());
        myHolder.mDes.setText(model.get(i).getProjectDescription());
        int img = myHolder.itemView.getContext().getResources().getIdentifier(model.get(i).getProjectPic(),"drawable","tim.hku.comp3330");
        myHolder.mImageView.setImageResource(img);
        int projID = model.get(i).getProjectID();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        int userID = prefs.getInt("userID",1);
        Project proj = database.GetProject(projID);
        if(proj.getOwnerID() == userID){
            myHolder.join.setVisibility(View.GONE);
            myHolder.join.setEnabled(false);
        }
        else{
            myHolder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConstructRequestMessage(userID, proj.getOwnerID(), projID);
                }
            });
        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("projID", projID);
                Navigation.findNavController(v).navigate(R.id.nav_project_test,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void ConstructRequestMessage(int senderID, int receiverID, int projID){
        Message msg = new Message();
        Project proj = new Project();
        User user = new User();
        proj = database.GetProject(projID);
        user = database.GetUserByID(senderID);
        msg.setProjID(projID);
        msg.setSenderID(senderID);
        msg.setReceiverID(receiverID);
        msg.setMessageContent(user.getUserName() + " wants to join the project <" + proj.getProjectName() + ">");
        database.CreateMessage(msg);
    }
}
