package tim.hku.comp3330.ui.message;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tim.hku.comp3330.R;

public class requestHolder extends RecyclerView.ViewHolder {
    TextView username, content;
    de.hdodenhof.circleimageview.CircleImageView profile;
    Button accept, reject;
    public requestHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.reqmsg_user_name);
        this.content = itemView.findViewById(R.id.reqmsg_msgContent);
        this.profile = itemView.findViewById(R.id.reqmsg_user_icon);
        this.accept = itemView.findViewById(R.id.reqmsg_accept);
        this.reject = itemView.findViewById(R.id.reqmsg_reject);
    }
}
