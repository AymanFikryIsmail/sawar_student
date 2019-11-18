package com.hesham.sawarstudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.CenterPojo;
import com.hesham.sawarstudent.ui.subjects.YearsFragment;
import com.hesham.sawarstudent.utils.PrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.hesham.sawarstudent.networkmodule.NetworkManager.BASE_URL;

public class CentersHomeAdapter extends RecyclerView.Adapter<CentersHomeAdapter.MyViewHolder> {
        private Context context;
        private List<CenterPojo>facultyPojos;
    private EventListener listener;

    PrefManager prefManager;
    public CentersHomeAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public CentersHomeAdapter(Context context, EventListener listener, List<CenterPojo> facultyPojos ) {
            this.context = context;
            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;
            this.listener=listener;
        }

        @NonNull
        @Override
        public CentersHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_centerhome, parent, false);
            return new CentersHomeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CentersHomeAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView facultyname ,facultyrate;
            ImageView centerLogo;

            public MyViewHolder(View itemView) {
                super(itemView);

                facultyname = itemView.findViewById(R.id.facultyname);
                facultyrate = itemView.findViewById(R.id.rate);

                centerLogo = itemView.findViewById(R.id.centerLogo);
            }

            public void bind(final CenterPojo facultyPojo) {
                facultyname.setText(facultyPojo.getName());
                facultyrate.setText(new DecimalFormat("##.#").format(facultyPojo.getRate()));

                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                        .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
                Glide.with(context).load( BASE_URL+facultyPojo.getLogo())
                        .apply(requestOptions)
                        .into(centerLogo);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefManager.setCenterId(facultyPojo.getId());
                        prefManager.setCenterData(facultyPojo);

                        YearsFragment yearsFragment = new YearsFragment(facultyPojo.getYears());
                        listener.onEvent(yearsFragment);
                    }
                });

            }
        }
    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<CenterPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

