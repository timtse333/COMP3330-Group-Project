package tim.hku.comp3330.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> mLogin;
    private MutableLiveData<String> mPassword;
    public void setmLogin(String login){
        mLogin.setValue(login);
    }
    public LiveData<String> getmLogin() {
        return mLogin;
    }

    public void setmPassword(String password) {
        mPassword.setValue(password);
    }
    public MutableLiveData<String> getmPassword() {
        return mPassword;
    }
}