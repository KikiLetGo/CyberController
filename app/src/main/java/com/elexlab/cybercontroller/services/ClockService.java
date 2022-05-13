package com.elexlab.cybercontroller.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by BruceYoung on 16/2/12.
 */
public class ClockService {
    private static final String TAG = "ClockService";
    private static ClockService instance;


    private static final long DIDA_TIME = 1000;
    private int day;
    private int hour;
    private int min;
    private TimeReceiver timeReceiver;
    private Context context;
    public ClockService(Context context){
        this.context = context;

        timeReceiver = new TimeReceiver();
        registerTimeReceiver();
    }




    private void timeTickTask(){
        int currentDay = Calendar.getInstance().getTime().getDay();
        int currentHour = Calendar.getInstance().getTime().getHours();
        int currentMin = Calendar.getInstance().getTime().getMinutes();

        if(currentDay!=day){
            dida.setNewDay(true);
            day = currentDay;
        }else{
            dida.setNewDay(false);
        }
        if(currentHour!=hour){
            dida.setNewHour(true);
            hour = currentHour;
        }else{
            dida.setNewHour(false);
        }
        if(currentMin!=min){
            dida.setNewMin(true);
            min = currentMin;
        }else{
            dida.setNewMin(false);
        }
        notifyClockObserver();
        //ThreadService.getInstance().getClockHandler().postDelayed(this,DIDA_TIME);
    }

    private class TimeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"TimeReceiver onReceive"+new SimpleDateFormat("yy/MM/DD hh:mm:ss").format(new Date()));
            timeTickTask();
        }
    }
    private void  registerTimeReceiver(){
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        context.registerReceiver(timeReceiver, filter);
    }
    //all observer
    private static Set<ClockObserverInterf> notifySet=new HashSet<ClockObserverInterf>();

    public synchronized void registClockObserver(ClockObserverInterf observer){
        notifySet.add(observer);
    }
    public synchronized void unregistClockObserver(ClockObserverInterf observer){
        notifySet.remove(observer);
    }


    private Dida dida = new Dida();
    //notify observers when the data changed
    public synchronized void notifyClockObserver(){
        Log.d(TAG,"dida");
        for(ClockObserverInterf observer:notifySet){
            if(!observer.dida(dida)) {
                break;
            }
        }
    }

    public void destroy(){
        context.unregisterReceiver(timeReceiver);

    }

    public static interface ClockObserverInterf{
        public boolean dida(Dida dida);

    }

    public static class Dida{
        private boolean newDay;
        private boolean newHour;
        private boolean newMin;

        public boolean isNewDay() {
            return newDay;
        }

        public void setNewDay(boolean newDay) {
            this.newDay = newDay;
        }

        public boolean isNewHour() {
            return newHour;
        }

        public void setNewHour(boolean newHour) {
            this.newHour = newHour;
        }

        public boolean isNewMin() {
            return newMin;
        }

        public void setNewMin(boolean newMin) {
            this.newMin = newMin;
        }
    }
}
