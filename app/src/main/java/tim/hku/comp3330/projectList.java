package tim.hku.comp3330;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;

public class projectList extends AppCompatActivity {
    RecyclerView myRecycleriew;
    projectAdapter myAdapter;
    DB database;
    private final AppCompatActivity activity = projectList.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);


        database = new DB(activity);
        myRecycleriew = findViewById(R.id.recyclerView);
        myRecycleriew.setLayoutManager(new LinearLayoutManager(this));

        myAdapter= new projectAdapter(this,getMyList());
        myRecycleriew.setAdapter(myAdapter);
    }

    private ArrayList<Project> getMyList() {
        ArrayList<Project> models = new ArrayList<>();

        int total = database.GetProjectNum();

        for(int i = 0; i<total; i++){
            models.add(database.GetProject(i));
        }

        return models;

    }
}
