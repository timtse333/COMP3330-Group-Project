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

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Database.DBUtil;
import tim.hku.comp3330.R;

public class pendingAdapter extends RecyclerView.Adapter<pendingHolder> {
    Context c;
    ArrayList<Message> model;
    private DB database;
    private DBUtil dbUtil;
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
        myHolder.username.setText(userName);
        myHolder.content.setText(msg.getMessageContent());
        int proPic = myHolder.itemView.getContext().getResources().getIdentifier("test","drawable","tim.hku.comp3330");
        myHolder.profile.setImageResource(proPic);
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
