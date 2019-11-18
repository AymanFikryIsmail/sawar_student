package com.hesham.sawarstudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.FacultyPojo;

import java.util.ArrayList;
import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.MyViewHolder> {
        private Context context;
        private List<FacultyPojo> facultyPojos;

    public UniversityAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public UniversityAdapter(Context context, List<FacultyPojo> facultyPojos) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;
        }

        @NonNull
        @Override
        public UniversityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_faultyselect, parent, false);
            return new UniversityAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UniversityAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView facultyname;
            public CheckBox facultycheck;

            public MyViewHolder(View itemView) {
                super(itemView);

                facultyname = itemView.findViewById(R.id.facultyname);
                facultycheck = itemView.findViewById(R.id.facultycheck);
            }

            public void bind(final FacultyPojo facultyPojo) {
                facultyname.setText(facultyPojo.getName());

                facultycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                });
            }
        }

        public void updateList(List<FacultyPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

