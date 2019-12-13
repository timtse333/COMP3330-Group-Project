package tim.hku.comp3330.ui.myprojects;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.BlogPost;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.R;

public class myProjectsAdapter extends RecyclerView.Adapter<myProjectsHolder> {
    Context c;
    ArrayList<Project> model;

    public myProjectsAdapter(Context c, ArrayList<Project> model) {
        this.c = c;
        this.model = model;
    }

    @NonNull
    @Override
    public myProjectsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);

        return new myProjectsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myProjectsHolder myHolder, int i) {

        myHolder.mTitle.setText(model.get(i).getProjectName());
        myHolder.mDes.setText(model.get(i).getProjectDescription());

        if(!model.get(i).getProjectPic().equals("project_test")){
            Picasso.with(c)
                    .load(model.get(i).getProjectPic())
                    .fit()
                    .centerCrop()
                    .into(myHolder.mImageView);
        }
        else {
            int img = myHolder.itemView.getContext().getResources().getIdentifier(model.get(i).getProjectPic(),"drawable","tim.hku.comp3330");
            myHolder.mImageView.setImageResource(img);
        }
        int projID = model.get(i).getProjectID();
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("projID", projID);
                Navigation.findNavController(v).navigate(R.id.nav_project_test,bundle);
            }
        });
        myHolder.join.setVisibility(View.GONE);
        myHolder.join.setEnabled(false);


    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
