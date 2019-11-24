package tim.hku.comp3330.ui.home;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    public HomeFragment() {
        //empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        database = new DB(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new projectAdapter(getActivity(),getMyList());
        myRecycleriew.setAdapter(myAdapter);


        return rootView;
    }



    private ArrayList<Project> getMyList() {
        ArrayList<Project> models = new ArrayList<>();

        int total = database.GetProjectNum();

        for(int i = 1; i<(total + 1); i++){
            models.add(database.GetProject(i));
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

        return models;

    }

}