package tim.hku.comp3330.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import tim.hku.comp3330.R;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init ViewModel
        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_login, container, false);
        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText loginEditText = view.findViewById(R.id.textInputEditTextLogin);

        // Add Text Watcher on name input text
        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginViewModel.setmLogin(charSequence.toString());
            }

            @Override public void afterTextChanged(Editable editable) {
            }
        });
    }
}