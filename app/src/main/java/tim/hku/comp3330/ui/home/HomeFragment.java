package tim.hku.comp3330.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;
import tim.hku.comp3330.projectAdapter;
import tim.hku.comp3330.projectList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView myRecycleriew;
    projectAdapter myAdapter;
    DB database;
    private long projCount = 0;
    private DatabaseReference databaseRef;
    ArrayList<DataSnapshot> itemList = new ArrayList<>();
    ArrayList<Project>projList = new ArrayList<>();
    public HomeFragment() {
        //empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ArrayList<Project> get_proj_from_db(){
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot proj : dataSnapshot.getChildren()){
                    Log.d("test:", proj.toString());
                    itemList.add(proj);

                }
                projList = getMyList();
                myAdapter = new projectAdapter(getActivity(),projList);
                myRecycleriew.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return projList;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        database = new DB(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));
        get_proj_from_db();
        myAdapter = new projectAdapter(getActivity(),projList);
        myRecycleriew.setAdapter(myAdapter);


        return rootView;
    }

    private ArrayList<Project> getMyList() {
        ArrayList<Project> models = new ArrayList<>();
        for (DataSnapshot proj : itemList) {
            models.add(proj.getValue(Project.class));
        }

        return models;

    }

// Testing Data:
//        Project m = new Project();
//        m.setProjectID(1);
//        m.setOwnerID(1);
//        m.setProjectPic("project_test");
//        m.setProjectDescription("testing");
//        m.setProjectName("testing");
//
//        models.add(m);


}