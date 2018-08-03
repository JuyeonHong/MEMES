package com.example.memes;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AccuTimeTab extends Fragment{

    private BarChart todayChart;
    private BarData barData;
    private Button btnReset;
    private Button btnRefresh;

    private PieChart yPieChart;
    private PieChart tPieChart;

    private TextView yesterdayVersusToday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accutime, container, false);

        yesterdayVersusToday = rootView.findViewById(R.id.yesterdayVersusToday2);
        int y = getYesterdayTime();
        int t = getNowTime();
        yesterdayVersusToday.setText("(" + y + " VS " + t + ")");

        //BarChart
        todayChart = rootView.findViewById(R.id.todayBarChart);
        todayChart.animateXY(3000, 3000);
        todayChart.invalidate();

        getBarData();

        btnReset = rootView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = getNowTime();
                RangeCount rangeCount;
                rangeCount = MainActivity.memesDatabase.rangeCountDao().getRecordByDate(time);
                rangeCount.setRange0_15(0);
                rangeCount.setRange15_30(0);
                rangeCount.setRange30_45(0);
                rangeCount.setRange45_60(0);
                rangeCount.setRange60_90(0);
                rangeCount.setRange90over(0);
                rangeCount.setSumOfAll(0);
                MainActivity.memesDatabase.rangeCountDao().updateCount(rangeCount);
                todayChart.animateXY(3000, 3000);
                todayChart.invalidate();
                getBarData();
                getTodayPieData();
            }
        });

        btnRefresh = rootView.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayChart.animateXY(3000, 3000);
                todayChart.invalidate();
                getBarData();
                getTodayPieData();
            }
        });

        //PieChart
        yPieChart = rootView.findViewById(R.id.yesterdayPieChart);
        tPieChart = rootView.findViewById(R.id.todayPieChart);

        getYesterdayPieData();
        getTodayPieData();

        return rootView;
    }

    private ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("0-15");
        labels.add("15-30");
        labels.add("30-45");
        labels.add("45-60");
        labels.add("60-90");
        labels.add("90-");

        return labels;
    }

    private int getYesterdayTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String date = sdf.format(calendar.getTime());
        int strToInt = Integer.parseInt(date);

        return strToInt;
    }

    private int getNowTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String getTime = sdf.format(date);
        int strToInt = Integer.parseInt(getTime);

        return strToInt;
    }

    private void getBarData() {
        ArrayList<String> barLabels = getLabels();
        int today = getNowTime();

        RangeCount rangeCount = MainActivity.memesDatabase.rangeCountDao().getRecordByDate(today);
        int range0_15 = rangeCount.getRange0_15();
        int range15_30 = rangeCount.getRange15_30();
        int range30_45 = rangeCount.getRange30_45();
        int range45_60 = rangeCount.getRange45_60();
        int range60_90 = rangeCount.getRange60_90();
        int range90over = rangeCount.getRange90over();
        int sum = rangeCount.getSumOfAll();

        float p0_15 = (range0_15/(float)sum)*100;
        float p15_30 = (range15_30/(float)sum)*100;
        float p30_45 = (range30_45/(float)sum)*100;
        float p45_60 = (range45_60/(float)sum)*100;
        float p60_90 = (range60_90/(float)sum)*100;
        float p90over = (range90over/(float)sum)*100;

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(p0_15, 0));
        entries.add(new BarEntry(p15_30, 1));
        entries.add(new BarEntry(p30_45, 2));
        entries.add(new BarEntry(p45_60, 3));
        entries.add(new BarEntry(p60_90, 4));
        entries.add(new BarEntry(p90over, 5));

        BarDataSet dataSet = new BarDataSet(entries, "오늘 거북목 상태");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barData = new BarData(barLabels, dataSet);
        todayChart.setData(barData);
    }

    private void getYesterdayPieData() {
        ArrayList<String> yLabels = getLabels();
        int yesterday = getYesterdayTime();


        RangeCount rangeCount = MainActivity.memesDatabase.rangeCountDao().getRecordByDate(yesterday);
        if(rangeCount != null) {
            float range0_15 = (float) rangeCount.getRange0_15();
            float range15_30 = (float) rangeCount.getRange15_30();
            float range30_45 = (float) rangeCount.getRange30_45();
            float range45_60 = (float) rangeCount.getRange45_60();
            float range60_90 = (float) rangeCount.getRange60_90();
            float range90over = (float) rangeCount.getRange90over();

            ArrayList<Entry> yEntries = new ArrayList<>();
            yEntries.add(new Entry(range0_15, 0));
            yEntries.add(new Entry(range15_30, 1));
            yEntries.add(new Entry(range30_45, 2));
            yEntries.add(new Entry(range45_60, 3));
            yEntries.add(new Entry(range60_90, 4));
            yEntries.add(new Entry(range90over, 5));

            PieDataSet yDataSet = new PieDataSet(yEntries, "어제 나의 자세");
            yDataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.CYAN, Color.LTGRAY});
            yDataSet.setValueTextSize(14f);
            PieData yData = new PieData(yLabels, yDataSet);
            yPieChart.setUsePercentValues(true);
            yPieChart.setDrawHoleEnabled(false);
            yPieChart.setData(yData);
            yPieChart.invalidate();
        }


    }

    private void getTodayPieData() {
        ArrayList<String> tLabels = getLabels();
        int now = getNowTime();

        RangeCount rangeCount = MainActivity.memesDatabase.rangeCountDao().getRecordByDate(now);
        float range0_15 = (float)rangeCount.getRange0_15();
        float range15_30 = (float)rangeCount.getRange15_30();
        float range30_45 = (float)rangeCount.getRange30_45();
        float range45_60 = (float)rangeCount.getRange45_60();
        float range60_90 = (float)rangeCount.getRange60_90();
        float range90over = (float)rangeCount.getRange90over();

        ArrayList<Entry> tEntries = new ArrayList<>();
        tEntries.add(new Entry(range0_15, 0));
        tEntries.add(new Entry(range15_30, 1));
        tEntries.add(new Entry(range30_45, 2));
        tEntries.add(new Entry(range45_60, 3));
        tEntries.add(new Entry(range60_90, 4));
        tEntries.add(new Entry(range90over, 5));

        PieDataSet tDataSet = new PieDataSet(tEntries, "오늘 나의 자세");
        tDataSet.setColors(new int[] {Color.RED, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.CYAN, Color.LTGRAY});
        tDataSet.setValueTextSize(14f);
        PieData tData = new PieData(tLabels, tDataSet);
        tPieChart.setUsePercentValues(true);
        tPieChart.setDrawHoleEnabled(false);
        tPieChart.setData(tData);
        tPieChart.invalidate();
    }
}
