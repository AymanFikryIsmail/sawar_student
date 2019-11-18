package com.hesham.sawarstudent.ui.subjects;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.PaperHomeAdapter;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.model.SubjectPojo;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.PaperResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.ui.cart.CartFragment;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperFragment extends Fragment implements View.OnClickListener , PaperHomeAdapter.EventListener {
    public static PaperFragment newInstance(String paperType) {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        args.putString("paperType", paperType);
        fragment.setArguments(args);
        return fragment;
    }

    public PaperFragment(String paperType) {
        this.paperType = paperType;
    }

    public PaperFragment() {
    }

    private List<PaperPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private PaperHomeAdapter facultySelectAdapter;
    PrefManager prefManager;
    private String paperType;

    TextView lectureId, handoutId, sectionId, courseId, revisionId;
    ImageView favLectureId, favHandoutId, favSectionId, favCourseId, favRevisionId;
    View lectureViewId, handoutViewId, sectionViewId, courseViewId, revisionViewId;
    private ImageView backarrowId;
    TextView emptyLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paperType = getArguments().getString("paperType");
        }
    }
    private FrameLayout progress_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paper, container, false);
        prefManager = new PrefManager(getContext());

        facultyRecyclerView = view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        emptyLayout=view.findViewById(R.id.emptyLayout);
        hideEmpty();
        progress_view = view.findViewById(R.id.progress_view);

        initView(view);
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new PaperHomeAdapter(getContext(), PaperFragment.this,facultyPojos, paperType);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getPapers();
        backarrowId=view.findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).onBackPressed();
            }
        });
        return view;
    }

    void initView(View view) {
        if (paperType == null) {
            paperType = "l";
        }
        lectureId = view.findViewById(R.id.lectureId);
        handoutId = view.findViewById(R.id.handoutId);
        sectionId = view.findViewById(R.id.sectionId);
        courseId = view.findViewById(R.id.courseId);
        revisionId = view.findViewById(R.id.revisionId);

        lectureViewId = view.findViewById(R.id.lectureViewId);
        handoutViewId = view.findViewById(R.id.handoutViewId);
        sectionViewId = view.findViewById(R.id.sectionViewId);
        courseViewId = view.findViewById(R.id.courseViewId);
        revisionViewId = view.findViewById(R.id.revisionViewId);

        favLectureId = view.findViewById(R.id.favLectureId);
        favHandoutId = view.findViewById(R.id.favHandoutId);
        favSectionId = view.findViewById(R.id.favSectionId);
        favCourseId = view.findViewById(R.id.favCourseId);
        favRevisionId = view.findViewById(R.id.favRevisionId);


        lectureId.setOnClickListener(this);
        handoutId.setOnClickListener(this);
        sectionId.setOnClickListener(this);
        courseId.setOnClickListener(this);
        revisionId.setOnClickListener(this);

        favLectureId.setOnClickListener(this);
        favHandoutId.setOnClickListener(this);
        favSectionId.setOnClickListener(this);
        favCourseId.setOnClickListener(this);
        favRevisionId.setOnClickListener(this);
        selectTab(paperType);

    }


    public void addFavourite() {//prefManager.getCenterId()

        new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                .setTitle("Add to favourite")
                .setMessage("Are you sure you add it ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addToFavouriteApi();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    void addToFavouriteApi() {
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.addFavourite(prefManager.getStudentData().getId(), prefManager.getSubjectId(), paperType);

        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {


            call.enqueue(new Callback<CustomResponse>() {
                @Override
                public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");
                        Toast.makeText(getContext(), "Added to favourite ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                }
            });
            }else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lectureId:
                paperType = "l";
                selectTab(paperType);
                break;
            case R.id.handoutId:
                paperType = "h";
                selectTab(paperType);

                break;
            case R.id.sectionId:
                paperType = "s";
                selectTab(paperType);

                break;
            case R.id.courseId:
                paperType = "c";
                selectTab(paperType);

                break;
            case R.id.revisionId:
                paperType = "r";
                selectTab(paperType);

                break;

            case R.id.favLectureId:
                paperType = "l";
                addFavourite();
                break;
            case R.id.favHandoutId:
                paperType = "h";
                addFavourite();
                break;
            case R.id.favSectionId:
                paperType = "s";
                addFavourite();
                break;
            case R.id.favCourseId:
                paperType = "c";
                addFavourite();
                break;
            case R.id.favRevisionId:
                paperType = "r";
                addFavourite();
                break;
        }

    }


    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(paperType, prefManager.getSubjectId(), prefManager.getStudentData().getId());
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {

            progress_view.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<PaperResponse>() {
                @Override
                public void onResponse(Call<PaperResponse> call, Response<PaperResponse> response) {
                    if (response.body()!=null &&response.body().status && response.body().data != null && response.body().data.size() != 0) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().data);
                        if (facultyPojos.size()==0){
                            showEmpty();
                        }else {
                            hideEmpty();
                        }
                        facultySelectAdapter = new PaperHomeAdapter(getContext(),PaperFragment.this, facultyPojos, paperType);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                    } else {
                        facultyPojos.clear();
                        facultySelectAdapter = new PaperHomeAdapter(getContext(), PaperFragment.this,facultyPojos, paperType);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                        showEmpty();
                    }
                    progress_view.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<PaperResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    showEmpty();
                    progress_view.setVisibility(View.GONE);

                }
            });
            }else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    public void selectTab(String tab){
        switch (tab) {
            case "l":
                paperType = "l";
                lectureId.setTextColor(getResources().getColor(R.color.colorPrimary));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.VISIBLE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.GONE);
                getPapers();
                break;
            case "h":
                paperType = "h";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.colorPrimary));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.VISIBLE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.GONE);
                getPapers();
                break;
            case "s":
                paperType = "s";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.colorPrimary));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.VISIBLE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.GONE);
                getPapers();
                break;
            case "c":
                paperType = "c";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.colorPrimary));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.VISIBLE);
                revisionViewId.setVisibility(View.GONE);
                getPapers();
                break;
            case "r":
                paperType = "r";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.colorPrimary));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.VISIBLE);
                getPapers();
                break;
        }
    }

    @Override
    public void onAddToCart() {
        if (prefManager.getCartPapers().size() ==1) {
            ((HomeActivity) getActivity()).SetNotificationCartBadge();
        }
    }

    @Override
    public void getUpdatePapers() {
        getPapers();
    }


    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);


    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);

    }
}