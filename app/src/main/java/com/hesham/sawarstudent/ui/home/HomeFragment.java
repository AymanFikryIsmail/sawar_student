package com.hesham.sawarstudent.ui.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.CentersHomeAdapter;
import com.hesham.sawarstudent.adapter.OrderHistoryAdapter;
import com.hesham.sawarstudent.data.model.CenterPojo;
import com.hesham.sawarstudent.data.response.CenterResponse;
import com.hesham.sawarstudent.data.response.OrderResponse;
import com.hesham.sawarstudent.databinding.FragmentHomeBinding;
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
public class HomeFragment extends Fragment implements CentersHomeAdapter.EventListener {

    private List<CenterPojo> facultyPojos;

    private CentersHomeAdapter facultySelectAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    PrefManager prefManager;

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view= inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        facultyPojos = new ArrayList<>();
        hideEmpty();
        binding.emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        prefManager = new PrefManager(getContext());
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        binding.centersRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new CentersHomeAdapter(getContext(), this, facultyPojos);
        binding.centersRecyclerView.setAdapter(facultySelectAdapter);
        return view;
    }

    void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getCenters();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    public void getCenters() {
        Call<CenterResponse> call = Apiservice.getInstance().apiRequest.
                getAllCenter(prefManager.getStudentData().getUniv(), prefManager.getStudentData().getFacultyId(), prefManager.getStudentData().getId());
        binding.progressView.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CenterResponse>() {
            @Override
            public void onResponse(Call<CenterResponse> call, Response<CenterResponse> response) {
                if (response.body() != null && response.body().status && response.body().data != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    facultySelectAdapter = new CentersHomeAdapter(getContext(), HomeFragment.this, facultyPojos);
                    binding.centersRecyclerView.setAdapter(facultySelectAdapter);
                }
                binding.progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<CenterResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                binding.progressView.setVisibility(View.GONE);
                showEmpty();
            }
        });
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity) getActivity()).loadFragment(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    void showEmpty() {
        binding.emptyLayout.setVisibility(View.VISIBLE);


    }

    void hideEmpty() {
        binding.emptyLayout.setVisibility(View.GONE);

    }


}
