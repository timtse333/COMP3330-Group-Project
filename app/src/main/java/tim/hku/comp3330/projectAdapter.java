package tim.hku.comp3330;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class projectAdapter extends RecyclerView.Adapter<projectHolder> {
    Context c;
    ArrayList<projectModel> model;

    public projectAdapter(Context c, ArrayList<projectModel> model) {
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
        myHolder.mTitle.setText(model.get(i).getTitle());
        myHolder.mDes.setText(model.get(i).getDescription());
        myHolder.mImageView.setImageResource(model.get(i).getImg());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
