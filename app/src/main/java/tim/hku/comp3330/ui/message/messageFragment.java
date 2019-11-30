package tim.hku.comp3330.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;
import tim.hku.comp3330.ui.projectDetails.ViewPagerAdapter;

public class messageFragment extends Fragment {

    private TabLayout msgTab;
    private ViewPager viewPager;
    DB database;

    public messageFragment(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_message, container, false);
        database = new DB(getActivity());
        msgTab = root.findViewById(R.id.msgTab);
        viewPager = root.findViewById(R.id.msgViewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new requestFragment(),"Request");
        adapter.addFragment(new responseFragment(), "Response");
        adapter.addFragment(new pendingFragment(), "Pending Request");
        viewPager.setAdapter(adapter);
        msgTab.setupWithViewPager(viewPager);
        return root;
    }
}