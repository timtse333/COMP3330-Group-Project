package tim.hku.comp3330.ui.projectDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tim.hku.comp3330.R;

public class FragmentProgress extends Fragment {
    View view;

    public FragmentProgress() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.progress_fragment,container,false);
        return view;
    }
}
