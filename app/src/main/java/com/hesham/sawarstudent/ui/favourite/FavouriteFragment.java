package com.hesham.sawarstudent.ui.favourite;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.FavouriteHomeAdapter;
import com.hesham.sawarstudent.adapter.SubjectHomeAdapter;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.data.response.SubjectResponse;
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
public class FavouriteFragment extends Fragment  implements FavouriteHomeAdapter.EventListener {


    private List<SubjectPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private FavouriteHomeAdapter facultySelectAdapter;

    PrefManager prefManager;
    private int years;
    TextView emptyLayout;
    private FrameLayout progress_view;

    public FavouriteFragment( ) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        prefManager = new PrefManager(getContext());
        facultyRecyclerView = view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        emptyLayout=view.findViewById(R.id.emptyLayout);
        progress_view = view.findViewById(R.id.progress_view);

        hideEmpty();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new FavouriteHomeAdapter(getContext(),this, facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getFavourite();
        return view;
    }


    public void getFavourite() {//prefManager.getCenterId()
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.getFavourite(prefManager.getStudentData().getId());
        progress_view.setVisibility(View.VISIBLE);
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
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                    }
                }
                progress_view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                progress_view.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).selectFavourite();
        ((HomeActivity)getActivity()).loadFragment(data);
    }

    @Override
    public void onCheckForEmpty() {
        checkForEmpty();
    }


    public  void checkForEmpty(){
        if (facultyPojos.size()==0){
            showEmpty();
        }else {
            hideEmpty();
        }
    }
    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);
    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);
    }
}
