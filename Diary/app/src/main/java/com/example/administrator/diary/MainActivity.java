package com.example.administrator.diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btn1;
    private ImageButton btn2;
    private List<Day> daylist = new ArrayList<Day>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.true_activity_main);
        getSupportActionBar().hide();

        initDays();
        DayAdapter adapter = new DayAdapter(MainActivity.this,R.layout.day_item,daylist);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Day day=daylist.get(position);
                Intent intent=new Intent(MainActivity.this,WriteActivity.class);
                startActivity(intent);

            }
        });

        btn1 = (ImageButton) findViewById(R.id.add);
        btn2 = (ImageButton) findViewById(R.id.list);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    private void initDays()
    {
        Day day1 = new Day("SAT","1","我去超市来着...");
        daylist.add(day1);
        Day day2 = new Day("SUN","2","今天第一次和智恩吃意大利面。");
        daylist.add(day2);
        Day day3 = new Day("MON","3","路两边的树吸走了汽车尾气");
        daylist.add(day3);
        Day day4 = new Day("TUE","4","现在每天都能吃到猪排饭了，好开心啊");
        daylist.add(day4);
        Day day5 = new Day("FRI","7","你，黑咖啡，芝士年糕，羽毛球，成功");
        daylist.add(day5);
        Day day6 = new Day("SAT","8","DGA 7 最爱");
        daylist.add(day6);
        Day day7 = new Day("SAT","1","我去超市来着...");
        daylist.add(day1);
        Day day8 = new Day("SUN","2","今天第一次和智恩吃意大利面。");
        daylist.add(day2);
        Day day9 = new Day("MON","3","路两边的树吸走了汽车尾气");
        daylist.add(day3);
        Day day10 = new Day("TUE","4","现在每天都能吃到猪排饭了，好开心啊");
        daylist.add(day4);
        Day day11 = new Day("FRI","7","你，黑咖啡，芝士年糕，羽毛球，成功");
        daylist.add(day5);
        Day day12 = new Day("SAT","8","DGA 7 最爱");
        daylist.add(day6);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                Toast.makeText(MainActivity.this,"new diary",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,WriteActivity.class);
                startActivity(intent);
                break;
            case R.id.list:
                break;
            default:
                break;
        }
    }
}
