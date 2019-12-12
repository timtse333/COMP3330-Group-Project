package tim.hku.comp3330.ui.projectDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;
import java.util.Collections;

public class FragmentProgress extends Fragment {
    private RecyclerView myRecycleriew;
    private ProgressAdapter myAdapter;
    private DatabaseReference databaseRef;
    private int projectID;
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
        databaseRef = FirebaseDatabase.getInstance().getReference("ProgressPost");
        Bundle bundle = getArguments();
        projectID = bundle.getInt("projID");
        Log.d("myTag", "The project id is "+ projectID);
        View rootView = inflater.inflate(R.layout.progress_fragment, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));


        getMyList();

        return rootView;
    }


    private void getMyList() {
        final ArrayList<ProgressPost> models = new ArrayList<>();

//        databaseRef.orderByChild("projectId").equalTo(projectID).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("myTag", "Can see child");
//                ProgressPost post = dataSnapshot.getValue(ProgressPost.class);
//                Log.d("myTag", "Post: " + post.getTitle());
//                models.add(post);
//                myAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        databaseRef.orderByChild("projectId").equalTo(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    ProgressPost post = postSnapShot.getValue(ProgressPost.class);
                    Log.d("myTag", "Post: " + post.getTitle());
                    models.add(post);
                }
                Collections.reverse(models);
                myAdapter = new ProgressAdapter(getActivity(),models);
                myRecycleriew.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
