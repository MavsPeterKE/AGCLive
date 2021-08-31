package com.room.arcadelive.repository;

import com.room.arcadelive.models.LoginModel;
import com.room.arcadelive.retrofit.RetrofitService;
import com.room.arcadelive.retrofit.responseStructures.LoginStructure;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Prefs;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class UserRepository {
    private PublishSubject<LoginModel> loginSubject;
    private CompositeDisposable disposable;
    private RetrofitService retrofitService;

    @Inject
    public UserRepository(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
        disposable = new CompositeDisposable();
    }

    public void startUserLoginApiRequest(HashMap<String, Object> loginParams) {
        loginSubject = PublishSubject.create();
        disposable.add(retrofitService.login(loginParams)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<LoginStructure>() {
                    @Override
                    public void onSuccess(LoginStructure userStructureAPIResponse) {
                        LoginModel loginResponseModel = getLoginResponseModel(userStructureAPIResponse);
                        Prefs.putString(Constants.PrefsKeys.ACCESS_TOKEN, "Bearer " + userStructureAPIResponse.accessToken);
                        loginSubject.onNext(loginResponseModel);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LoginModel loginResponseModel = new LoginModel();
                        loginResponseModel.message = e.getMessage();
                        loginResponseModel.loginStructure = null;
                        loginSubject.onNext(loginResponseModel);
                    }
                }));
    }

    @NotNull
    private LoginModel getLoginResponseModel(LoginStructure userStructureAPIResponse) {
        String errorMsg = userStructureAPIResponse.error != null ? userStructureAPIResponse.error : Constants.SUCCESS;
        LoginModel loginResponseModel = new LoginModel();
        loginResponseModel.message = errorMsg;
        loginResponseModel.loginStructure = userStructureAPIResponse;
        return loginResponseModel;
    }

    public PublishSubject<LoginModel> getLoginSubject() {
        return loginSubject;
    }
}
