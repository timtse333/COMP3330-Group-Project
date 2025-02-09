package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Database.DBUtil;
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
    private String userID;
    private DatabaseReference databaseRef;
    private DBUtil dbUtil;
    Project proj = new Project();
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
        dbUtil = new DBUtil();
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        Query query = databaseRef.orderByChild("projectID").equalTo(getArguments().getInt("projID")).limitToFirst(1);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                userID = prefs.getString("userID","no");
                proj = dataSnapshot.getValue(Project.class);
                background = (AppCompatImageView) view.findViewById(R.id.project_image);
                Project project = proj;
                if(!project.getProjectPic().equals("project_test")){
                    Picasso.with(getContext())
                            .load(project.getProjectPic())
                            .fit()
                            .centerCrop()
                            .into(background);
                }
                else {
                    int image = view.getResources().getIdentifier(project.getProjectPic(), "drawable", "tim.hku.comp3330");
                    background.setImageResource(image);
                }
                name = (MaterialTextView) view.findViewById(R.id.project_name);
                name.setText(project.getProjectName());
                description = (MaterialTextView) view.findViewById(R.id.project_description);
                description.setText(project.getProjectDescription());
                tabLayout = (TabLayout) view.findViewById(R.id.tablayout_id);
                appBarLayout = (AppBarLayout) view.findViewById(R.id.appbarid);
                viewPager = (ViewPager) view.findViewById(R.id.viewpager_id);
                ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
                //set Project Pic
                if(project.getOwnerID().equals(userID)){
                    background.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("projID", project.getProjectID());
                            Navigation.findNavController(getView()).navigate(R.id.nav_update_blog_pic, bundle);
                        }
                    });
                }
                //Pass ProjID to fragments
                FragmentProgress fragProgress = new FragmentProgress();
                FragmentBlog fragBlog = new FragmentBlog();
                Bundle bundle = new Bundle();
                bundle.putInt("projID", project.getProjectID());
                fragProgress.setArguments(bundle);
                fragBlog.setArguments(bundle);
                adapter.addFragment(fragProgress, "Progress");
                adapter.addFragment(fragBlog, "Blog");
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }



}
