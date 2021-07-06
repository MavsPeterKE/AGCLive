package com.room.arcadelive.viewmodels;

import android.text.format.DateFormat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.room.arcadelive.R;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.view.adapters.EndDayAdapter;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.room.arcadelive.utils.Constants.DATE_INDEX;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();

    @Inject
    public HomeViewModel() {

    }
}
