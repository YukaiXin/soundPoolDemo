package com.example.soundpooldemo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter {

    /**
     * Create song list adapter
     *
     * @param activity : Which activity the adapter is relative
     * @param resource : Which view the adapter is relative
     * @param songList : What is the data
     * @return
     */
    public static ArrayAdapter songListAdapter(Activity activity, int resource, final List<Object> songList) {

        return new ArrayAdapter<Object>(activity, resource, songList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#FF999999"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.NORMAL);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                item.setTextColor(Color.YELLOW);
                Object o = songList.get(position);
                if(String.class.isInstance(o)){
                    if(!((String)o).contains("sound/")){
                        item.setTextColor(Color.BLUE);
                        item.setText("文件： "+songList.get(position));
                    }else {
                        item.setTextColor(Color.RED);

                        item.setText("Assets： "+songList.get(position));
                    }
                }

                return item;
            }
        };
    }
}