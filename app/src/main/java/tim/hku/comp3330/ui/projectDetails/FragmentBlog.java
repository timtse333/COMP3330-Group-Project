package tim.hku.comp3330.ui.projectDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import tim.hku.comp3330.DataClass.BlogPost;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class FragmentBlog extends Fragment {
    private DatabaseReference databaseRef;
    private int projectID;
    RecyclerView myRecycleriew;
    BlogAdapter myAdapter;
    DB database;

    public FragmentBlog () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.blog_fragment, container, false);
        Bundle bundle = getArguments();
        projectID = bundle.getInt("projID");
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseRef = FirebaseDatabase.getInstance().getReference("BlogPost");
        getMyList();


        return rootView;
    }



    private void getMyList() {
        final ArrayList<BlogPost> models = new ArrayList<>();


        databaseRef.orderByChild("projectId").equalTo(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    BlogPost post = postSnapShot.getValue(BlogPost.class);
                    models.add(post);
                }
                Collections.reverse(models);
                myAdapter = new BlogAdapter(getActivity(),models);
                myRecycleriew.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
