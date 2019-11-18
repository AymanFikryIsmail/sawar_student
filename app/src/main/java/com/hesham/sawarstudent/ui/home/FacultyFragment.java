package com.hesham.sawarstudent.ui.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.hesham.sawarstudent.adapter.FacultyHomeAdapter;
import com.hesham.sawarstudent.data.model.FacultyPojo;
import com.hesham.sawarstudent.data.response.FacultyResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hesham.sawarstudent.networkmodule.NetworkManager.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyFragment extends Fragment implements FacultyHomeAdapter.EventListener{

    private List<FacultyPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private FacultyHomeAdapter facultySelectAdapter;
    public FacultyFragment() {
        // Required empty public constructor
    }
    private ImageView centerimage;
    private TextView centerName;

    PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_faculty, container, false);
        facultyRecyclerView=view.findViewById(R.id.facultyRecyclerView);
        facultyPojos = new ArrayList<>();

        prefManager=new PrefManager(getContext());

        centerimage=view.findViewById(R.id.centerimage);
        centerName=view.findViewById(R.id.centernameId);

        centerName.setText(prefManager.getCenterData().getName());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
        Glide.with(this).load(BASE_URL+prefManager.getImageProfile())
                .apply(requestOptions)
                .into(centerimage);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 2);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new FacultyHomeAdapter(getContext(),this,facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getFaculties();
        return view;
    }




    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getHomeFaculties(prefManager.getCenterId());
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body().status  && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.addAll(response.body().cc_id);
                    facultySelectAdapter = new FacultyHomeAdapter(getContext(), FacultyFragment.this,facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);
    }
}
