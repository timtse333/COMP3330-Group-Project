package tim.hku.comp3330;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Project;

public class projectAdapter extends RecyclerView.Adapter<projectHolder> {
    Context c;
    ArrayList<Project> model;

    public projectAdapter(Context c, ArrayList<Project> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public projectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,null);

        return new projectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull projectHolder myHolder, int i) {
        myHolder.mTitle.setText(model.get(i).getProjectName());
        myHolder.mDes.setText(model.get(i).getProjectDescription());
        int img = myHolder.itemView.getContext().getResources().getIdentifier(model.get(i).getProjectPic(),"drawable","tim.hku.comp3330");
        myHolder.mImageView.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
