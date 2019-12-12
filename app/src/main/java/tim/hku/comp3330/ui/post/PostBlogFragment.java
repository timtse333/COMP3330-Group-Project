package tim.hku.comp3330.ui.post;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tim.hku.comp3330.DataClass.BlogPicture;
import tim.hku.comp3330.DataClass.BlogPost;
import tim.hku.comp3330.DataClass.ProgressPost;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostBlogFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button postBtn;
    private Button pictureBtn;
    private Spinner projectList;
    private TextView cancelBtn;
    private DB database;
    private EditText content;
    private ImageView uploaded;
    private ProgressBar progressBar;
    private BlogPost blog;
    private int count;
    private String ownerID;
    private ArrayAdapter<Project> adapter;
    private TextWatcher checkPost;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private DatabaseReference blogRef;
    private DatabaseReference projectRef;
    private DatabaseReference relationRef;

    private Uri imageUri;


    public PostBlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_blog, container, false);
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
        postBtn = (Button) rootView.findViewById(R.id.postBtn);
        pictureBtn = (Button) rootView.findViewById(R.id.pictureBtn);
        projectList = (Spinner) rootView.findViewById(R.id.project);
        cancelBtn = (TextView) rootView.findViewById(R.id.cancelBtn);
        content = (EditText) rootView.findViewById(R.id.content);
        uploaded = (ImageView) rootView.findViewById(R.id.uploadImage);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        content.addTextChangedListener(checkPost);
        count = 0;
        blog = new BlogPost();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        ownerID = prefs.getString("userID","no");
        Log.d("myTag", "The owner id is "+ownerID);

        storageRef = FirebaseStorage.getInstance().getReference("blogPics");
        databaseRef = FirebaseDatabase.getInstance().getReference("blogPics");
        blogRef = FirebaseDatabase.getInstance().getReference("BlogPost");
        projectRef = FirebaseDatabase.getInstance().getReference("Projects");
        relationRef = FirebaseDatabase.getInstance().getReference("UserProjectRelation");

        getNewBlogId();
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openFileChooser();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(imageUri != null) {
                    uploadFile();
                }
                else {
                    postBlog();
                    Bundle bundle = new Bundle();
                    Navigation.findNavController(v).navigate(R.id.nav_home, bundle);
                }
            }
        });

        adapter = new ArrayAdapter<>(getActivity(), R.layout.project_drop_down_item, getProjectList());
        projectList.setAdapter(adapter);
        return rootView;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void getNewBlogId(){
        blogRef.addValueEventListener(new ValueEventListener() {
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

    private ArrayList<Project> getProjectList() {
        Log.d("myTag", "get Project List is called ");
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

    private void uploadFile() {
        StorageReference fileReference = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        },500);
                        int postId = count + 1;
                        BlogPicture picture = new BlogPicture(postId,taskSnapshot.getUploadSessionUri().toString());
                        String pictureId = databaseRef.push().getKey();
                        databaseRef.child(pictureId).setValue(picture);
                        Project project = (Project) projectList.getSelectedItem();
                        blog.setContent(content.getText().toString().trim());
                        blog.setProjectId(project.getProjectID());
                        blog.setBlogPostID(postId);
                        blog.setOwnerID(ownerID);
                        blog.setCreated(DateFormat.getDateTimeInstance().format(new Date()));
                        if(imageUri != null){
                            blog.setBlogPostPic("True");
                        }
                        String blogHash = blogRef.push().getKey();
                        blogRef.child(blogHash).setValue(blog);
                        Bundle bundle = new Bundle();
                        Navigation.findNavController(getView()).navigate(R.id.nav_home, bundle);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int)progress);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.with(getActivity()).load(imageUri).into(uploaded);
        }
    }

    private void postBlog() {

        Project project = (Project) projectList.getSelectedItem();
        blog.setContent(content.getText().toString().trim());
        blog.setProjectId(project.getProjectID());
        int postId = count + 1;
        blog.setBlogPostID(postId);
        blog.setOwnerID(ownerID);
        blog.setCreated(DateFormat.getDateTimeInstance().format(new Date()));
        if(imageUri != null){
            blog.setBlogPostPic("True");
        }
        String blogHash = blogRef.push().getKey();
        blogRef.child(blogHash).setValue(blog);

    }

    private Boolean checkReadiness() {
        if (projectList.getSelectedItem() != null){
            if (!content.getText().toString().matches("")) {
                return true;
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
