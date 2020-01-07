package com.hesham.sawarstudent.ui.paper;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.CategoryAdapter;
import com.hesham.sawarstudent.adapter.PaperHomeAdapter;
import com.hesham.sawarstudent.data.model.CategoryPojo;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.response.CategoryResponse;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.PaperResponse;
import com.hesham.sawarstudent.databinding.FragmentPaperBinding;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperFragment extends Fragment implements PaperHomeAdapter.EventListener, CategoryAdapter.EventListener {
    public static PaperFragment newInstance(int paperType) {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        args.putInt("paperType", paperType);
        fragment.setArguments(args);
        return fragment;
    }

    public PaperFragment(int paperType) {
        this.paperCategory = paperType;
    }

    public PaperFragment() {
    }

    private List<PaperPojo> facultyPojos;

    private PaperHomeAdapter facultySelectAdapter;
    PrefManager prefManager;
    private int paperCategory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paperCategory = getArguments().getInt("papeType");
        }
    }

    private FragmentPaperBinding binding;
    private List<CategoryPojo> categoryPojos;

    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_paper, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);


        prefManager = new PrefManager(getContext());
        categoryPojos = new ArrayList<>();
        facultyPojos = new ArrayList<>();
        hideEmpty();

        initView(view);


        binding.backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HomeActivity) getActivity()).onBackPressed();
            }
        });

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
                getCategory();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

   private void initView(View view) {

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        binding.termRecyclerView.setLayoutManager(gridLayoutManager);
    }


    public void getCategory() {//prefManager.getCenterId()
        Call<CategoryResponse> call = Apiservice.getInstance().apiRequest.
                getCategory(prefManager.getSubjectId());
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {

                binding.progressView.setVisibility(View.VISIBLE);

                call.enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                                Log.d("tag", "articles total result:: " + response.body().getMessage());
                                categoryPojos.clear();
                                categoryPojos.addAll(response.body().data);
                                if (categoryPojos.size() == 0) {
                                    showEmpty();
                                } else {
                                    hideEmpty();
                                    if (paperCategory == 0)
                                        paperCategory = categoryPojos.get(0).getId();
                                }
                                categoryAdapter = new CategoryAdapter(getContext(), categoryPojos, PaperFragment.this, paperCategory);
                                binding.categoryRecyclerView.setAdapter(categoryAdapter);
                                getPapers();
                            } else {
                                categoryPojos.clear();
                                categoryAdapter = new CategoryAdapter(getContext(), categoryPojos, PaperFragment.this, paperCategory);
                                binding.categoryRecyclerView.setAdapter(categoryAdapter);
                                showEmpty();
                            }
                        }
                        binding.progressView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        showEmpty();
                        binding.progressView.setVisibility(View.GONE);

                        Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(paperCategory, prefManager.getSubjectId(), prefManager.getStudentData().getId());
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {

                binding.progressView.setVisibility(View.VISIBLE);

                call.enqueue(new Callback<PaperResponse>() {
                    @Override
                    public void onResponse(Call<PaperResponse> call, Response<PaperResponse> response) {
                        if (response.body() != null && response.body().status && response.body().data != null && response.body().data.size() != 0) {
                            Log.d("tag", "articles total result:: " + response.body().getMessage());
                            facultyPojos.clear();
                            facultyPojos.addAll(response.body().data);
                            if (facultyPojos.size() == 0) {
                                showEmpty();
                            } else {
                                hideEmpty();
                            }
                            facultySelectAdapter = new PaperHomeAdapter(getContext(), PaperFragment.this, facultyPojos, paperCategory);
                            binding.termRecyclerView.setAdapter(facultySelectAdapter);
                        } else {
                            facultyPojos.clear();
                            facultySelectAdapter = new PaperHomeAdapter(getContext(), PaperFragment.this, facultyPojos, paperCategory);
                            binding.termRecyclerView.setAdapter(facultySelectAdapter);
                            showEmpty();
                        }
                        binding.progressView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<PaperResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                        showEmpty();
                        binding.progressView.setVisibility(View.GONE);

                    }
                });
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAddToCart() {
        if (prefManager.getCartPapers().size() == 1) {
            ((HomeActivity) getActivity()).SetNotificationCartBadge();
        }
    }

    @Override
    public void getUpdatePapers() {
        getPapers();
    }


    void showEmpty() {
        binding.emptyLayout.setVisibility(View.VISIBLE);


    }

    void hideEmpty() {
        binding.emptyLayout.setVisibility(View.GONE);

    }

    @Override
    public void onCheckForEmpty() {

    }


    @Override
    public void onCategoryClick(int catId) {
        paperCategory = catId;
        getPapers();
    }

}