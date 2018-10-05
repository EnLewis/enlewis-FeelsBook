package com.example.erikn.enlewis_feelsbook;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

/**************************************************************
 * MainActivity
 * Author: Erik Lewis, 1516026
 * Desc: Handles displaying the listview and holds
 *          most of the important structures for the app.
 * Inputs:
 *   NONE
 * Outputs:
 *  NONE
 ***************************************************************/
public class MainActivity extends AppCompatActivity{
    public static final String EXTRA_MESSAGE1 = "com.example.erikn.enlewis____feelsbook.EFLAG";
    public static final String EXTRA_MESSAGE2 = "com.example.erikn.enlewis____feelsbook.POS";

    public static final String FILENAME = "emo_list_fd.sav";

    public static ArrayList<Emotion> emotions = new ArrayList<Emotion>();
    private String[] emote_dialog_options = {"love", "joy", "surprise", "sadness", "anger", "fear"};
    private CustomListAdapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Displaying the listview which is the central
        // and most efficient way of displying the
        // users emotions.
        loadFromFile();
        listView = (ListView) findViewById(R.id.emotion_list);

        adapter = new CustomListAdapter(this, R.layout.listview_row, emotions);
        listView.setAdapter(adapter);

        //The onclick for the individual list items is here because
        // I can't add a onClick attribute to it in xml. So since I don't know
        // where else to put it otherwise, here it is. Hurts consistency
        // but it works.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,LogEmotionActivity.class);
                intent.putExtra(EXTRA_MESSAGE1, true);
                intent.putExtra(EXTRA_MESSAGE2, position);
                startActivity(intent);

                //Make sure the emotions are sorted in order of Date
                Collections.sort(emotions);
                adapter.notifyDataSetChanged();
            }
        });

    }

    //I put the summary in a menu because I made a sliding tab view for it
    // but it broke everything so I decided an afterthought of a feature
    // like this deserved to be relegated to what in most apps is used for
    // settings of app info.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater minflater = getMenuInflater();
        minflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This was meant to be encapsulated in another function
    // like addItem but again the xml wouldn't let me...
    // so I put it here.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.summary:
                Intent intent = new Intent(this, SummaryActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //This function is here to ensure that the whole list is displayed
    // from last session when the app is opened.
    @Override
    protected void onStart () {
        super.onStart();
        loadFromFile();
    }

    //Very simply stashes emotion data in JSON so that it can
    // persists between sessions.
    private void loadFromFile () {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            Gson gson = new Gson();
            Type typeListTweets = new TypeToken<ArrayList<Emotion>>() {
            }.getType();
            emotions = gson.fromJson(reader, typeListTweets);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Start the process of adding a user emotion by
    // switching process'. Notice SaveInFile() is
    // missing. Go look for it and the explanation as
    // to why it's there in LogEmotionActivity.
    public void addItem (View view){
        Intent intent = new Intent(this, LogEmotionActivity.class);
        intent.putExtra(EXTRA_MESSAGE1, false);
        startActivity(intent);

        Collections.sort(emotions);
        adapter.notifyDataSetChanged();
    }
    }

