package com.hesham.sawarstudent.ui.subjects;

import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
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
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.DepartmentResponse;
import com.hesham.sawarstudent.data.response.SubjectResponse;
import com.hesham.sawarstudent.databinding.FragmentSecondTermBinding;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SecondTermFragment extends Fragment implements SubjectHomeAdapter.EventListener{

    private List<SubjectPojo> facultyPojos;
    private List<DepartmentPojo> depPojos;
    public Integer depId;
    private SubjectHomeAdapter facultySelectAdapter;

    PrefManager prefManager;

    private int years;

    public SecondTermFragment(int years) {
        this.years=years;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private FragmentSecondTermBinding binding;
    private SubjectFragment subjectFragment;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_second_term, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        facultyPojos = new ArrayList<>();
        depPojos= new ArrayList<>();
         subjectFragment=(SubjectFragment) getParentFragment();

        binding.departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (depPojos.size() != 0) {
                    depId = depPojos.get(i).getId();
                }
                subjectFragment.checkNotification(depId);
                getFilteredSubjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hideEmpty();

        prefManager=new PrefManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 2);
        binding.termRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new SubjectHomeAdapter(getContext(),this,facultyPojos,years,2);
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

    void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getAllDepartments();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }



    public void getFilteredSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getStudentData().getFacultyId(), years, 2, depId);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getFilteredSubjects(subjectPojo);
        binding.progressView.setVisibility(View.VISIBLE);

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
                    facultySelectAdapter = new SubjectHomeAdapter(getContext(),SecondTermFragment.this, facultyPojos ,years, 2);
                    binding.termRecyclerView.setAdapter(facultySelectAdapter);
                }
                binding.progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                binding.progressView.setVisibility(View.GONE);

            }
        });
    }
    public void getSubjects() {
        SubjectPojo subjectPojo=new SubjectPojo(prefManager.getCenterId() ,prefManager.getStudentData().getFacultyId(), years,2);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getAllSubjects(subjectPojo);
        binding.progressView.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body()!= null&& response.body().status  && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().cc_id);
                    if (facultyPojos.size()==0){
                        showEmpty();
                    }else {
                        hideEmpty();
                    }
                    facultySelectAdapter = new SubjectHomeAdapter(getContext(),SecondTermFragment.this,facultyPojos,years,2);
                    binding.termRecyclerView.setAdapter(facultySelectAdapter);
                }
                binding.progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                binding.progressView.setVisibility(View.GONE);

            }
        });
    }

    public void getAllDepartments() {//prefManager.getCenterId()
        Call<DepartmentResponse> call = Apiservice.getInstance().apiRequest.
                getAllDepartments(prefManager.getStudentData().getFacultyId());
        binding.progressView.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<DepartmentResponse>() {
            @Override
            public void onResponse(Call<DepartmentResponse> call, Response<DepartmentResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        depPojos.clear();
                        depPojos.addAll(response.body().cc_id);
                        if (depPojos.size() == 0) {
                            binding.departmentSpinner.setVisibility(View.GONE);
                            getSubjects();
                        } else {
                            binding.departmentSpinner.setVisibility(View.VISIBLE);
                            depId = depPojos.get(0).getId();
                        }
                        List<String> depList = new ArrayList<>(depPojos.size());
                        for (DepartmentPojo departmentPojo : depPojos) {
                            depList.add(departmentPojo.getName());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, depList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.departmentSpinner.setAdapter(dataAdapter);
                    }
                }
                binding.progressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DepartmentResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                binding.progressView.setVisibility(View.GONE);
            }
        });
    }




    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);

    }

    void showEmpty(){
        binding.emptyLayout.setVisibility(View.VISIBLE);


    }
    void hideEmpty(){
        binding.emptyLayout.setVisibility(View.GONE);

    }
}