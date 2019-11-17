package tim.hku.comp3330.Account;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.os.Bundle;

import java.util.List;

import tim.hku.comp3330.DataClass.Project;
import tim.hku.comp3330.MainActivity;
import tim.hku.comp3330.R;
import tim.hku.comp3330.Database.DB;
import tim.hku.comp3330.Account.InputValidation;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = Login.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextLogin;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextLogin = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }
    private void initObjects() {
        database = new DB(activity);
        inputValidation = new InputValidation(activity);
    }
    @Override
    public void onClick(View v) {
    }
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextLogin, textInputLayoutEmail, getString(R.string.error_message_login))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextLogin, textInputLayoutEmail, getString(R.string.error_message_login))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_login))) {
            return;
        }
        if (database.GetUserByLoginName(textInputEditTextLogin.getText().toString().trim()) != null) {
            // TODO: start activity and load homepage for the user
            List<Project> projList = database.GetProjectByUserID(database.GetUserByLoginName(textInputEditTextLogin.getText().toString().trim()).getUserID());
            emptyInputEditText();

        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_login_password), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextLogin.setText(null);
        textInputEditTextPassword.setText(null);

    }
}
