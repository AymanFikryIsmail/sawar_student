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
import com.hesham.sawarstudent.ui.subjects.SubjectFragment;
import com.hesham.sawarstudent.ui.subjects.YearsFragment;

import java.util.ArrayList;
import java.util.List;

public class YearHomeAdapter extends RecyclerView.Adapter<YearHomeAdapter.MyViewHolder> {
        private Context context;
        private List<String> facultyPojos;
    private EventListener listener;

    public YearHomeAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public YearHomeAdapter(Context context, EventListener listener, List<String> facultyPojos ) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;
            this.listener=listener;
        }

        @NonNull
        @Override
        public YearHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_faultyhome, parent, false);
            return new YearHomeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull YearHomeAdapter.MyViewHolder holder, int position) {
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

            public void bind(final String facultyPojo) {
                facultyname.setText(facultyPojo);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SubjectFragment yearsFragment = new SubjectFragment(getAdapterPosition()+1);

                        listener.onEvent(yearsFragment);
                    }
                });

            }
        }
    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<String> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

