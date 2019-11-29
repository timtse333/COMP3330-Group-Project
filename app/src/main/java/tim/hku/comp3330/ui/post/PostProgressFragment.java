package tim.hku.comp3330.ui.post;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;


public class PostProgressFragment extends Fragment {

    Spinner projectList;
    Button postBtn;
    DB database;
    ProgressPost post;
    EditText topic;
    EditText content;
    int ProjectId;

   public PostProgressFragment() {
       // empty constructor
   }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_progress, container, false);
        topic = (EditText)rootView.findViewById(R.id.topic);
        content = (EditText)rootView.findViewById(R.id.content);
        post = new ProgressPost();
        database = new DB(getActivity());
        Project m = database.GetProject(1);
        ArrayList<Project> projects = new ArrayList<>();
        projectList = (Spinner) rootView.findViewById(R.id.project);
        postBtn = (Button) rootView.findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                postDataToSQLite();
            }
        });
        projects.add(m);
        ArrayAdapter<Project> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, projects);
        projectList.setAdapter(adapter);
        return rootView;
    }

    private void postDataToSQLite() {
        Project project = (Project) projectList.getSelectedItem();
        post.setTitle(topic.getText().toString().trim());
        post.setContent(content.getText().toString().trim());
        post.setProjectId(project.getProjectID());
        post.setOwnerID(1);
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        post.setCreated(dateFormat.format(currentTime));
        database.CreateNewPost(post);
        //Log.d("myTag", "The project id is "+project.getProjectID());


    }
}
