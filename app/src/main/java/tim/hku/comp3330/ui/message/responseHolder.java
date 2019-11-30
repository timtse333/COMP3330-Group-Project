package tim.hku.comp3330.ui.message;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tim.hku.comp3330.R;

public class responseHolder extends RecyclerView.ViewHolder {
    TextView username, content;
    de.hdodenhof.circleimageview.CircleImageView profile;
    public responseHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.resmsg_user_name);
        this.content = itemView.findViewById(R.id.resmsg_msgContent);
        this.profile = itemView.findViewById(R.id.resmsg_user_icon);
    }
}
