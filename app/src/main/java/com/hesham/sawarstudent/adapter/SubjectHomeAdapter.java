package com.hesham.sawarstudent.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.ui.paper.PaperFragment;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class SubjectHomeAdapter extends RecyclerView.Adapter<SubjectHomeAdapter.MyViewHolder> {
    private Context context;
    private List<SubjectPojo> facultyPojos;
    private EventListener listener;

    PrefManager prefManager;

    //dialog
    Dialog dialog ;

    public SubjectHomeAdapter() {
        facultyPojos = new ArrayList<>();
    }
    int years, term;

    public SubjectHomeAdapter(Context context, EventListener listener, List<SubjectPojo> facultyPojos , int years , int term) {
        this.context = context;
        prefManager = new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.listener = listener;
        this.years=years;
        this.term=term;

    }

    @NonNull
    @Override
    public SubjectHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_termsubject, parent, false);
        return new SubjectHomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHomeAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView facultyname ;

        public MyViewHolder(View itemView) {
            super(itemView);

            facultyname = itemView.findViewById(R.id.facultyname);

        }

        public void bind(final SubjectPojo facultyPojo) {
            facultyname.setText(facultyPojo.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefManager.setSubjectId(facultyPojo.getId());
                    prefManager.setSubjectName(facultyPojo.getName());

                    PaperFragment yearsFragment = new PaperFragment();
                    listener.onEvent(yearsFragment);

                }
            });

        }
    }

    public interface EventListener {
        void onEvent(Fragment data);
    }

    public void updateList(List<SubjectPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }
}

