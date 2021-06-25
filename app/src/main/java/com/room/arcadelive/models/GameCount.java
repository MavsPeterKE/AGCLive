package com.room.arcadelive.models;

import androidx.databinding.BaseObservable;

public class GameCount extends BaseObservable {
    public long gameId;
    public String startTime;
    public String stopTime;
    public int startTimeMinutes;
    public int gamesCount;
    public int gamesBonus;
    public String playerNames;
    public String playerPhone;
    public long screenId;
    public long gameTypeId;
    public double payableAmount;
    public String hashKey;
}
