package tim.hku.comp3330.ui.post;


import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openFileChooser();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.with(getActivity()).load(imageUri).into(uploaded);
        }
    }
}
