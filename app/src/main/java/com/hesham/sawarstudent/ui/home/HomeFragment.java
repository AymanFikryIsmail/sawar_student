package com.hesham.sawarstudent.ui.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.CentersHomeAdapter;
import com.hesham.sawarstudent.adapter.OrderHistoryAdapter;
import com.hesham.sawarstudent.data.model.CenterPojo;
import com.hesham.sawarstudent.data.response.CenterResponse;
import com.hesham.sawarstudent.data.response.OrderResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.ui.order.OrderFragment;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements CentersHomeAdapter.EventListener{

    private List<CenterPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private CentersHomeAdapter facultySelectAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }
    private FrameLayout progress_view;


    PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        facultyRecyclerView=view.findViewById(R.id.centersRecyclerView);
        facultyPojos = new ArrayList<>();
        progress_view = view.findViewById(R.id.progress_view);

        prefManager=new PrefManager(getContext());
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new CentersHomeAdapter(getContext(),this,facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getCenters();
            }else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
        return view;
    }


    public void getCenters() {
        Call<CenterResponse> call = Apiservice.getInstance().apiRequest.
                getAllCenter(prefManager.getStudentData().getUniv(), prefManager.getStudentData().getFacultyId());
        progress_view.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CenterResponse>() {
            @Override
            public void onResponse(Call<CenterResponse> call, Response<CenterResponse> response) {
                if (response.body()!=null &&response.body().status  && response.body().data != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    facultySelectAdapter = new CentersHomeAdapter(getContext(), HomeFragment.this,facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
                progress_view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<CenterResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                progress_view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);
    }
}
