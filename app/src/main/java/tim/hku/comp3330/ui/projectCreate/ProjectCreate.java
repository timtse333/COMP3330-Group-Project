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
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;


public class ProjectCreate extends Fragment {
    private Button createBtn;
    private TextView cancelBtn;
    private Project project;
    private EditText name;
    private EditText description;
    private TextWatcher checkPost;
    private DatabaseReference databaseRef;
    private int count;
    private long projCount = 0;
    public ProjectCreate() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project_create, container, false);
        get_data_from_db();
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
        // count of existing project, for assigning id
        count = 0;
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
        databaseRef = FirebaseDatabase.getInstance().getReference("Project");
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                createProject();
                Bundle bundle = new Bundle();
                Navigation.findNavController(v).navigate(R.id.nav_home,bundle);
            }
        });

        return rootView;
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

    private void createProject() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String userID = prefs.getString("userID","");
        project.setProjectName(name.getText().toString().trim());
        project.setProjectDescription(description.getText().toString().trim());
        project.setProjectID((int)(projCount == 0? 1 : projCount));
        project.setOwnerID(userID);
        String projectHash = databaseRef.push().getKey();
        databaseRef.child(projectHash).setValue(project);
    }

}
