package com.hesham.sawarstudent.ui.year;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.YearHomeAdapter;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static com.hesham.sawarstudent.networkmodule.NetworkManager.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearsFragment extends Fragment  implements YearHomeAdapter.EventListener{

    private List<String> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private YearHomeAdapter facultySelectAdapter;
    private int years;
    private ArrayList<String> yearsNames;
    public YearsFragment(int years) {
        this.years=years;
        // Required empty public constructor
    }
    private ImageView centerimage , backarrowId;
    private TextView centerName;

    PrefManager prefManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yearsNames = new ArrayList<>();
        yearsNames.add("First year");
        yearsNames.add("Second year");
        yearsNames.add("Third year");
        yearsNames.add("Fourth year");
        if (years==5){
            yearsNames.add("Fifth year");
        }else if (years==6){
            yearsNames.add("Fifth year");
            yearsNames.add("Sixth year");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_years, container, false);
        facultyRecyclerView=view.findViewById(R.id.yearRecyclerView);
        prefManager=new PrefManager(getContext());

        centerimage=view.findViewById(R.id.centerimage);
        centerName=view.findViewById(R.id.centernameId);

        centerName.setText(prefManager.getCenterData().getName());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
        Glide.with(this).load(BASE_URL+prefManager.getCenterData().getLogo())
                .apply(requestOptions)
                .into(centerimage);
//        yearsNames.
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new YearHomeAdapter(getContext(),this,yearsNames);
        facultyRecyclerView.setAdapter(facultySelectAdapter);

        backarrowId=view.findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);

    }
}
