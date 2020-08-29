package com.hesham.sawarstudent.ui.subjects;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.SubjectViewPager;
import com.hesham.sawarstudent.data.model.CenterPojo;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectFragment extends Fragment {

    SubjectViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;

    private int years;
    private TextView centerName;

    PrefManager prefManager;
    private ImageView backarrowId;

    private ImageView notificationBtnId;
    private Integer depId;
    private CenterPojo centerPojo;
    private boolean isNotificationEnable;
    private int notificationYear;
    private Integer notificationDep;

    public SubjectFragment(int years) {
        this.years = years;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        prefManager = new PrefManager(getContext());
        centerName = view.findViewById(R.id.centernameId);
        centerName.setText(prefManager.getCenterData().getName());
        notificationBtnId = view.findViewById(R.id.notificationBtnId);

        viewPager = view.findViewById(R.id.view_pager);
        tabs = view.findViewById(R.id.tabs);
        sectionsPagerAdapter = new SubjectViewPager(getContext(), getChildFragmentManager(), years);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(1);
        tabs.setupWithViewPager(viewPager);
        centerPojo = prefManager.getCenterData();
        checkNotification(centerPojo.getNotification_dep());
        notificationBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerPojo.setNotification_flag(!isNotificationEnable);
                applyNotification();
            }
        });
        backarrowId = view.findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).onBackPressed();
            }
        });
        return view;
    }
    public void checkNotification(Integer notif_Dep ) {
        depId=notif_Dep;
        centerPojo = prefManager.getCenterData();
        isNotificationEnable = centerPojo.getIsNotification_flag();
        notificationDep=centerPojo.getNotification_dep();
        notificationYear=centerPojo.getNotification_year();
        if (notif_Dep==null)notif_Dep=0;
        if (notificationDep==null)notificationDep=0;
        if (isNotificationEnable&& notificationDep.equals(notif_Dep) &&notificationYear==years  ) {
            notificationBtnId.setImageResource(R.drawable.choosed_bell);
        } else {
            notificationBtnId.setImageResource(R.drawable.bell);
        }
    }

    public void applyNotification() {//prefManager.getCenterId()

//        List<Fragment> fragments = getChildFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment instanceof FirstTermFragment) {
//                    depId = ((FirstTermFragment) fragment).depId;
//                } else {
//                    depId = ((SecondTermFragment) fragment).depId;
//                }
//            }
//        }
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                applyNotification(prefManager.getCenterId(), prefManager.getStudentData().getId()
                        , prefManager.getStudentData().getFacultyId(), depId, years);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                Log.d("tag", "articles total result:: ");
                if (response.body().status) {
                    isNotificationEnable=!isNotificationEnable;
                    centerPojo.setNotification_flag(isNotificationEnable);
                    centerPojo.setNotification_year(years);
                    centerPojo.setNotification_dep(depId);

                    prefManager.setCenterData(centerPojo);
                    notificationBtnId.setImageResource(R.drawable.choosed_bell);

                } else {
                    centerPojo.setNotification_flag(isNotificationEnable);
                }
                if (isNotificationEnable) {
                    notificationBtnId.setImageResource(R.drawable.choosed_bell);
                } else {
                    notificationBtnId.setImageResource(R.drawable.bell);
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                centerPojo.setNotification_flag(isNotificationEnable);
                if (isNotificationEnable) {
                    notificationBtnId.setImageResource(R.drawable.choosed_bell);
                } else {
                    notificationBtnId.setImageResource(R.drawable.bell);
                }
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
    }


}
