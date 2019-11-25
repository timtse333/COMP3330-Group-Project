package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class FragmentProgress extends Fragment {
    RecyclerView myRecycleriew;
    ProgressAdapter myAdapter;
    DB database;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        database = new DB(context);
    }
    public FragmentProgress() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //database = new DB(getActivity());
        Bundle bundle = getArguments();
        View rootView = inflater.inflate(R.layout.progress_fragment, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new ProgressAdapter(getActivity(),database.GetPostsByProjectID(bundle.getInt("projID")));
        myRecycleriew.setAdapter(myAdapter);


        return rootView;
    }


    private ArrayList<ProgressPost> getMyList() {
        ArrayList<ProgressPost> models = new ArrayList<>();



        // Testing Data:

        ProgressPost o = new ProgressPost();
        o.setProgressPostID(1);
        o.setOwnerID(1);
        o.setTitle("Created Repository for the project!!!");
        o.setContent("Please checkout the github repo!!!");
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        o.setCreated(dateFormat.format(currentTime));
        models.add(o);

        ProgressPost n = new ProgressPost();
        n.setProgressPostID(1);
        n.setOwnerID(1);
        n.setTitle("Recruited the third member!");
        n.setContent("Nice to have Jon Snow joining our project! very cool!");
        Date currentTime1 = Calendar.getInstance().getTime();
        DateFormat dateFormat1 = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        n.setCreated(dateFormat1.format(currentTime1));
        models.add(n);

        ProgressPost m = new ProgressPost();
        m.setProgressPostID(1);
        m.setOwnerID(1);
        m.setTitle("Project Created!!!");
        m.setContent("This project is just created, very, very cool!");
        Date currentTime2 = Calendar.getInstance().getTime();
        DateFormat dateFormat2 = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        m.setCreated(dateFormat2.format(currentTime2));
        models.add(m);

        return models;

    }
}
