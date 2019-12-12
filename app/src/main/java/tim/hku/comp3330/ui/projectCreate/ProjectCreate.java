package tim.hku.comp3330.ui.projectCreate;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.UserProjectRelation;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;
import tim.hku.comp3330.Database.DBUtil;


public class ProjectCreate extends Fragment {
    private Button createBtn;
    private TextView cancelBtn;
    private Project project;
    private EditText name;
    private EditText description;
    private TextWatcher checkPost;
    private DatabaseReference databaseRef;
    private DatabaseReference relationRef;
    private long projCount = 0;
    private DBUtil dbUtil;

    public ProjectCreate() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project_create, container, false);
        dbUtil = new DBUtil();
        checkPost = new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createBtn.setEnabled(checkReadiness());
                if(checkReadiness()){
                    createBtn.setAlpha(1);
                }
                else {
                    createBtn.setAlpha((float) 0.2);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        get_data_from_db();
        relationRef = FirebaseDatabase.getInstance().getReference("UserProjectRelation");
        project = new Project();
        name = (EditText)rootView.findViewById(R.id.name);
        name.addTextChangedListener(checkPost);
        description = (EditText)rootView.findViewById(R.id.description);
        description.addTextChangedListener(checkPost);
        createBtn = (Button) rootView.findViewById(R.id.createBtn);
        cancelBtn = rootView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View view){
                Bundle bundle = new Bundle();
                Navigation.findNavController(view).navigate(R.id.nav_home,bundle);
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                postDataToDB();
            }
        });

        return rootView;
    }

    private Boolean checkReadiness() {
            if (!name.getText().toString().matches("")) {
                if(!description.getText().toString().matches("")){
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



    private void get_data_from_db(){
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    projCount = dataSnapshot.getChildrenCount() + 1;
                }
                else{
                    projCount += 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postDataToDB() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userID = prefs.getString("userID","");
        project.setProjectName(name.getText().toString().trim());
        project.setProjectDescription(description.getText().toString().trim());
        project.setProjectPic("project_test"); // Default image for project
        project.setOwnerID(userID);
        project.setProjectID((int)(projCount == 0? 1 : projCount));
        dbUtil.CreateProjectRecord(project);
        UserProjectRelation relation = new UserProjectRelation();
        relation.setProjectID((int)(projCount == 0? 1 : projCount));
        relation.setUserID(userID);
        String relationHash = relationRef.push().getKey();
        relationRef.child(relationHash).setValue(relation);
        Bundle bundle = new Bundle();
        Navigation.findNavController(getView()).navigate(R.id.nav_home, bundle);

    }


}
