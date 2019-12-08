package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;


public class projectDetail extends Fragment {


    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    View view;
    MaterialTextView name;
    MaterialTextView description;
    AppCompatImageView background;
    DB database;

    public projectDetail() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_project_detail, container, false);
        database = new DB(getActivity());
        Project project = database.GetProject(getArguments().getInt("projID"));
        int image = view.getResources().getIdentifier(project.getProjectPic(),"drawable","tim.hku.comp3330");
        background = (AppCompatImageView) view.findViewById(R.id.project_image);
        background.setImageResource(image);
        name = (MaterialTextView) view.findViewById(R.id.project_name);
        name.setText(project.getProjectName());
        description = (MaterialTextView) view.findViewById(R.id.project_description);
        description.setText(project.getProjectDescription());
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_id);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbarid);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        //Pass ProjID to fragments
        FragmentProgress fragProgress = new FragmentProgress();
        Bundle bundle=new Bundle();
//        bundle.putInt("projID",project.getProjectID());
//        fragProgress.setArguments(bundle);

        adapter.addFragment(new FragmentProgress(),"Progress");
        adapter.addFragment(new FragmentBlog(),"Blog");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }



}
