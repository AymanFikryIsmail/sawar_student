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
import com.hesham.sawarstudent.data.model.DepartmentPojo;

import java.util.ArrayList;
import java.util.List;

public class FacultySelectAdapter extends RecyclerView.Adapter<FacultySelectAdapter.MyViewHolder> {
    private Context context;
    private List<DepartmentPojo> facultyPojos;
    EventListener eventListener;

    public FacultySelectAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public FacultySelectAdapter(Context context, EventListener eventListener, List<DepartmentPojo> facultyPojos) {
        this.context = context;
        this.eventListener = eventListener;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
    }

    @NonNull
    @Override
    public FacultySelectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_faultyselect, parent, false);
        return new FacultySelectAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultySelectAdapter.MyViewHolder holder, int position) {
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

        public void bind(final DepartmentPojo facultyPojo) {
            facultyname.setText(facultyPojo.getName());

            facultycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    eventListener.onChange(facultyPojo.getId(), b);
                }
            });
        }
    }

    public void updateList(List<DepartmentPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


    public interface EventListener {

        void onChange(int facultyId, boolean check);
    }
}

