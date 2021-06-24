package com.room.arcadelive.database.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "complete_games_table")
public class CompletedGame extends BaseObservable {
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "screen_lable")
    private String screenLable;

    @ColumnInfo(name = "duration")
    private String duration;

    @ColumnInfo(name = "games_count")
    private int gamesCount;

    @ColumnInfo(name = "end_time_seconds")
    private int endTimeSeconds;

    @ColumnInfo(name = "payable_amount")
    private double payableAmount;

    @ColumnInfo(name = "player_phone")
    private String playerPhone;

    @ColumnInfo(name = "bonus_amount")
    private double bonusAmount;

    @ColumnInfo(name = "date")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenLable() {
        return screenLable;
    }

    public void setScreenLable(String screenLable) {
        this.screenLable = screenLable;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(int gamesCount) {
        this.gamesCount = gamesCount;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getEndTimeSeconds() {
        return endTimeSeconds;
    }

    public void setEndTimeSeconds(int endTimeSeconds) {
        this.endTimeSeconds = endTimeSeconds;
    }

    public double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
