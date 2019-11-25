package tim.hku.comp3330.ui.projectDetails;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tim.hku.comp3330.R;

public class ProgressHolder extends RecyclerView.ViewHolder {
    TextView mTitle, mDes, mDate;

    public ProgressHolder(@NonNull View itemView) {
        super(itemView);
        this.mTitle = itemView.findViewById(R.id.titleTv);
        this.mDes= itemView.findViewById(R.id.descriptionTv);
        this.mDate = itemView.findViewById(R.id.date);
    }
}
