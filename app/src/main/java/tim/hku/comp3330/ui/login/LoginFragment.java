package tim.hku.comp3330.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import tim.hku.comp3330.Account.InputValidation;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.R;
import tim.hku.comp3330.projectList;
import tim.hku.comp3330.ui.home.HomeFragment;

public class LoginFragment extends Fragment{

    private LoginViewModel loginViewModel;
    private DB database;
    private InputValidation inputValidation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        database = new DB(context);
        inputValidation = new InputValidation(context);
    }

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
        TextInputEditText pwEditText = view.findViewById(R.id.textInputEditTextPassword);
        Button loginButton = view.findViewById(R.id.appCompatButtonLogin);
        TextInputLayout textInputLayoutLogin = view.findViewById(R.id.textInputLayoutLogin);
        TextInputLayout textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPassword);
        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);
        AppCompatTextView registerLink = view.findViewById(R.id.textViewLinkRegister);
            class Util{
                private void verifyFromSQLite() {
                    if (!inputValidation.isInputEditTextFilled(loginEditText, textInputLayoutLogin, getString(R.string.error_message_login))) {
                        return;
                    }
                    if (!inputValidation.isInputEditTextFilled(pwEditText, textInputLayoutPassword, getString(R.string.error_message_login))) {
                        return;
                    }
                    if (database.GetUserByLoginName(loginEditText.getText().toString().trim()) != null) {
                        // TODO: start activity and load homepage for the user
                        List<Project> projList = database.GetProjectByUserID(database.GetUserByLoginName(loginEditText.getText().toString().trim()).getUserID());
                        emptyInputEditText();
                        NavController nav = NavHostFragment.findNavController(LoginFragment.this);
                        nav.navigate(R.id.nav_myprojects );
                    } else {
                        // Snack Bar to show success message that record is wrong
                        Snackbar.make(nestedScrollView, getString(R.string.error_valid_login_password), Snackbar.LENGTH_LONG).show();
                    }
                }

                private void emptyInputEditText() {
                    loginEditText.setText(null);
                    pwEditText.setText(null);

                }
            }
        // Add Text Watcher on name input text
        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginViewModel.setLogin(charSequence.toString());
            }

            @Override public void afterTextChanged(Editable editable) {
            }
        });
        pwEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginViewModel.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Util().verifyFromSQLite();
            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController nav = NavHostFragment.findNavController(LoginFragment.this);
                nav.navigate(R.id.nav_registration );
            }
        });
    }


}