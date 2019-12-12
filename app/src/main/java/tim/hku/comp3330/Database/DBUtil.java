package tim.hku.comp3330.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import tim.hku.comp3330.DataClass.Project;

public class DBUtil {
    private DatabaseReference databaseRef;
    public DBUtil(){};
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
}
