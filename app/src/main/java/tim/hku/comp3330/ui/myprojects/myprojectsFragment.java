package tim.hku.comp3330.ui.myprojects;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class myprojectsFragment extends Fragment {

    RecyclerView myRecycleriew;
    myProjectsAdapter projAdapter;
    DB database;
    private DatabaseReference databaseRef;
    private int count = 1;
    private DatabaseReference proj;
    private ArrayList<Integer>projIDList = new ArrayList<>();
    private ArrayList<Project>projList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = new DB(getActivity());
        databaseRef = FirebaseDatabase.getInstance().getReference("UserProjectRelation");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String userID = prefs.getString("userID","");
        Query query = databaseRef.orderByChild("userID").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    projIDList.add((child.child("projectID").getValue(int.class)));
                }
                getMyList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        View root = inflater.inflate(R.layout.fragment_myproject, container, false);
        myRecycleriew =  root.findViewById(R.id.projectList);
        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));
        projAdapter = new myProjectsAdapter(getActivity().getApplicationContext(),projList);
        myRecycleriew.setAdapter(projAdapter);
        return root;
    }
    private void getMyList() {
        proj = FirebaseDatabase.getInstance().getReference("Projects");
        for(int projID: projIDList) {
            Query projQuery = proj.orderByChild("projectID").equalTo(projID).limitToFirst(1);
            projQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot test : dataSnapshot.getChildren()) {
                            projList.add(test.getValue(Project.class));
                            projAdapter = new myProjectsAdapter(getActivity().getApplicationContext(), projList);
                            myRecycleriew.setAdapter(projAdapter);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}