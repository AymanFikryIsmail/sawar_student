package com.hesham.sawarstudent.ui.order;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.OrderHistoryAdapter;
import com.hesham.sawarstudent.data.model.OrderPojo;
import com.hesham.sawarstudent.data.response.OrderResponse;
import com.hesham.sawarstudent.databinding.FragmentOrderBinding;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements OrderHistoryAdapter.EventListener {


    private List<OrderPojo> facultyPojos;
    private OrderHistoryAdapter facultySelectAdapter;
    PrefManager prefManager;

    public OrderFragment() {
        // Required empty public constructor
    }

    private FragmentOrderBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_order, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        prefManager = new PrefManager(getContext());
        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrders();
            }
        });

        hideEmpty();

        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        binding.ordersRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderHistoryAdapter(getContext(), facultyPojos, this);
        binding.ordersRecyclerView.setAdapter(facultySelectAdapter);

        binding.emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getOrders();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    public void getOrders() {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.
                getMyOrders(prefManager.getStudentData().getId());
        binding.progressView.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                facultyPojos.clear();
                if (response.body() != null && response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    if (facultyPojos.size() == 0) {
                        showEmpty();
                    } else {
                        hideEmpty();
                        prefManager.setOrderNumber(facultyPojos.size());
                    }
                    facultySelectAdapter = new OrderHistoryAdapter(getContext(), facultyPojos, OrderFragment.this);
                    binding.ordersRecyclerView.setAdapter(facultySelectAdapter);
                } else {
                    showEmpty();
                }
                binding.progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                binding.progressView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onRemove(List<OrderPojo> orderPojo) {
        if (orderPojo.size() == 0) {
            prefManager.setOrderCenterId(0);
            ((HomeActivity) getActivity()).clearNotificationOrderBadge();
            showEmpty();
        }
    }

    @Override
    public void onReceive() {
        ((HomeActivity) getActivity()).updatePoints();

    }

    @Override
    public void onResend() {
        getOrders();
    }

    void showEmpty() {
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        binding.emptyLayout.setVisibility(View.GONE);
    }

}
