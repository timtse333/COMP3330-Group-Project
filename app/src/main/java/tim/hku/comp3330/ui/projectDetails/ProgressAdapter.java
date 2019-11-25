package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.R;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressHolder> {
    Context c;
    List<ProgressPost> model;

    public ProgressAdapter(Context c, List<ProgressPost> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public ProgressHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_post,viewGroup,false);

        return new ProgressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressHolder myHolder, int i) {
        myHolder.mTitle.setText(model.get(i).getTitle());
        myHolder.mDes.setText(model.get(i).getContent());
        myHolder.mDate.setText(model.get(i).getCreated());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
