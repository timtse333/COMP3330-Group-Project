package tim.hku.comp3330.ui.myprojects;

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

import tim.hku.comp3330.R;

public class myprojectsFragment extends Fragment {

    private myprojectsViewModel myprojectsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myprojectsViewModel =
                ViewModelProviders.of(this).get(myprojectsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myproject, container, false);

        return root;
    }
}