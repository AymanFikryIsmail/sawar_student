package com.hesham.sawarstudent.ui.order;


import android.os.Bundle;

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
import com.hesham.sawarstudent.networkmodule.Apiservice;
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

    private RecyclerView facultyRecyclerView;
    private OrderHistoryAdapter facultySelectAdapter;
    PrefManager prefManager;
    TextView emptyLayout;
    private FrameLayout progress_view;

    private ImageView refresh;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        facultyRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        prefManager = new PrefManager(getContext());
        emptyLayout = view.findViewById(R.id.emptyLayout);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrders();
            }
        });

        hideEmpty();
        progress_view = view.findViewById(R.id.progress_view);

        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderHistoryAdapter(getContext(), facultyPojos, this);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getOrders();


        return view;
    }

    public void getOrders() {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.
                getMyOrders(prefManager.getStudentData().getId());
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                facultyPojos.clear();
                if (response.body()!=null &&response.body().status && response.body().data != null && response.body().data.size() != 0) {
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
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }else {
                    showEmpty();
                }
                progress_view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();

                progress_view.setVisibility(View.GONE);

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
        emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        emptyLayout.setVisibility(View.GONE);
    }

}
