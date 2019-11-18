package com.hesham.sawarstudent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.hesham.sawarstudent.R;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    List<String> company;
    Context context;
    String title;

    public CustomSpinnerAdapter(Context context, List<String> company, String title) {
        this.company = company;
        this.context = context;
        this.title = title;
    }

    @Override
    public int getCount() {
        return company.size();
    }

    @Override
    public Object getItem(int position) {
        return company.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position == 0) {
            view = View.inflate(context, R.layout.spinner_title, null);
            TextView textView = (TextView) view.findViewById(R.id.dropdown);
            textView.setText(title);
        } else {
            view = View.inflate(context, R.layout.spinner_row, null);
            TextView textView = (TextView) view.findViewById(R.id.dropdown);
            textView.setText(company.get(position));
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position == 0) {
            view = View.inflate(context, R.layout.spinner_title, null);
            TextView textView = (TextView) view.findViewById(R.id.dropdown);
            textView.setText(title);
        } else {
            view = View.inflate(context, R.layout.spinner_row, null);
            TextView textView = (TextView) view.findViewById(R.id.dropdown);
            textView.setText(company.get(position));
        }

        return view;
    }
}
