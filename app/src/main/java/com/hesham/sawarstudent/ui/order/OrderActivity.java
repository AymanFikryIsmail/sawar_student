package com.hesham.sawarstudent.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.OrderDetailsAdapter;
import com.hesham.sawarstudent.data.model.OrderDetailsPojo;
import com.hesham.sawarstudent.data.response.DetailsResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {


    private List<OrderDetailsPojo> orderDetailsPojos;

    private RecyclerView facultyRecyclerView;
    private OrderDetailsAdapter facultySelectAdapter;

    PrefManager prefManager;
    int orderid;
    LinearLayout calculationId;
    LinearLayout emptyLayout;
    private TextView totalPrice, totalService, totalMonye;
    double total_service, total_money;
    double total_Price = 0;
    double orderservice, ordertotal;


    String year , faculty , department ;
    TextView facultyTxt, yearTxt ,  departmentTxt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        facultyRecyclerView = findViewById(R.id.termRecyclerView);
        prefManager = new PrefManager(this);
        if (getIntent().getExtras() != null) {
            orderid = getIntent().getIntExtra("orderid", 0);
            ordertotal = getIntent().getDoubleExtra("ordertotal", 0);
            orderservice = getIntent().getDoubleExtra("orderservice", 0);
        }
        facultyTxt= findViewById(R.id.faculty);
        yearTxt= findViewById(R.id.year);
        departmentTxt= findViewById(R.id.department);

        orderDetailsPojos = new ArrayList<>();
        initView();
        calculationId = findViewById(R.id.calculationId);
        emptyLayout = findViewById(R.id.emptyLayout);
        hideEmpty();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(this);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderDetailsAdapter(this, orderDetailsPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);

        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(this)) {
            if (NetworkUtilities.isFast(this)){
                getOrdersDetails();
            } else{
                Toast.makeText(this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    void initView() {
        totalPrice = findViewById(R.id.totalPriceCart);
        totalService = findViewById(R.id.totalServiceCart);
        totalMonye = findViewById(R.id.totalCart);
        totalService.setText(orderservice + "");
        totalPrice.setText(ordertotal + "");
        total_money = (ordertotal + orderservice);
        totalMonye.setText(total_money + "");

    }

    void calculateTotalPrice() {
        total_Price = 0;
        for (int i = 0; i < orderDetailsPojos.size(); i++) {
            total_Price = total_Price + orderDetailsPojos.get(i).getPrice() * orderDetailsPojos.get(i).getNo();
        }
        totalPrice.setText(total_Price + "");
        double service = calculateService();

        total_service = service;
        total_money = (total_Price + service);
        totalService.setText(total_service + "");
        totalMonye.setText(total_money + "");
    }

    double calculateService() {
        ArrayList<Integer> copyNum = new ArrayList<>();
        ArrayList<Integer> pageNum = new ArrayList<>();
        ;
        for (int i = 0; i < orderDetailsPojos.size(); i++) {
            copyNum.add(orderDetailsPojos.get(i).getNo());
            pageNum.add(orderDetailsPojos.get(i).getPage());
        }
        int x = 5;
        double sum = 0;
        double serv = 0;
        int q = 0;
        while (q == 0) {
            for (int i = 0; i < copyNum.size(); i++) {
                if (copyNum.get(i) > 0) {
                    sum += pageNum.get(i);
                    int newCopies = copyNum.get(i);
                    copyNum.set(i, --newCopies);
                }
            }
            if (sum > 0) {
//                console.log(sum/100)
                double rem = sum % 100;
                if (rem == 0) {
                    serv += Math.floor(sum / 100);
                } else {
                    serv += Math.floor(sum / 100) + 1;
                }
                sum = 0;
            } else {
                q = 1;
            }
        }
        return serv;
    }

    public void getOrdersDetails() {//prefManager.getCenterId()
        Call<DetailsResponse> call = Apiservice.getInstance().apiRequest.
                getOrderDetails(orderid);
        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        orderDetailsPojos.clear();
                        orderDetailsPojos.addAll(response.body().data);
//                    calculateTotalPrice();

                        if (orderDetailsPojos.size() == 0) {
                            showEmpty();
                        } else {
                            hideEmpty();
                            String year=orderDetailsPojos.get(0).getYear();
                            List<String> yearList =new ArrayList<>();
                            List<String> departmentList =new ArrayList<>();

                            for(int i= 0 ; i<orderDetailsPojos.size() ; i++){
                                if (!yearList.contains(orderDetailsPojos.get(i).getYear())){

                                    if (orderDetailsPojos.get(i).getYear().equals("1")&&!yearList.contains("First year") ){
                                        yearList.add("First year");

                                    }else if(orderDetailsPojos.get(i).getYear().equals("2")&&!yearList.contains("Second year") ){
                                        yearList.add("Second year");

                                    }else if(orderDetailsPojos.get(i).getYear().equals("3")&&!yearList.contains("Third year") ){
                                        yearList.add("Third year");

                                    }else if(orderDetailsPojos.get(i).getYear().equals("4")&&!yearList.contains("Fourth year") ){
                                        yearList.add("Fourth year");

                                    }else if(orderDetailsPojos.get(i).getYear().equals("5")&&!yearList.contains("Fifth year") ){
                                        yearList.add("Fifth year");

                                    }else if(orderDetailsPojos.get(i).getYear().equals("6")&&!yearList.contains("Sixth year") ){
                                        yearList.add("Sixth year");
                                    }
                                }
                            }


                            for(int i= 0 ; i<orderDetailsPojos.size() ; i++){
                                if (!departmentList.contains(orderDetailsPojos.get(i).getDepartment())){
                                    departmentList.add(orderDetailsPojos.get(i).getDepartment());
                                }
                            }


                            String faculty=orderDetailsPojos.get(0).getFaculty();
                            String department=orderDetailsPojos.get(0).getDepartment();
                            facultyTxt.setText(faculty );
                            departmentTxt.setText(departmentList.toString() );
                            yearTxt.setText(yearList.toString() );
                        }
                        facultySelectAdapter = new OrderDetailsAdapter(OrderActivity.this, orderDetailsPojos);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                showEmpty();

                Toast.makeText(OrderActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();

            }
        });
    }

    void showEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
        calculationId.setVisibility(View.GONE);


    }

    void hideEmpty() {
        emptyLayout.setVisibility(View.GONE);
        calculationId.setVisibility(View.VISIBLE);

    }
}
