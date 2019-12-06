package com.hesham.sawarstudent.ui.favourite;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.FavouriteHomeAdapter;
import com.hesham.sawarstudent.adapter.SubjectHomeAdapter;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.data.response.SubjectResponse;
import com.hesham.sawarstudent.databinding.FragmentFavouriteBinding;
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
public class FavouriteFragment extends Fragment implements FavouriteHomeAdapter.EventListener {


    private List<SubjectPojo> facultyPojos;

    private FavouriteHomeAdapter facultySelectAdapter;

    PrefManager prefManager;
    private int years;

    public FavouriteFragment() {
    }

    private FragmentFavouriteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_favourite, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        prefManager = new PrefManager(getContext());
        facultyPojos = new ArrayList<>();

        hideEmpty();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        binding.termRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new FavouriteHomeAdapter(getContext(), this, facultyPojos);
        binding.termRecyclerView.setAdapter(facultySelectAdapter);

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
                getFavourite();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    public void getFavourite() {//prefManager.getCenterId()
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.getFavourite(prefManager.getStudentData().getId());
        binding.progressView.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().cc_id);
                        if (facultyPojos.size() == 0) {
                            showEmpty();
                        } else {
                            hideEmpty();
                        }
                        facultySelectAdapter = new FavouriteHomeAdapter(getContext(), FavouriteFragment.this, facultyPojos);
                        binding.termRecyclerView.setAdapter(facultySelectAdapter);
                    }
                }
                binding.progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                binding.progressView.setVisibility(View.GONE);
                showEmpty();
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity) getActivity()).selectFavourite();
        ((HomeActivity) getActivity()).loadFragment(data);
    }

    @Override
    public void onCheckForEmpty() {
        checkForEmpty();
    }


    public void checkForEmpty() {
        if (facultyPojos.size() == 0) {
            showEmpty();
        } else {
            hideEmpty();
        }
    }

    void showEmpty() {
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        binding.emptyLayout.setVisibility(View.GONE);
    }
}
