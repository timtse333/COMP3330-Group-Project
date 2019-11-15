package tim.hku.comp3330;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class projectList extends AppCompatActivity {
    RecyclerView myRecycleriew;
    projectAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        myRecycleriew = findViewById(R.id.recyclerView);
        myRecycleriew.setLayoutManager(new LinearLayoutManager(this));

        myAdapter= new projectAdapter(this,getMyList());
        myRecycleriew.setAdapter(myAdapter);
    }

    private ArrayList<projectModel> getMyList() {
        ArrayList<projectModel> models = new ArrayList<>();

        projectModel m = new projectModel();
        m.setTitle("project 1");
        m.setDescription("interesting project");
        m.setImg(R.drawable.project_test);
        models.add(m);

        projectModel n = new projectModel();
        n.setTitle("project 2");
        n.setDescription("cool project");
        n.setImg(R.drawable.project_test);
        models.add(n);

        projectModel o = new projectModel();
        o.setTitle("project 3");
        o.setDescription("interesting project");
        o.setImg(R.drawable.project_test);
        models.add(o);

        return models;

    }
}
