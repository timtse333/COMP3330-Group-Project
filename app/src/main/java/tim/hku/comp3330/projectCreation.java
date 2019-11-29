package tim.hku.comp3330;

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

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.Database.DB;

public class projectCreation extends AppCompatActivity {

    private final AppCompatActivity activity = projectCreation.this;
    private EditText projectName;
    private EditText projectDescription;
    private AppCompatButton createBtn;
    private DB database;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_creation);
        init_view();
        createBtn.setOnClickListener(new AppCompatButton.OnClickListener() {
            public void onClick(View view){
                postDataToSQLite();
            }
        }
    );
        initObjects();
    }

    private void init_view() {
        projectName = (EditText) findViewById(R.id.create_projectName);
        projectDescription = (EditText) findViewById(R.id.create_projectDescription);
        createBtn = (AppCompatButton) findViewById(R.id.create_projectBtn);

    }

    private void initObjects() {
        database = new DB(activity);
        project = new Project();

    }

    private void postDataToSQLite() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int userID = prefs.getInt("userID",1);
        project.setProjectName(projectName.getText().toString().trim());
        project.setProjectDescription(projectDescription.getText().toString().trim());
        project.setProjectPic("project_test"); // Default image for project
        project.setOwnerID(userID);
        database.CreateProject(project);

    }

}
