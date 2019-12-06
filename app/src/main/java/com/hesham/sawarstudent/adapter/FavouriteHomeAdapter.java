package com.hesham.sawarstudent.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.paper.PaperFragment;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteHomeAdapter extends RecyclerView.Adapter<FavouriteHomeAdapter.MyViewHolder> {
    private Context context;
    private List<SubjectPojo> facultyPojos;
    private EventListener listener;

    PrefManager prefManager;

    //dialog
    Dialog dialog ;

    public FavouriteHomeAdapter() {
        facultyPojos = new ArrayList<>();
    }
    int years, term;

    public FavouriteHomeAdapter(Context context ,EventListener listener, List<SubjectPojo> facultyPojos ) {
        this.context = context;
        prefManager = new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.listener = listener;

    }

    @NonNull
    @Override
    public FavouriteHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_favourite, parent, false);
        return new FavouriteHomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHomeAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView favname , remove ;

        public MyViewHolder(View itemView) {
            super(itemView);

            favname = itemView.findViewById(R.id.favname);
            remove = itemView.findViewById(R.id.removeFavourite);

        }

        public void bind(final SubjectPojo facultyPojo) {
            String lectutre=facultyPojo.getCategory();

            favname.setText(facultyPojo.getSub_name()+"/ "+lectutre);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeFavourite(facultyPojo);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    prefManager.setSubjectId();
                  //  removeFavourite(facultyPojo);
                    PaperFragment paperFragment = new PaperFragment(facultyPojo.getType());
                    prefManager.setSubjectId(facultyPojo.getSub_id());
//                    PaperFragment.newInstance(  facultyPojo.getType());
                    listener.onEvent(paperFragment);
                }
            });

        }
    }


    public void removeFavourite(final SubjectPojo subjectPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.removeFavourite(prefManager.getStudentData().getId() , subjectPojo.getSub_id()  ,subjectPojo.getType());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");
                    facultyPojos.remove(subjectPojo);
                    notifyDataSetChanged();
                    listener.onCheckForEmpty();
                }
            }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }

    public interface EventListener {
        void onEvent(Fragment data);
        void onCheckForEmpty();

    }


}

