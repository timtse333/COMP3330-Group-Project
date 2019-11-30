package tim.hku.comp3330.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class responseAdapter  extends RecyclerView.Adapter<responseHolder>{
    Context c;
    ArrayList<Message> model;
    private DB database;
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
        myHolder.username.setText("Kaori");
        myHolder.content.setText(model.get(i).getMessageContent());
        int proPic = myHolder.itemView.getContext().getResources().getIdentifier("test","drawable","tim.hku.comp3330");
        myHolder.profile.setImageResource(proPic);

    }
    @Override
    public int getItemCount() {
        return model.size();
    }
}
