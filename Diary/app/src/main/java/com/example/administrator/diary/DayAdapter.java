package com.example.administrator.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class DayAdapter extends ArrayAdapter<Day> {
    private int resourceId;
    public DayAdapter(Context context, int textViewResourceId, List<Day> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Day day = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView daydetail = (TextView)view.findViewById(R.id.right);
        TextView dayweek = (TextView)view.findViewById(R.id.leftup);
        TextView dayday = (TextView)view.findViewById(R.id.leftbottom);
        daydetail.setText(day.getDetail());
        dayweek.setText(day.getWeek());
        dayday.setText(day.getDay());
        return view;
    }
}