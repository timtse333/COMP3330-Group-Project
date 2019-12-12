package tim.hku.comp3330;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Database.DBUtil;

public class projectCreation extends AppCompatActivity {

    private final AppCompatActivity activity = projectCreation.this;
    private EditText projectName;
    private EditText projectDescription;
    private AppCompatButton createBtn;
    private DB database;
    private DBUtil dbUtil;
    private Project project;
    private DatabaseReference databaseRef;
    private long projCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_creation);
        init_view();
        createBtn.setOnClickListener(new AppCompatButton.OnClickListener() {
            public void onClick(View view){
                get_data_from_db();
                postDataToSQLite();
            }
        }
    );
        initObjects();
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
    private void init_view() {
        projectName = (EditText) findViewById(R.id.create_projectName);
        projectDescription = (EditText) findViewById(R.id.create_projectDescription);
        createBtn = (AppCompatButton) findViewById(R.id.create_projectBtn);
        get_data_from_db();
    }

    private void initObjects() {
        database = new DB(activity);
        project = new Project();
        dbUtil = new DBUtil();
    }

    private void postDataToSQLite() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userID = prefs.getString("userID","");
        project.setProjectName(projectName.getText().toString().trim());
        project.setProjectDescription(projectDescription.getText().toString().trim());
        project.setProjectPic("project_test"); // Default image for project
        project.setOwnerID(userID);
        project.setProjectID((int)(projCount == 0? 1 : projCount));
        dbUtil.CreateProjectRecord(project);

    }

}
