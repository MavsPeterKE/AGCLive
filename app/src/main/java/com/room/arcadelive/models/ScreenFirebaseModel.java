package com.room.arcadelive.models;

import androidx.room.Embedded;

public class ScreenFirebaseModel {
    public double payableAmount;
    public int totalGameCount;
    public Screen screen;
    public GameCount gameCount;
    public double bonusAmount;
    public GameType gameType;
}
