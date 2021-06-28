package com.room.arcadelive.viewmodels;

import android.text.format.DateFormat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.room.arcadelive.R;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.models.ScreenFirebaseModel;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.view.adapters.EndDayAdapter;
import com.room.arcadelive.view.adapters.LiveGamesAdapter;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.room.arcadelive.utils.Constants.DATE_INDEX;

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

    public String getEndDayTime(String time,int itemIndex){
        String[] splitResult = time!=null?time.trim().split(" "):null;
        String result = "";
        if (splitResult!=null){
            if (splitResult.length>0){
                result = splitResult[itemIndex];
            }
        }
        return result;

    }

    public String getEndDayDate(String time){
        String result = "";
        if (time!=null){
            String dateString = getEndDayTime(time, DATE_INDEX);
            Date date = Utils.convertToDate(dateString,Constants.DATE_FORMAT);
            String monthString  = (String) DateFormat.format("MMM",  date); // Jun
            result = monthString+" "+dateString;
        }
        return result;
    }

    public String getEndDayTimeDetail(String time){
        String result = "";
        if (time!=null){
            String dateString = getEndDayTime(time, DATE_INDEX);
            Date date = Utils.convertToDate(dateString,Constants.DATE_FORMAT);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            result = dayOfTheWeek+" "+getEndDayTime(time,Constants.TIME_INDEX);
        }
        return result;
    }
}
