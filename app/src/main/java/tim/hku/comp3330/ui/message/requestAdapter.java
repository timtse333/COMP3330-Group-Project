package tim.hku.comp3330.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class requestAdapter extends RecyclerView.Adapter<requestHolder> {
    Context c;
    ArrayList<Message> model;
    private DB database;
    public requestAdapter(Context c, ArrayList<Message> model){
        this.c = c;
        this.model = model;
    }
    @NonNull
    @Override
    public requestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_request,viewGroup,false);

        return new requestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull requestHolder myHolder, int i) {
        myHolder.username.setText("Kaori");
        myHolder.content.setText(model.get(i).getMessageContent());
        int proPic = myHolder.itemView.getContext().getResources().getIdentifier("test","drawable","tim.hku.comp3330");
        myHolder.profile.setImageResource(proPic);
        myHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add participant-project association
                ConstructAcceptMessage(model.get(i).getSenderID(), model.get(i).getReceiverID(), model.get(i).getProjID());
            }
        });
        myHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContructRejectMessage(model.get(i).getSenderID(), model.get(i).getReceiverID(), model.get(i).getProjID());
                database.SoftDeleteMessages(model.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void ContructRejectMessage(int senderID, int receiverID, int projID){
        Message msg = new Message();
        Project proj = new Project();
        proj = database.GetProject(projID);
        msg.setProjID(projID);
        msg.setSenderID(receiverID);
        msg.setReceiverID(senderID);
        msg.setMessageContent("Your request in joining project <" + proj.getProjectName() + "> has been rejected");
        database.CreateMessage(msg);
    }
    public void ConstructAcceptMessage(int senderID, int receiverID, int projID){
        Message msg = new Message();
        Project proj = new Project();
        proj = database.GetProject(projID);
        msg.setProjID(projID);
        msg.setSenderID(receiverID);
        msg.setReceiverID(senderID);
        msg.setMessageContent("Your request in joining project <" + proj.getProjectName() + "> has been accepted");
        database.CreateMessage(msg);
    }
}
