package com.example.erikn.enlewis_feelsbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    private int love_count, joy_count, surprise_count, sadness_count, anger_count, fear_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        for (Emotion emote : MainActivity.emotions) {
            if(emote.getType().equals("love")){
                love_count += 1;
            }
            if(emote.getType().equals("joy")){
                joy_count += 1;
            }
            if(emote.getType().equals("surprise")){
                surprise_count += 1;
            }
            if(emote.getType().equals("sadness")){
                sadness_count += 1;
            }
            if(emote.getType().equals("anger")){
                anger_count += 1;
            }
            if(emote.getType().equals("fear")){
                fear_count += 1;
            }
        }

        TextView love_count_text = (TextView) findViewById(R.id.love_count);
        love_count_text.setText(String.valueOf(love_count));
        TextView joy_count_text = (TextView) findViewById(R.id.joy_count);
        joy_count_text.setText(String.valueOf(joy_count));
        TextView surprise_count_text = (TextView) findViewById(R.id.surprise_count);
        surprise_count_text.setText(String.valueOf(surprise_count));
        TextView sadness_count_text = (TextView) findViewById(R.id.sadness_count);
        sadness_count_text.setText(String.valueOf(sadness_count));
        TextView anger_count_text = (TextView) findViewById(R.id.anger_count);
        anger_count_text.setText(String.valueOf(anger_count));
        TextView fear_count_text = (TextView) findViewById(R.id.fear_count);
        fear_count_text.setText(String.valueOf(fear_count));
    }
}
