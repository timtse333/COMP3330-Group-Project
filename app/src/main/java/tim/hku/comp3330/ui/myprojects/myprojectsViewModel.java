package tim.hku.comp3330.ui.myprojects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class myprojectsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public myprojectsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myproject fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}