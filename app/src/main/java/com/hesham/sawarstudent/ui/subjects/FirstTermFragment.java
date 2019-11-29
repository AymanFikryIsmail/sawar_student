package com.hesham.sawarstudent.ui.subjects;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.SubjectHomeAdapter;
import com.hesham.sawarstudent.data.model.DepartmentPojo;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.data.response.DepartmentResponse;
import com.hesham.sawarstudent.data.response.SubjectResponse;
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
public class FirstTermFragment extends Fragment implements SubjectHomeAdapter.EventListener{


    private List<SubjectPojo> facultyPojos;
    private List<DepartmentPojo> depPojos;
    private Integer depId;
    private RecyclerView facultyRecyclerView;
    private SubjectHomeAdapter facultySelectAdapter;

    PrefManager prefManager;
    private int years;
    TextView emptyLayout;
    private FrameLayout progress_view;
    private Spinner departmentSpinner;

    public FirstTermFragment(int years) {
        this.years = years;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_term, container, false);
        prefManager = new PrefManager(getContext());
        facultyRecyclerView = view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        depPojos= new ArrayList<>();
        emptyLayout=view.findViewById(R.id.emptyLayout);
        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (depPojos.size() != 0) {
                    depId = depPojos.get(i).getId();
                }
                getFilteredSubjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hideEmpty();
        progress_view = view.findViewById(R.id.progress_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new SubjectHomeAdapter(getContext(), this,facultyPojos,years, 1);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        if (NetworkUtilities.isOnline(getContext())) {
                if (NetworkUtilities.isFast(getContext())) {
                    getAllDepartments();
                }else {
                    Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
                }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }           return view;
    }


    public void getFilteredSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getStudentData().getFacultyId(), years, 1, depId);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getFilteredSubjects(subjectPojo);
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().cc_id);
                    if (facultyPojos.size()==0){
                        showEmpty();
                    }else {
                        hideEmpty();
                    }
                    facultySelectAdapter = new SubjectHomeAdapter(getContext(),FirstTermFragment.this, facultyPojos ,years, 1);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
                progress_view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                progress_view.setVisibility(View.GONE);

            }
        });
    }

    public void getSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getStudentData().getFacultyId(), years, 1);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getAllSubjects(subjectPojo);
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().cc_id);
                    if (facultyPojos.size()==0){
                        showEmpty();
                    }else {
                        hideEmpty();
                    }
                    facultySelectAdapter = new SubjectHomeAdapter(getContext(),FirstTermFragment.this, facultyPojos ,years, 1);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
                progress_view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                progress_view.setVisibility(View.GONE);

            }
        });
    }
    public void getAllDepartments() {//prefManager.getCenterId()
        Call<DepartmentResponse> call = Apiservice.getInstance().apiRequest.
                getAllDepartments(prefManager.getFacultyId());
        progress_view.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<DepartmentResponse>() {
            @Override
            public void onResponse(Call<DepartmentResponse> call, Response<DepartmentResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        depPojos.clear();
                        depPojos.addAll(response.body().cc_id);
                        if (depPojos.size() == 0) {
                            departmentSpinner.setVisibility(View.GONE);
                            getSubjects();
                        } else {
                            departmentSpinner.setVisibility(View.VISIBLE);
                            depId = depPojos.get(0).getId();
                        }
                        List<String> depList = new ArrayList<>(depPojos.size());
                        for (DepartmentPojo departmentPojo : depPojos) {
                            depList.add(departmentPojo.getName());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, depList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        departmentSpinner.setAdapter(dataAdapter);
                    }
                }
                progress_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DepartmentResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                progress_view.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);
    }
    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);


    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);

    }
}
