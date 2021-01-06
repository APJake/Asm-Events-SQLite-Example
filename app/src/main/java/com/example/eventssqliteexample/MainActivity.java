package com.example.eventssqliteexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fBtnAdd;
    List<MyEvent> myEvents;

    MyDBHelper dbHelper;
    RecyclerView rcyEvents;
    MyRcyAdapter rcyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fBtnAdd = findViewById(R.id.fb_add);
        rcyEvents=findViewById(R.id.rcy_events);
        dbHelper=new MyDBHelper(this);

        fBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEventDialog();
            }
        });

        dbRefresher();
    }

    private void dbRefresher() {
        myEvents=dbHelper.fetchEvents();
        rcyAdapter= new MyRcyAdapter(myEvents);
        registerForContextMenu(rcyEvents);
        rcyEvents.setHasFixedSize(true);
        rcyEvents.setLayoutManager(new LinearLayoutManager(this));
        rcyEvents.setAdapter(rcyAdapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 630:
                showEventDialog(false, dbHelper.getEvent(item.getGroupId()));
                break;
            case 631:
                dbHelper.removeEvent(item.getGroupId());
                dbRefresher();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private String dateToString(int dd, int mm, int yy){
        return ""+dd+"-"+(mm)+"-"+yy;
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        return dateToString(dd,mm+1,yy);
    }

    private void showEventDialog(){
        showEventDialog(true,null);
    }

    private void showEventDialog(boolean isNew, MyEvent myEvent) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_add);
        TextView tvTitle = dialog.findViewById(R.id.tv_event_top_title);
        EditText event_name = dialog.findViewById(R.id.edt_event_name);
        EditText event_desc = dialog.findViewById(R.id.edt_event_desc);
        EditText event_loc = dialog.findViewById(R.id.edt_event_loc);
        Button btnPickDate = dialog.findViewById(R.id.btn_event_pick_date);
        Button btnCancel = dialog.findViewById(R.id.btn_event_cancel);
        Button btnSave = dialog.findViewById(R.id.btn_event_save);

        btnPickDate.setText(getCurrentDate());

        if(!isNew) {
            tvTitle.setText(("Edit Event"));

            event_name.setText(myEvent.getName());
            event_desc.setText(myEvent.getDescription());
            event_loc.setText(myEvent.getLocation());
            btnPickDate.setText(myEvent.getDate());
        }else{
            myEvent=new MyEvent();
        }

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dateDialog = new Dialog(MainActivity.this);
                dateDialog.setContentView(R.layout.date_picker);
                DatePicker dp = dateDialog.findViewById(R.id.dp_event_date);
                Calendar c = Calendar.getInstance();
                int yy = c.get(Calendar.YEAR);
                int mm = c.get(Calendar.MONTH);
                int dd = c.get(Calendar.DAY_OF_MONTH);
                dp.init(yy, mm, dd, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btnPickDate.setText(dateToString(dayOfMonth, monthOfYear+1, year));
                        dateDialog.dismiss();
                    }
                });
                dateDialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        MyEvent finalMyEvent = myEvent;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String eName = event_name.getText().toString();
                final String eDesc = event_desc.getText().toString();
                final String eLoc = event_loc.getText().toString();
                final String eDate = btnPickDate.getText().toString();

                if(eName==null || eName.equals("")){
                    event_name.requestFocus();
                    Toast.makeText(MainActivity.this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                }
                else if(eDesc==null || eDesc.equals("")){
                    event_desc.requestFocus();
                    Toast.makeText(MainActivity.this, "Description can't be empty", Toast.LENGTH_SHORT).show();
                }
                else if(eLoc==null || eLoc.equals("")){
                    event_loc.requestFocus();
                    Toast.makeText(MainActivity.this, "Location can't be empty", Toast.LENGTH_SHORT).show();
                }
                else if(eDate==null || eDate.equals("")){
                    Toast.makeText(MainActivity.this, "Date can't be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    finalMyEvent.setName(eName);
                    finalMyEvent.setDescription(eDesc);
                    finalMyEvent.setDate(eDate);
                    finalMyEvent.setLocation(eLoc);
                    if(isNew) dbHelper.addEvent(finalMyEvent);
                    else dbHelper.editEvent(finalMyEvent);
                    dbRefresher();
                    dialog.dismiss();
                }
            }
        });

        dialog.setCancelable(false);

        dialog.show();
    }

}