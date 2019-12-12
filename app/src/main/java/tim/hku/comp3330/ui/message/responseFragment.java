package tim.hku.comp3330.ui.message;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class responseFragment extends Fragment {
    RecyclerView myRecycleriew;
    responseAdapter adapter;
    DB database;
    DatabaseReference msgRef;
    private ArrayList<Message>msgList = new ArrayList<>();
    private int count = 1;
    public responseFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new DB(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_response, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.responseRecyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));
        getMyList();
        adapter = new responseAdapter(getActivity(),msgList);
        myRecycleriew.setAdapter(adapter);


        return rootView;
    }
    private void getMyList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String userID = prefs.getString("userID","");
        msgRef = FirebaseDatabase.getInstance().getReference("Message");
        Query msgQuery = msgRef.orderByChild("receiverID").equalTo(userID);
        msgQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(count == 1) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Message msg = child.getValue(Message.class);
                        if(msg.isDeleted()){
                            msgList.add(msg);
                        }
                    }
                }
                adapter = new responseAdapter(getActivity(),msgList);
                myRecycleriew.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
