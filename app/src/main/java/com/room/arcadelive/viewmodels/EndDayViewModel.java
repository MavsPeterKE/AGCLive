package com.room.arcadelive.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.room.arcadelive.R;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.models.ScreenFirebaseModel;
import com.room.arcadelive.view.adapters.EndDayAdapter;
import com.room.arcadelive.view.adapters.LiveGamesAdapter;

import java.util.List;

import javax.inject.Inject;

public class EndDayViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();
    private EndDayAdapter endDayAdapter;

    @Inject
    public EndDayViewModel() {
        endDayAdapter = new EndDayAdapter(R.layout.end_day_item,this);
    }

    public EndDayAdapter getEndDayAdapter() {
        return endDayAdapter;
    }

    public void setEndDayList(List<EndDayModel> endDayViewModelList){
        endDayAdapter.setEndDayList(endDayViewModelList);
    }
}
