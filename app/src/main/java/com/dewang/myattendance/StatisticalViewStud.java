package com.dewang.myattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;

public class StatisticalViewStud extends AppCompatActivity {

    BarChart barChart;
    CircleProgressView pvDWM,pvHMI,pvPDS,pvML;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_view_stud);
        int subs[]= getIntent().getIntArrayExtra("lecs");

        barChart= (BarChart) findViewById(R.id.barChartLecs);
        pvDWM= (CircleProgressView) findViewById(R.id.pvDWM);
        pvHMI= (CircleProgressView) findViewById(R.id.pvHMI);
        pvPDS= (CircleProgressView) findViewById(R.id.pvPDS);
        pvML= (CircleProgressView) findViewById(R.id.pvML);

        //Y-axis data
        ArrayList<BarEntry> barEntries=new ArrayList<>();
        barEntries.add(new BarEntry(subs[0],0));
        barEntries.add(new BarEntry(subs[1],1));
        barEntries.add(new BarEntry(subs[2],2));
        barEntries.add(new BarEntry(subs[3],3));
        barEntries.add(new BarEntry(subs[4],4));
        barEntries.add(new BarEntry(subs[5],5));
        barEntries.add(new BarEntry(subs[6],6));
        barEntries.add(new BarEntry(subs[7],7));
        BarDataSet barDataSet=new BarDataSet(barEntries,"No. of Lectures");
        barDataSet.setColors(new int[]{R.color.red,R.color.dark_red,R.color.blue,R.color.dark_blue,R.color.green,R.color.dark_green,
                R.color.orange,R.color.dark_orange},getApplicationContext());

        //X-axis data
        ArrayList<String> subjects=new ArrayList<>();
        subjects.add("DWM");
        subjects.add("");
        subjects.add("HMI");
        subjects.add("");
        subjects.add("PDS");
        subjects.add("");
        subjects.add("ML");
        subjects.add("");

        BarData data=new BarData(subjects,barDataSet);
        barChart.setData(data);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.animateY(3000);
        barChart.setDescription("");
        barChart.setNoDataText("Unable to Retrieve data");
        Legend legend=barChart.getLegend();
        legend.setEnabled(false);

        int percentDWM=(int)((subs[0]/(float)subs[1])*100);
        pvDWM.setRimWidth(2);
        pvDWM.setBarWidth(20);
        pvDWM.setValueAnimated(percentDWM,2000);

        int percentHMI=(int)((subs[2]/(float)subs[3])*100);
        pvHMI.setRimWidth(2);
        pvHMI.setBarWidth(20);
        pvHMI.setValueAnimated(percentHMI,2000);

        int percentPDS=(int)((subs[4]/(float)subs[5])*100);
        pvPDS.setRimWidth(2);
        pvPDS.setBarWidth(20);
        pvPDS.setValueAnimated(percentPDS,2000);

        int percentML=(int)((subs[6]/(float)subs[7])*100);
        pvML.setRimWidth(2);
        pvML.setBarWidth(20);
        pvML.setValueAnimated(percentML,2000);

    }
}
