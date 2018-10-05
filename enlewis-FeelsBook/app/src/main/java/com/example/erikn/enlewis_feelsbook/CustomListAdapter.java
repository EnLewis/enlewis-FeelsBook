package com.example.erikn.enlewis_feelsbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**************************************************************
 * CustomListAdapter
 * Author: Erik Lewis, 1516026
 * Desc: Display the custom list rows properly. This function was
 *        a pain to make.
 * Inputs:
 *   NONE
 * Outputs:
 *  NONE
 ***************************************************************/
public class CustomListAdapter extends ArrayAdapter {

    private int resourceLayout;
    private Context mContext;
    private final HashMap<String, Integer> map = new HashMap<String, Integer>();

    public CustomListAdapter(Context context, int resource, ArrayList<Emotion> emotions) {
        super(context, resource, emotions);
        this.resourceLayout = resource;
        this.mContext = context;

        //Setting up a hashmap here so that later on
        // when passing in emotions, I can use
        // their type to determine the right
        // icon to draw beside them.
        this.map.put("love",R.drawable.love);
        this.map.put("joy",R.drawable.joy);
        this.map.put("surprise",R.drawable.surprise);
        this.map.put("anger",R.drawable.anger);
        this.map.put("sadness",R.drawable.sadness);
        this.map.put("fear",R.drawable.fear);
    }

    //Creating each individual emotions personalized row
    // Fairly straightforward
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vinflater;
            vinflater = LayoutInflater.from(mContext);
            view = vinflater.inflate(resourceLayout, null);
        }

        Emotion toDisplay = (Emotion) getItem(position);

        if(toDisplay !=null){
            ImageView thumbnail = (ImageView) view.findViewById(R.id.emotion_row_thumbnail);
            TextView title = (TextView) view.findViewById(R.id.emotion_row_title);
            TextView date  = (TextView) view.findViewById(R.id.emotion_row_date);

            int icon = map.get(toDisplay.getType());

            if (thumbnail != null) {
                thumbnail.setImageResource(icon);
            }
            if (title != null) {
                title.setText(toDisplay.getType());
            }
            if (date != null) {
                date.setText(toDisplay.getDate());
            }
        }

        return view;
    }

}
