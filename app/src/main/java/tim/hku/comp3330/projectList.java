package tim.hku.comp3330;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;

public class projectList extends AppCompatActivity {
    RecyclerView myRecycleriew;
    projectAdapter myAdapter;
    DB database;
    private DatabaseReference databaseRef;
    private final AppCompatActivity activity = projectList.this;
    private long projCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        get_count_from_db();
        setContentView(R.layout.activity_project_list);
        myRecycleriew = findViewById(R.id.recyclerView);
        myRecycleriew.setLayoutManager(new LinearLayoutManager(this));

        myAdapter= new projectAdapter(getApplicationContext(),getMyList());
        myRecycleriew.setAdapter(myAdapter);
    }
    private void get_count_from_db(){
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    projCount = dataSnapshot.getChildrenCount() + 1;
                }
                else{
                    projCount += 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private ArrayList<DataSnapshot> get_projects_from_db(){
        ArrayList<DataSnapshot> projList = new ArrayList<DataSnapshot>();
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot proj : dataSnapshot.getChildren()){
                        projList.add(proj);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return projList;
    }
    private ArrayList<Project> getMyList() {
        ArrayList<Project> models = new ArrayList<>();

        ArrayList<DataSnapshot>projList = get_projects_from_db();
        for (DataSnapshot proj : projList) {
            models.add(proj.getValue(Project.class));
        }

        return models;

    }
}
