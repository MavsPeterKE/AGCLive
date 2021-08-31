package com.room.arcadelive.viewmodels;

import static com.room.arcadelive.utils.Constants.InputError.PASSWORD_ERROR;
import static com.room.arcadelive.utils.Constants.InputError.USERNAME_ERROR;
import static com.room.arcadelive.utils.Constants.InputError.VALID_LOGIN;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.room.arcadelive.models.LoginModel;
import com.room.arcadelive.repository.UserRepository;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;

public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private UserRepository userRepository;
    public ObservableField<String> usernameMutableLiveData = new ObservableField<>();
    public ObservableField<String> errorTitle = new ObservableField<>();
    public ObservableField<String> errorMsg = new ObservableField<>();
    public ObservableField<String> passwordMutableLiveData = new ObservableField<>();
    public MutableLiveData<String> loginInputObservable = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> progressVisible = new MutableLiveData<>(false);
    public Application application;

    @Inject
    public LoginViewModel(@NonNull Application application, UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;
        this.application = application;
    }

    public void loginUser() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", "R6FqYEQYLVt7f18x9SAC2J9rGu6CIDsAnhIKtEL6");
        params.put("username", usernameMutableLiveData.get());
        params.put("password", passwordMutableLiveData.get());
        userRepository.startUserLoginApiRequest(params);
        updateProgressValue(true);
    }

    public String getAppVersion() {
        return Utils.getPackageInfo(application).versionName;
    }

    public void loginAction() {
        clickEventsLiveData.setValue(Constants.Events.LOGIN);
    }

    public void validateLoginInputs() {
        if (TextUtils.isEmpty(usernameMutableLiveData.get())) {
            loginInputObservable.setValue(USERNAME_ERROR);
        } else if (TextUtils.isEmpty(passwordMutableLiveData.get())) {
            loginInputObservable.setValue(PASSWORD_ERROR);
        } else {
            loginInputObservable.setValue(VALID_LOGIN);
        }
    }

    public PublishSubject<LoginModel> getLoginObervable() {
        return userRepository.getLoginSubject();
    }

    public void closeError() {
        clickEventsLiveData.setValue(Constants.Events.CLOSE_ERROR_SHEET);
    }

    public void updateProgressValue(boolean isVisible) {
        progressVisible.setValue(isVisible);
    }

    public void closeErrorBottomSheet() {
        clickEventsLiveData.setValue(Constants.Events.CLOSE_ERROR_SHEET);
    }
}
