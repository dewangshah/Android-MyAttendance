package com.dewang.myattendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dewang on 3/25/2017.
 */

public class DefaulterAdapter extends ArrayAdapter {

    List list=new ArrayList<>();

    public DefaulterAdapter(Context context, int resource){
        super(context,resource);

    }

    public void add(Attendance object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        int agg;
        row=convertView;
        DefaulterAdapter.AttendanceHolder attendanceHolder;
        label:
        if(row==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row_layout,parent,false);
            attendanceHolder=new DefaulterAdapter.AttendanceHolder();
            attendanceHolder.tx_id= (TextView) row.findViewById(R.id.tx_id);
            attendanceHolder.tx_name= (TextView) row.findViewById(R.id.tx_name);
            attendanceHolder.tx_agg= (TextView) row.findViewById(R.id.tx_agg);
            row.setTag(attendanceHolder);
        }
        else
        {
            attendanceHolder= (DefaulterAdapter.AttendanceHolder) row.getTag();
        }
        Attendance attendance= (Attendance) this.getItem(position);
        attendanceHolder.tx_id.setText(Integer.toString(attendance.getId()));
        attendanceHolder.tx_name.setText(attendance.getName());
        attendanceHolder.tx_agg.setText(Integer.toString(attendance.getAgg()));
        return row;
    }

    static class  AttendanceHolder{
        TextView tx_id,tx_name,tx_agg;
    }
}