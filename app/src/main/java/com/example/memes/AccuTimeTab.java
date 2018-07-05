package com.example.memes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class AccuTimeTab extends Fragment{

    BarChart chart;
    ArrayList<BarEntry> BarEntry;
    ArrayList<String> BarEntryLabels;
    BarDataSet BarDataSet;
    BarData BarData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accutime, container, false);

        chart = rootView.findViewById(R.id.chart1);
        BarEntry = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        BarDataSet = new BarDataSet(BarEntry, "나의 하루 거북목 상태");
        BarData = new BarData(BarEntryLabels, BarDataSet);
        BarDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(BarData);
        chart.animateY(3000);

        return rootView;
    }

    public void AddValuesToBARENTRY(){
        BarEntry.add(new BarEntry(2f, 0));
        BarEntry.add(new BarEntry(4f, 1));
        BarEntry.add(new BarEntry(6f, 2));
        BarEntry.add(new BarEntry(8f, 3));
        BarEntry.add(new BarEntry(7f, 4));
        BarEntry.add(new BarEntry(3f, 5));
    }

    public void AddValuesToBarEntryLabels(){
        BarEntryLabels.add("0도~15도");
        BarEntryLabels.add("15도~30도");
        BarEntryLabels.add("30도~45도");
        BarEntryLabels.add("45도~60도");
        BarEntryLabels.add("60도~90도");
        BarEntryLabels.add("90도이상");
    }
}
