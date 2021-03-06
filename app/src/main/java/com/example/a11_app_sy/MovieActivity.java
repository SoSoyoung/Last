package com.example.a11_app_sy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MovieActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    DatePickerDialog.OnDateSetListener callbackMethod;

    static RequestQueue requestQueue;

    RecyclerView recyclerView;
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        this.InitalLizeeListenner();

        editText = findViewById(R.id.xeditText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.xbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });
        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateProcess(v);

            }
        });

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

    }
    public void InitalLizeeListenner()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String year1 = String.valueOf(year);
                String month1 = String.valueOf(month);
                String dayOfMonth1 = String.valueOf(dayOfMonth);
                String date = year1 + month1 + dayOfMonth1;
                editText.setText("https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=6411f3c4d1a0b83c3b67557a8a0b8efe&targetDt=" + date);
            }
        };
    }
    public void dateProcess(View v)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod,2021,12,10);
        dialog.show();
    }
    public void makeRequest(){
        String url = editText.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("?????? ->" + response);
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("?????? ->" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return  params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        println("?????? ??????.");
    }

    public void println(String data){
        Log.d("MovieActivity",data);
    }
    public void processResponse(String response)
    {
        Gson gson = new Gson();
        MovieList movieList =gson.fromJson(response, MovieList.class);
        println(("?????? ????????? ???: " + movieList.boxOfficeResult.dailyBoxOfficeList.size()));

        for(int i=0; i< movieList.boxOfficeResult.dailyBoxOfficeList.size();i++){
            Movie movie = movieList.boxOfficeResult.dailyBoxOfficeList.get(i);
            adapter.addItem(movie);
        }
        adapter.notifyDataSetChanged();
    }
}
