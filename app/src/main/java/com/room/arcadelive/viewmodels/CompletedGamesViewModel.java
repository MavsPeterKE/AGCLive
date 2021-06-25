package com.room.arcadelive.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.room.arcadelive.R;
import com.room.arcadelive.models.CompletedGame;
import com.room.arcadelive.view.adapters.CompletedGamesAdapter;

import java.util.List;

import javax.inject.Inject;

public class CompletedGamesViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();
    private CompletedGamesAdapter completedGamesAdapter;

    @Inject
    public CompletedGamesViewModel() {
        completedGamesAdapter = new CompletedGamesAdapter(R.layout.completed_game_item,this);
    }

    public CompletedGamesAdapter getCompletedGamesAdapter() {
        return completedGamesAdapter;
    }

    public void setGameList(List<CompletedGame> completedGameList) {
        completedGamesAdapter.setGameList(completedGameList);
    }
}
