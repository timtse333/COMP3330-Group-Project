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

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class myprojectsFragment extends Fragment {

    RecyclerView myRecycleriew;
    myProjectsAdapter projAdapter;
    DB database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = new DB(getActivity());
        View root = inflater.inflate(R.layout.fragment_myproject, container, false);
        myRecycleriew =  root.findViewById(R.id.projectList);
        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));
        projAdapter = new myProjectsAdapter(getActivity(),getMyList());
        myRecycleriew.setAdapter(projAdapter);
        return root;
    }
    private ArrayList<Project> getMyList() {
        ArrayList<Project> models = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int userID = prefs.getInt("userID",1);
        models = database.GetProjectByUserID(userID);
        return models;

    }
}