package com.example.prisms.calenderviewexample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://topschool.prisms.in/rest/index.php/user_list.json";
    HashMap<String, ArrayList<EventBean>> eventHashMap = new HashMap<String, ArrayList<EventBean>>();
    HashMap<String, ArrayList<EventBean>> events = new HashMap<String, ArrayList<EventBean>>();
    ArrayList<EventBean> eventList = new ArrayList<>();
    ArrayList<EventBean> newList = new ArrayList<EventBean>();
    List<EventDay> event = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    CalendarView calendarView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        LoadCalenderEvents();

        calendarView = findViewById(R.id.calendarView);

        try {
            calendarView.setDate(calendar);
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
            String D = df.format(date);
            Date date1 = null;

            date1 = df.parse(D);
            calendarView.setDate(date1);

        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //calendarView.setOnDayClickListener(eventDay -> Toast.makeText(getApplicationContext(), eventDay.getCalendar().getTime().toString() + " " + eventDay.isEnabled(), Toast.LENGTH_SHORT).show());

        calendarView.setOnDayClickListener((EventDay eventDay) -> {

            Toast.makeText(getApplicationContext(), eventDay.getCalendar().getTime().toString() + " " + eventDay.isEnabled(), Toast.LENGTH_SHORT).show();

            LayoutInflater lInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View detailsView = lInflater.inflate(R.layout.calendar_event_details, null);
            dialog = new AlertDialog.Builder(MainActivity.this).create();
            dialog.setView(detailsView);
            ImageButton close = (ImageButton) detailsView.findViewById(R.id.close_popup_button);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {

            }
        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {

            }
        });
    }

    void LoadCalenderEvents() {

        ProgressBar progressBar = new ProgressBar(getApplicationContext() , null, android.R.attr.progressBarStyleSmall);
        progressBar.setVisibility(View.VISIBLE);
        List<EventDay> e = new ArrayList<>();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("fun_name", "getSharedCalendarDetails");
            jsonBody.put("sid", "329");
            jsonBody.put("u_id", "700");
        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //hiding the progressbar after completion
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("responce","" + jsonObject);
                try {
                    if (jsonObject != null) {
                        JSONObject jObject = new JSONObject(String.valueOf(jsonObject));
                        JSONArray jsonArray = jObject.getJSONArray("Share_Calendar_Details");
                        Log.d("log jsonArray",  "" + jsonArray);
                        JSONObject eventOject = jObject.getJSONObject("Shared_Calendar_Events_Details");
                        Log.d("log eventOject",  "" + eventOject);
                        JSONArray allSortedEventsArray = jObject.getJSONArray("ALL_SORTED_EVENTS");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                        ArrayList<EventBean> multipleEventList;

                        EventBean eventBeanObj = null;
                        multipleEventList = new ArrayList<EventBean>();

                        List<String> stringEventdateList = new ArrayList<String>();
                        for (int i = 0; i < allSortedEventsArray.length(); i++) {
                            JSONObject obj1 = null;
                            obj1 =  allSortedEventsArray.getJSONObject(i);
                            eventBeanObj = new EventBean();
                            eventBeanObj.setEventTitle(obj1.getString("title"));
                            eventBeanObj.setCalenderTitle(obj1.getString("cal_title"));
                            Date startDateObj = sdf.parse(obj1.getString("start_time"));
                            Date endDateObj = sdf.parse(obj1.getString("end_time"));
                            eventBeanObj.setEventDate(sdf.format(startDateObj));
                            eventBeanObj.setEventEndDate(sdf.format(endDateObj));
                            eventBeanObj.setId(Integer.parseInt("100"));
                            //here we add all events in the list
                            stringEventdateList.add(sdf.format(startDateObj));
                            multipleEventList.add(eventBeanObj);
                        }

                        for (int i = 0; i < multipleEventList.size() ; i++)
                        {
                            EventBean tempEvent = new EventBean();
                            tempEvent = multipleEventList.get(i);
                            String dat = tempEvent.getEventDate();
                            Log.d("Event Date String :- ","" + dat);
                            Date date = ConvertDate(dat);
                            Log.d("Event Date :- ","" + date);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(date);
                            event.add(new EventDay(calendar1,R.drawable.ic_message_black_24dp));
                        }
                        //calendarView = findViewById(R.id.calendarView);
                        calendarView.setEvents(event);

                        Set<String> hashsetList = new HashSet<String>(stringEventdateList);
                        stringEventdateList = new ArrayList<String>();
                        stringEventdateList.addAll(hashsetList);
                        //for(int i=0;i<hash)

                        for (int j = 0; j < stringEventdateList.size(); j++) {
                            newList = new ArrayList<EventBean>();
                            for (int k = 0; k < multipleEventList.size(); k++) {
                                if (stringEventdateList.get(j).equals(multipleEventList.get(k).getEventDate())) {
                                    newList.add(multipleEventList.get(k));
                                }
                            }
                            eventHashMap.put(stringEventdateList.get(j), newList);
                        }
                        Log.d("HASHMAP_VALUES", eventHashMap.size() + "");
                    }
                }
                catch(Exception e){
                        e.printStackTrace();
                }
            }

            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //displaying the error in toast if occurrs
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }});


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(jsonObjectRequest);

    }

    public Date ConvertDate(String d)
    {
        Date  date1 = null;
        @SuppressLint("SimpleDateFormat") DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = inputFormatter1.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }
}
