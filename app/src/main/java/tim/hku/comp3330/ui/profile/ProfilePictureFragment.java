package tim.hku.comp3330.ui.profile;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePictureFragment extends Fragment {
    private TextView username;
    private Button editBtn;
    private Button confirmBtn;
    private CircleImageView icon;
    private DatabaseReference userRef;
    private StorageReference storageRef;
    private User user;
    private String userID;

    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    public ProfilePictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_picture, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userID = prefs.getString("userID","no");

        storageRef = FirebaseStorage.getInstance().getReference("icon");

        username = (TextView) rootView.findViewById(R.id.username);
        editBtn = (Button) rootView.findViewById(R.id.editBtn);
        confirmBtn = (Button) rootView.findViewById(R.id.confirmBtn);
        confirmBtn.setEnabled(false);
        icon = (CircleImageView) rootView.findViewById(R.id.user_image);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openFileChooser();
            }
        });

        getDataFromDB();
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

    public void getDataFromDB () {
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("userID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    user = postSnapShot.getValue(User.class);
                }
                if (user.getIcon() != null) {
                    Picasso.with(getContext())
                            .load(user.getIcon())
                            .fit()
                            .centerCrop()
                            .into(icon);
                }
                username.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.with(getActivity()).load(imageUri).into(icon);
            confirmBtn.setAlpha(1);
            confirmBtn.setEnabled(true);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFile();
                }
            });
        }
    }

    private void uploadFile() {
        editBtn.setEnabled(false);
        editBtn.setAlpha((float)0.2);
        confirmBtn.setEnabled(false);
        confirmBtn.setAlpha((float)0.2);
        confirmBtn.setText("LOADING");
        StorageReference fileReference = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setProgress(0);
//                            }
//                        },500);
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        userRef.child(userID).child("icon").setValue(imageUrl);
                                        Bundle bundle = new Bundle();
                                        Navigation.findNavController(getView()).navigate(R.id.nav_home, bundle);
                                        //createNewPost(imageUrl);
                                    }
                                });
                            }
                        }


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
                        Toast.makeText(getActivity(),"Uploading",Toast.LENGTH_SHORT);
                    }
                });
    }

}
