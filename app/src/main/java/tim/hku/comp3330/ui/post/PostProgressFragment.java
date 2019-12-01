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

    Spinner projectList;
    Button postBtn;
    TextView cancelBtn;
    DB database;
    ProgressPost post;
    EditText topic;
    EditText content;
    TextWatcher checkPost;
    int ProjectId;

   public PostProgressFragment() {
       // empty constructor
   }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_progress, container, false);
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

                postDataToSQLite();
                Bundle bundle = new Bundle();
                Navigation.findNavController(view).navigate(R.id.nav_home,bundle);
                Log.d("checkButton","The result is" + checkReadiness());
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
        ArrayAdapter<Project> adapter = new ArrayAdapter<>(getActivity(), R.layout.project_drop_down_item, getMyList());
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
        post.setOwnerID(userID);
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        post.setCreated(dateFormat.format(currentTime));
        database.CreateNewPost(post);
        //Log.d("myTag", "The project id is "+project.getProjectID());


    }

    private ArrayList<Project> getMyList() {
        ArrayList<Project> models = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int userID = prefs.getInt("userID",1);
        models = database.GetProjectByUserID(userID);
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
}
