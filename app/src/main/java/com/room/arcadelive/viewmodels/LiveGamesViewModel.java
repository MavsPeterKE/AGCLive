package com.room.arcadelive.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.room.arcadelive.R;
import com.room.arcadelive.models.ScreenFirebaseModel;
import com.room.arcadelive.view.adapters.LiveGamesAdapter;

import java.util.List;

import javax.inject.Inject;

public class LiveGamesViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();
    private LiveGamesAdapter liveGamesAdapter;

    @Inject
    public LiveGamesViewModel() {
        liveGamesAdapter = new LiveGamesAdapter(R.layout.live_game_item,this);
    }

    public LiveGamesAdapter getLiveGamesAdapter() {
        return liveGamesAdapter;
    }

    public void setScreenList(List<ScreenFirebaseModel> screenList){
        liveGamesAdapter.screenList(screenList);
        liveGamesAdapter.notifyDataSetChanged();
    }
}
