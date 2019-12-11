package tim.hku.comp3330.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import tim.hku.comp3330.Account.InputValidation;
import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.MainActivity;
import tim.hku.comp3330.R;
import tim.hku.comp3330.projectList;
import tim.hku.comp3330.ui.home.HomeFragment;

public class LoginFragment extends Fragment{

    private LoginViewModel loginViewModel;
    private DB database;
    private InputValidation inputValidation;
    private Boolean userExists = false;
    private int userId;
    private DatabaseReference databaseRef;
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
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");
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
                    if (!inputValidation.isInputEditTextFilled(pwEditText, textInputLayoutLogin, getString(R.string.error_message_password))) {
                        return;
                    }
                    if (userExists) {
                        // TODO: start activity and load homepage for the user
                        emptyInputEditText();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("IsLogin",true);
                        editor.putInt("userID",userId);
                        editor.apply();
                        /*NavController nav = NavHostFragment.findNavController(LoginFragment.this);
                        nav.navigate(R.id.nav_myprojects );*/
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        startActivity(intent);
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
                Query loginNameQuery = databaseRef.orderByChild("loginName").equalTo(loginEditText.getText().toString().trim());
                loginNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            userExists = true;
                            //TODO:Fetch userId from DB
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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