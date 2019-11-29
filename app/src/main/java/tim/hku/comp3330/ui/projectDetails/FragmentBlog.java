package tim.hku.comp3330.ui.projectDetails;

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

import tim.hku.comp3330.DataClass.BlogPost;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class FragmentBlog extends Fragment {
    RecyclerView myRecycleriew;
    BlogAdapter myAdapter;
    DB database;

    public FragmentBlog () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.blog_fragment, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new BlogAdapter(getActivity(),getMyList());
        myRecycleriew.setAdapter(myAdapter);


        return rootView;
    }

    private ArrayList<BlogPost> getMyList() {
        ArrayList<BlogPost> models = new ArrayList<>();

        BlogPost n = new BlogPost();
        n.setBlogPostID(2);
        n.setBlogPostPic("test1");
        n.setContent("Just want to say this to Jon Snow!");
        Date currentTime1 = Calendar.getInstance().getTime();
        DateFormat dateFormat1 = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        n.setCreated(dateFormat1.format(currentTime1));
        n.setOwnerID(1);
        n.setProjectId(1);
        models.add(n);

        BlogPost m = new BlogPost();
        m.setBlogPostID(1);
        m.setBlogPostPic("project_test");
        m.setContent("Have a good day with our new member Jon Snow. You guess what? He literally knows nothing.");
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        m.setCreated(dateFormat.format(currentTime));
        m.setOwnerID(1);
        m.setProjectId(1);
        models.add(m);


        return models;
    }
}
