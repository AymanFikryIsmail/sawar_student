package com.hesham.sawarstudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.FacultyPojo;
import com.hesham.sawarstudent.ui.year.YearsFragment;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class FacultyHomeAdapter extends RecyclerView.Adapter<FacultyHomeAdapter.MyViewHolder> {
        private Context context;
        private List<FacultyPojo> facultyPojos;
    private EventListener listener;

    PrefManager prefManager;
    public FacultyHomeAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public FacultyHomeAdapter(Context context,EventListener listener, List<FacultyPojo> facultyPojos ) {
            this.context = context;
            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;
            this.listener=listener;
        }

        @NonNull
        @Override
        public FacultyHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_faultyhome, parent, false);
            return new FacultyHomeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FacultyHomeAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView facultyname;

            public MyViewHolder(View itemView) {
                super(itemView);

                facultyname = itemView.findViewById(R.id.facultyname);
            }

            public void bind(final FacultyPojo facultyPojo) {
                facultyname.setText(facultyPojo.getName());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefManager.setFacultyId(facultyPojo.getId());
                        YearsFragment yearsFragment = new YearsFragment(facultyPojo.getYears());
                        listener.onEvent(yearsFragment);
                    }
                });

            }
        }
    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<FacultyPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

