package tim.hku.comp3330.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
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
import tim.hku.comp3330.R;
import tim.hku.comp3330.ui.login.LoginFragment;


public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private DB database;
    private InputValidation inputValidation;
    private User user = new User();
    private DatabaseReference databaseRef;
    private Boolean exist = false;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        database = new DB(context);
        inputValidation = new InputValidation(context);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init ViewModel
        registerViewModel = ViewModelProviders.of(requireActivity()).get(RegisterViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_registration, container, false);
        return root;

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        TextInputEditText loginEditText = view.findViewById(R.id.textInputEditTextLoginName);
        TextInputEditText pwEditText = view.findViewById(R.id.textInputEditTextPassword);
        TextInputEditText confirmEditText = view.findViewById(R.id.textInputEditTextConfirmPassword);
        Button regButton = view.findViewById(R.id.appCompatButtonRegister);
        TextInputLayout textInputLayoutLogin = view.findViewById(R.id.textInputLayoutLoginName);
        TextInputLayout textInputLayoutConfirm= view.findViewById(R.id.textInputLayoutConfirmPassword);
        TextInputLayout textInputLayoutPW= view.findViewById(R.id.textInputLayoutPassword);
        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);
        AppCompatTextView loginLink = view.findViewById(R.id.appCompatTextViewLoginLink);
        class Util{
            private void postDataToSQLite() {
                if (!inputValidation.isInputEditTextFilled(loginEditText, textInputLayoutLogin, getString(R.string.error_message_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(pwEditText, textInputLayoutPW, getString(R.string.error_message_password))) {
                    return;
                }
                if (!inputValidation.isInputEditTextMatches(pwEditText, confirmEditText,
                        textInputLayoutConfirm, getString(R.string.error_password_match))) {
                    return;
                }
                if (!exist) { // app stopping running this
                    user.setLoginName(loginEditText.getText().toString().trim());
                    user.setPassword(pwEditText.getText().toString().trim());
                    user.setUserName(loginEditText.getText().toString().trim()); // Username equals to login name by default
                    database.CreateUser(user);
                    String userRefID = databaseRef.push().getKey();
                    user.setUserID(userRefID);
                    databaseRef.child(userRefID).setValue(user);
                    // Snack Bar to show success message that record saved successfully
                    Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                    emptyInputEditText();


                } else {
                    // Snack Bar to show error message that record already exists
                    Snackbar.make(nestedScrollView, getString(R.string.error_login_exists), Snackbar.LENGTH_LONG).show();
                }
            }

            private void emptyInputEditText() {
                loginEditText.setText(null);
                pwEditText.setText(null);
                confirmEditText.setText(null);
            }
        }
        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerViewModel.setLogin(charSequence.toString());
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
                registerViewModel.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerViewModel.setConfirmPW(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query queryToGetData = databaseRef.orderByChild("loginName").equalTo(loginEditText.getText().toString().trim());
                queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            exist = false;
                        }
                        else{
                            exist = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                new Util().postDataToSQLite();
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController nav = NavHostFragment.findNavController(RegisterFragment.this);
                nav.navigate(R.id.nav_login );
            }
        });
    }
}