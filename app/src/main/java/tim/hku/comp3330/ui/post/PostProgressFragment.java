package tim.hku.comp3330.ui.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;


public class PostProgressFragment extends Fragment {

    private Spinner projectList;
    private Button postBtn;
    private TextView cancelBtn;
    private DB database;
    private ProgressPost post;
    private EditText topic;
    private EditText content;
    private TextWatcher checkPost;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private DatabaseReference progressRef;
    private DatabaseReference projectRef;
    private DatabaseReference relationRef;
    ArrayAdapter<Project> adapter;
    private String ownerID;
    private int count;

   public PostProgressFragment() {
       // empty constructor
   }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_progress, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        count = 0;
        ownerID = prefs.getString("userID","no");
        Log.d("myTag", "OwnerID is " + ownerID);
        progressRef = FirebaseDatabase.getInstance().getReference("ProgressPost");
        projectRef = FirebaseDatabase.getInstance().getReference("Projects");
        relationRef = FirebaseDatabase.getInstance().getReference("UserProjectRelation");
        getNewProgressId();
        checkPost = new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postBtn.setEnabled(checkReadiness());
                if(checkReadiness()){
                    postBtn.setAlpha(1);
                }
                else {
                    postBtn.setAlpha((float) 0.2);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        topic = (EditText)rootView.findViewById(R.id.topic);
        topic.addTextChangedListener(checkPost);
        content = (EditText)rootView.findViewById(R.id.content);
        content.addTextChangedListener(checkPost);
        post = new ProgressPost();
        database = new DB(getActivity());
        projectList = (Spinner) rootView.findViewById(R.id.project);
        postBtn = (Button) rootView.findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){

                postProgress();
                Bundle bundle = new Bundle();
                Navigation.findNavController(view).navigate(R.id.nav_home, bundle);
            }
        });
        postBtn.setEnabled(false);
        cancelBtn = rootView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View view){
                Bundle bundle = new Bundle();
                Navigation.findNavController(view).navigate(R.id.nav_home,bundle);
            }
        });
        adapter = new ArrayAdapter<>(getActivity(), R.layout.project_drop_down_item, getProjectList());
        projectList.setAdapter(adapter);
        return rootView;
    }

    private void postDataToSQLite() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int userID = prefs.getInt("userID",1);
        Project project = (Project) projectList.getSelectedItem();
        post.setTitle(topic.getText().toString().trim());
        post.setContent(content.getText().toString().trim());
        post.setProjectId(project.getProjectID());
        post.setOwnerID(ownerID);
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        post.setCreated(dateFormat.format(currentTime));
        database.CreateNewPost(post);
        //Log.d("myTag", "The project id is "+project.getProjectID());


    }



    private ArrayList<Project> getProjectList() {
        Log.d("myTag", "get Project List is called ");
        Log.d("myTag", "OWNERID:" + ownerID);
        final ArrayList<Project> models = new ArrayList<>();
        relationRef.orderByChild("userID").equalTo(ownerID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String preChildKey) {
                final int searchkey = dataSnapshot.child("projectID").getValue(int.class);
                projectRef.orderByChild("projectID").equalTo(searchkey).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d("myTag", "getting from firebase");
                        Project project = new Project();
                        project.setOwnerID(dataSnapshot.child("ownerID").getValue(String.class));
                        project.setProjectID(dataSnapshot.child("projectID").getValue(int.class));
                        project.setProjectDescription(dataSnapshot.child("projectDescription").getValue().toString());
                        project.setProjectName(dataSnapshot.child("projectName").getValue().toString());
                        models.add(project);
                        Log.d("myTag", "The project id is "+project.getProjectName());
                        adapter.notifyDataSetChanged();
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
        return models;
    }

    private Boolean checkReadiness() {
       if (projectList.getSelectedItem() != null){
           if (!topic.getText().toString().matches("")) {
               if(!content.getText().toString().matches("")){
                   return true;
               }
               else{
                   return false;
               }
           }
           else {
               return false;
           }
       }
       else {
           return false;
       }
    }

    private void postProgress() {

        Project project = (Project) projectList.getSelectedItem();
        post.setTitle(topic.getText().toString().trim());
        post.setContent(content.getText().toString().trim());
        post.setProjectId(project.getProjectID());
        post.setOwnerID(ownerID);
        int id = count + 1;
        post.setProgressPostID(id);
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        post.setCreated(dateFormat.format(currentTime));
        String blogHash = progressRef.push().getKey();
        progressRef.child(blogHash).setValue(post);

    }

    private void getNewProgressId(){
        progressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    count++ ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
