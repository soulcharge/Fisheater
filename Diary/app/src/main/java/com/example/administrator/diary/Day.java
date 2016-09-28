package com.example.administrator.diary;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Day {
    private String day;
    private String week;
    private String detail;

    public Day(String week, String day, String detail)
    {
        this.day = day;
        this.week = week;
        this.detail = detail;
    }

    public String getDay()
    {
        return day;
    }

    public String getWeek()
    {
        return week;
    }

    public String getDetail()
    {
        return detail;
    }
}