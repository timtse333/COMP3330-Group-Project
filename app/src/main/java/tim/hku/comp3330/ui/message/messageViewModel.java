package tim.hku.comp3330.ui.message;

import android.media.Image;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class messageViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public messageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is message fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}