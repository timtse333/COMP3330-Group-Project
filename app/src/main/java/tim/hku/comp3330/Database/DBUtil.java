package tim.hku.comp3330.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.Project;

public class DBUtil {
    private DatabaseReference databaseRef;
    private DatabaseReference msgRef;
    public DBUtil(){};
    Project proj = new Project();
    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void CreateProjectRecord(Project project){
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        String projRefID = databaseRef.push().getKey();
        databaseRef.child(projRefID).setValue(project);
    }

    public void CreateMessage(Message msg){
        msgRef = FirebaseDatabase.getInstance().getReference("Message");
        String msgRefID = msgRef.push().getKey();
        msg.setMessageID(msgRefID);
        msgRef.child(msgRefID).setValue(msg);
    }
    public Project GetProjectByID(int projID){
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects");
        Query loginNameQuery = databaseRef.orderByChild("projectID").equalTo(projID).limitToFirst(1);
        loginNameQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                proj = dataSnapshot.getValue(Project.class);
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
        return proj;
    }

}
