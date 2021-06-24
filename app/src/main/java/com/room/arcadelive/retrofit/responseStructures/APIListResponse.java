package com.room.arcadelive.retrofit.responseStructures;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elish on 9/29/2017.
 */

public class APIListResponse<T> extends BaseModel {
    @SerializedName("data")
    public List<T> data = new ArrayList<T>();

    @SerializedName("errors")
    public Object strMessage = "";

    @SerializedName("success")
    public boolean success;

}