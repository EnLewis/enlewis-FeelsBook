package com.example.erikn.enlewis_feelsbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**************************************************************
 * LogEmotionActivity
 * Author: Erik Lewis, 1516026
 * Desc: Handles the tracking and populating of
 *          individual emotions and the list they
 *          belong to.
 * Inputs:
 *   NONE
 * Outputs:
 *  NONE
 ***************************************************************/
public class LogEmotionActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    //This is a flag to determine whether an emotion
    // is being added or edited. I figured that the
    // processes are nearly identical when adding, or
    // editing so I used the flag so that I could reuse
    // a lot of code and be lazy.
    private Boolean eflag;
    private int position;
    private ArrayList<String> refArray = new ArrayList<String>();


    private String date;
    private String note = "note";
    private String user_emotion;

    private int year_x, month_x, day_x;
    private int hour_x,minute_x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_emotion);

        Button addButton = (Button) findViewById(R.id.add_emote);
        Button editButton = (Button) findViewById(R.id.edit_emote);
        Button deleteButton = (Button) findViewById(R.id.delet_emote);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //Indexing into a reference array that is identical in order
        // to the order of the spinner
        // was the only good way I could think of for displaying
        // the previously selected type when editing an emotion.
        //It's a uses a few unnecessary ressources but it works so
        // I'm fine with the small amount of extra code.
        eflag = extras.getBoolean(MainActivity.EXTRA_MESSAGE1, false);
        refArray.add("love");
        refArray.add("joy");
        refArray.add("surprise");
        refArray.add("sadness");
        refArray.add("anger");
        refArray.add("fear");

        if (!eflag) {
            //Display the appropriate buttons
            // this is my way of enforcing a difference between
            // editing mode and adding mode.
            addButton.setEnabled(true);
            editButton.setEnabled(false);
            editButton.setVisibility(View.GONE);
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.GONE);

            //Set default values for the date and time pickers
            final Calendar cal = Calendar.getInstance();
            year_x = cal.get(Calendar.YEAR);
            month_x = cal.get(Calendar.MONTH);
            day_x = cal.get(Calendar.DAY_OF_MONTH);
            hour_x = cal.get(Calendar.HOUR_OF_DAY);
            minute_x = cal.get(Calendar.MINUTE);

            user_emotion = "joy";

            //Setting up the spinner
            // I wish I could have come up with a good way to use the
            // string array here in place of the reference array I made
            // to determine the indices of the emotions on the spinner...
            //Oh well.
            Spinner spinner = (Spinner) findViewById(R.id.user_emote_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.emote_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        } else{
            //Enforcing the correct mode of operation again
            addButton.setEnabled(false);
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.GONE);

            position = extras.getInt(MainActivity.EXTRA_MESSAGE2,0);
            Emotion emoteToEdit = MainActivity.emotions.get(position);
            if (emoteToEdit != null){

                //Set the date and time picker values
                //If there is a better way of converting a
                // ISO8601 string into it's individual integer
                // components barring some gnarly Regex then I
                // clearly do not know it.
                String dateString = emoteToEdit.getDate();
                year_x = Integer.valueOf(dateString.substring(0,4));
                month_x = Integer.valueOf(dateString.substring(5,7));
                day_x = Integer.valueOf(dateString.substring(8,10));
                hour_x = Integer.valueOf(dateString.substring(11,13));
                minute_x = Integer.valueOf(dateString.substring(14,16));

                //Setting up spinner again
                Spinner spinner = (Spinner) findViewById(R.id.user_emote_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.emote_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(refArray.indexOf(emoteToEdit.getType()));
                spinner.setOnItemSelectedListener(this);

                //Display previous note value
                EditText noteET = (EditText) findViewById(R.id.note_edit_text);
                noteET.setText(emoteToEdit.getNote());
            }
        }
    }

    public void chooseDate(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,0,dateSetListener,
                year_x, month_x, day_x);
        datePickerDialog.show();
    }

    public void chooseTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,timeSetListener,
                hour_x,minute_x,true);
        timePickerDialog.show();
    }

    private  TimePickerDialog.OnTimeSetListener timeSetListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user_emotion = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        user_emotion = "joy"; //Cause everyone wants to be happy be default right?
    }

    //This function is kind of an island.
    //It doesn't take advantage of anything else in this function
    // since it doesn't need to. All it does IS DESTROY!!
    public void deleteEmotion(View view){
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.emotions.remove(position);
        Collections.sort(MainActivity.emotions);

        //For some reason finish() was causing me issues
        // so I found a way to destroy all the activities
        // above the main one and then start it up.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        saveInFile(); // I put saveInFile() here because when I had it in MainActivity
                      // the load action in OnStart() would load in the old list
                      // over the new one everytime I went back to the MainActivity.
                      //So I moved it here so it saves, switches Activities, then loads
                      // the saved list.
        startActivity(intent);
    }

    public void logEmotion(View view){
        Intent intent = new Intent(this, MainActivity.class);

        //What follows is a very verbose way of ensuring IS08601 formatting
        // because neither DateFormatter or GregorianCalender was saving the
        // integers with the correct amount of precision for some reason.
        String year_x_disp = String.format("%04d", year_x);
        String month_x_disp = String.format("%02d", month_x);
        String day_x_disp = String.format("%02d", day_x);
        String hour_x_disp = String.format("%02d", hour_x);
        String minute_x_disp = String.format("%02d", minute_x);

        date = year_x_disp + "-" + month_x_disp + "-" + day_x_disp + "T" + hour_x_disp + ":" + minute_x_disp + ":00";
        EditText noteET = (EditText) findViewById(R.id.note_edit_text);
        note = noteET.getText().toString();
        try {
            if (!eflag) {
                MainActivity.emotions.add(new Emotion(date, note, user_emotion));
            } else {
                Emotion toEdit = MainActivity.emotions.get(position);
                toEdit.setDate(date);
                toEdit.setNote(note);
                toEdit.setType(user_emotion);
            }
        } catch (NoteTooLongException e){
            //Chastise your user for breaking my rules
            // Bad User!
            Toast.makeText(this, "Your Note was too long, 100 chars only!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Collections.sort(MainActivity.emotions);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        saveInFile();
        startActivity(intent);
    }

    //Moved this function here from main because I didn't need it there
    // and I did need it here.
    public void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(MainActivity.FILENAME,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            Gson gson = new Gson();
            gson.toJson(MainActivity.emotions,osw);
            osw.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
