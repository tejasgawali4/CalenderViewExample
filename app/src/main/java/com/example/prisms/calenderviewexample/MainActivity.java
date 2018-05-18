package com.example.prisms.calenderviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://topschool.prisms.in/rest/index.php/user_list.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.ic_message_black_24dp));

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);

        events = LoadCalenderEvents();

        calendarView.setOnDayClickListener(eventDay ->
                Toast.makeText(getApplicationContext(),
                        eventDay.getCalendar().getTime().toString() + " "
                                + eventDay.isEnabled(),
                        Toast.LENGTH_SHORT).show());

    }

    private List<EventDay> LoadCalenderEvents() {

        ProgressBar progressBar = new ProgressBar(getApplicationContext() , null, android.R.attr.progressBarStyleSmall);
        progressBar.setVisibility(View.VISIBLE);
        List<EventDay> e = new ArrayList<>();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("fun_name", "getSharedCalendarDetails");
            jsonBody.put("sid", "90");
            jsonBody.put("u_id", "3794");
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

        return null;
    }
}
