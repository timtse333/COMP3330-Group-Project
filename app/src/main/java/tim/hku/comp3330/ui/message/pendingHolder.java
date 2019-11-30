package tim.hku.comp3330.ui.message;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tim.hku.comp3330.R;

public class pendingHolder extends RecyclerView.ViewHolder {
    TextView username, content;
    de.hdodenhof.circleimageview.CircleImageView profile;
    Button cancel;
    public pendingHolder(@NonNull View itemView){
        super(itemView);
        this.username = itemView.findViewById(R.id.pendingmsg_user_name);
        this.content = itemView.findViewById(R.id.pendingmsg_msgContent);
        this.profile = itemView.findViewById(R.id.pendingmsg_user_icon);
        this.cancel = itemView.findViewById(R.id.pendingmsg_cancel);
    }
}
