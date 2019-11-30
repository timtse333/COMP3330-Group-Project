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

import java.util.ArrayList;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;

public class requestFragment extends Fragment {
    RecyclerView myRecycleriew;
    requestAdapter adapter;
    DB database;

    public requestFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request, container, false);
        myRecycleriew = (RecyclerView) rootView.findViewById(R.id.requestRecyclerView);

        myRecycleriew.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new requestAdapter(getActivity(),getMyList());
        myRecycleriew.setAdapter(adapter);


        return rootView;
    }
    private ArrayList<Message> getMyList() {
        ArrayList<Message> msgList = new ArrayList<Message>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int userID = prefs.getInt("userID",1);
        //msgList = database.GetAliveIncomingMessages(userID);
        return msgList;
    }
}
