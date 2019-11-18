package tim.hku.comp3330.ui.home;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    ImageView mImageView;
    TextView mTitle, mDes;
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Projects");
    }

    public LiveData<String> getText() {
        return mText;
    }
}