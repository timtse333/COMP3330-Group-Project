package tim.hku.comp3330.ui.post;


import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import tim.hku.comp3330.DataClass.BlogPicture;
import tim.hku.comp3330.DataClass.ProgressPost;
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

    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    private Uri imageUri;


    public PostBlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_blog, container, false);
        postBtn = (Button) rootView.findViewById(R.id.postBtn);
        pictureBtn = (Button) rootView.findViewById(R.id.pictureBtn);
        projectList = (Spinner) rootView.findViewById(R.id.project);
        cancelBtn = (TextView) rootView.findViewById(R.id.cancelBtn);
        content = (EditText) rootView.findViewById(R.id.content);
        uploaded = (ImageView) rootView.findViewById(R.id.uploadImage);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        storageRef = FirebaseStorage.getInstance().getReference("blogPics");
        databaseRef = FirebaseDatabase.getInstance().getReference("blogPics");

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
            }
        });


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
                        BlogPicture picture = new BlogPicture(1,taskSnapshot.getUploadSessionUri().toString());
                        String pictureId = databaseRef.push().getKey();
                        databaseRef.child(pictureId).setValue(picture);

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
}
