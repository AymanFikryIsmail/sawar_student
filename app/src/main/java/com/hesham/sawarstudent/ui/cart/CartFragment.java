package com.hesham.sawarstudent.ui.cart;


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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.CartAdapter;
import com.hesham.sawarstudent.data.model.AddOrderPojo;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.response.CheckPromoResponse;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.DelayResponse;
import com.hesham.sawarstudent.data.response.WaitingResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements CartAdapter.EventListener {


    public CartFragment() {
        // Required empty public constructor
    }

    PrefManager prefManager;
    private ArrayList<PaperPojo> paperCartPojos;

    private RecyclerView paperCartRecyclerView;
    private CartAdapter paperCartAdapter;

    private TextView totalPrice, totalService, totalMonye;
    private Button enterPromo, cancelCart, confirmCart;
    double total_service, total_money;
    double total_Price = 0;

    HashMap<Integer, PaperPojo> paperPojoHashMap;

    boolean isServiceFreeFromPoints;
    TextView emptyLayout;

    LinearLayout calculationId;

    AlertDialog addOrderDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        prefManager = new PrefManager(getContext());
        paperPojoHashMap = new HashMap<>();
        isServiceFreeFromPoints = false;

        calculationId = view.findViewById(R.id.calculationId);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        hideEmpty();
        paperCartRecyclerView = view.findViewById(R.id.termRecyclerView);
        paperPojoHashMap = prefManager.getCartPapers();
        paperCartPojos = new ArrayList<>();
        if (paperPojoHashMap != null) {
            for (Map.Entry<Integer, PaperPojo> entry : paperPojoHashMap.entrySet()) {
                paperCartPojos.add(entry.getValue());
            }
        }
        if (paperCartPojos == null) {
            paperCartPojos = new ArrayList<>();
        }
        if (paperCartPojos.size() == 0) {
            showEmpty();
        } else {
            hideEmpty();
        }
        initView(view);
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        paperCartRecyclerView.setLayoutManager(gridLayoutManager);
        Collections.sort(paperCartPojos, new Comparator<PaperPojo>() {
            public int compare(PaperPojo o1, PaperPojo o2) {
                return o1.getLongDate().compareTo(o2.getLongDate());
            }
        });
        Collections.sort(paperCartPojos, new Comparator<PaperPojo>() {
            public int compare(PaperPojo o1, PaperPojo o2) {
                return o1.getSubName().compareTo(o2.getSubName());
            }
        });

        paperCartAdapter = new CartAdapter(getContext(), paperCartPojos, this, "");
        paperCartRecyclerView.setAdapter(paperCartAdapter);

        return view;
    }

    void initView(View view) {
        totalPrice = view.findViewById(R.id.totalPriceCart);
        totalService = view.findViewById(R.id.totalServiceCart);
        totalMonye = view.findViewById(R.id.totalCart);
        enterPromo = view.findViewById(R.id.promocode);
        cancelCart = view.findViewById(R.id.removecart);
        confirmCart = view.findViewById(R.id.confirmcart);

        if (paperCartPojos.size() == 0) {
            enterPromo.setClickable(false);
            cancelCart.setClickable(false);
            confirmCart.setClickable(false);
        }


        cancelCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to cancel  ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                prefManager.removeCartPapers();
                                paperCartPojos.clear();
                                prefManager.sethasPromo(false);
                                paperCartAdapter = new CartAdapter(getContext(), paperCartPojos, CartFragment.this, "");
                                paperCartRecyclerView.setAdapter(paperCartAdapter);
//                                paperCartAdapter.notifyDataSetChanged();
                                calculateTotalPrice();
                                ((HomeActivity) getActivity()).clearNotificationCartBadge();
                                prefManager.setCartCenterId(0);
                                prefManager.setCartPapers(new HashMap<Integer, PaperPojo>());

                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });

        confirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orderNum = prefManager.getOrderNumber();
                if (orderNum < 2) {
                    checkProblem();
                } else {
                    Toast.makeText(getContext(), "you can't make greater than 2 orders", Toast.LENGTH_LONG).show();
                }
            }
        });

        enterPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPromoDialog();
            }
        });
        calculateServiceFormPoints();
    }

    void calculateServiceFormPoints() {
        Call<CheckPromoResponse> call = Apiservice.getInstance().apiRequest.
                checkForfreeServiceFromPoints(prefManager.getStudentData().getId());
        call.enqueue(new Callback<CheckPromoResponse>() {
            @Override
            public void onResponse(Call<CheckPromoResponse> call, Response<CheckPromoResponse> response) {
                if (response != null && response.body().status) {
                    isServiceFreeFromPoints = response.body().data;
                }
                calculateTotalPrice();

            }

            @Override
            public void onFailure(Call<CheckPromoResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                calculateTotalPrice();

            }
        });

    }

    void calculateTotalPrice() {
        total_Price = 0;
        for (int i = 0; i < paperCartPojos.size(); i++) {
            total_Price = total_Price + paperCartPojos.get(i).getPrice() * paperCartPojos.get(i).getNo();
        }
        totalPrice.setText(total_Price + "");
        double service ;
        if (prefManager.getHasPromo()){
            service = 0;
        }else{
            service = calculateService();
        }
        if (isServiceFreeFromPoints) {
            service = 0;
        }
        total_service = service;
        total_money = (total_Price + service);
        totalService.setText(total_service + "");
        totalMonye.setText(total_money + "");
    }

    double calculateService() {
        ArrayList<Integer> copyNum = new ArrayList<>();
        ArrayList<Integer> pageNum = new ArrayList<>();
        ;
        for (int i = 0; i < paperCartPojos.size(); i++) {
            copyNum.add(paperCartPojos.get(i).getNo());
            pageNum.add(paperCartPojos.get(i).getPage());
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


    @Override
    public void onEvent(Fragment data) {

    }

    @Override
    public void onRemove(ArrayList<PaperPojo> paperPojos) {
        this.paperCartPojos = paperPojos;
        if (paperCartPojos.size() == 0) {
            prefManager.sethasPromo(false);
            prefManager.setCartCenterId(0);
            showEmpty();
            prefManager.setCartPapers(new HashMap<Integer, PaperPojo>());
            ((HomeActivity) getActivity()).clearNotificationCartBadge();
        }
        calculateTotalPrice();

    }

    @Override
    public void onChange(ArrayList<PaperPojo> paperPojos) {
        this.paperCartPojos = paperPojos;
        calculateTotalPrice();
    }

    void getWaiting() {
        Call<WaitingResponse> call = Apiservice.getInstance().apiRequest.
                getWaiting(prefManager.getCenterId());
        call.enqueue(new Callback<WaitingResponse>() {
            @Override
            public void onResponse(Call<WaitingResponse> call, Response<WaitingResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        confirmOrder(response.body().data.getOrders());
                    }
                }
            }

            @Override
            public void onFailure(Call<WaitingResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
     int i=0;
    void confirmOrder(int waiting) {
//        addOrderDialog=new AlertDialog(getContext());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                .setMessage("Once you have confirmed you have to receive the order within 24 hours \n waiting: " + waiting)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (i==0){
                            addOrder();
                        }
                        i=1;
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    void addOrder() {
        AddOrderPojo addOrderPojo = new AddOrderPojo(prefManager.getCenterId(), prefManager.getStudentData().getId(), total_service, total_money, paperCartPojos);
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                addOrder(addOrderPojo);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status)
                        paperCartPojos.clear();
                    prefManager.sethasPromo(false);
                    paperCartAdapter = new CartAdapter(getContext(), paperCartPojos, CartFragment.this, "");
                    paperCartRecyclerView.setAdapter(paperCartAdapter);
                    prefManager.setCartPapers(new HashMap<Integer, PaperPojo>());
                    int orderNum = prefManager.getOrderNumber();
                    prefManager.setOrderNumber(++orderNum);
                    calculateTotalPrice();
                    ((HomeActivity) getActivity()).clearNotificationCartBadge();
                    if (prefManager.getOrderCenterId() == 0) {
                        ((HomeActivity) getActivity()).SetNotificationOrderBadge();
                    }
                    prefManager.setCartCenterId(0);
                    prefManager.setOrderCenterId(1);
                    Log.d("tag", "articles total result:: "
                    );
//                    facultyPojos.clear();
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    void checkPomoCode(String promoCode) {
        Call<CheckPromoResponse> call = Apiservice.getInstance().apiRequest.
                checkPromocode(prefManager.getStudentData().getId(), promoCode);
        call.enqueue(new Callback<CheckPromoResponse>() {
            @Override
            public void onResponse(Call<CheckPromoResponse> call, Response<CheckPromoResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data) {
                        total_service = 0;
                        prefManager.sethasPromo(true);
                        total_money = (total_Price + total_service);
                        totalService.setText(total_service + "");
                        totalMonye.setText(total_money + "");
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Log.d("tag", "articles total result:: ");
                }
            }

            @Override
            public void onFailure(Call<CheckPromoResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }

    void checkProblem() {
        Call<DelayResponse> call = Apiservice.getInstance().apiRequest.
                checkProblem(prefManager.getCenterId());
        call.enqueue(new Callback<DelayResponse>() {
            @Override
            public void onResponse(Call<DelayResponse> call, Response<DelayResponse> response) {
                if (response.body() != null) {

                    if (response.body().status && response.body().data != null) {
                        if (response.body().data.getDelay_hours() == 0) {
                            getWaiting();
                        } else {
                            showDelayDialog(response.body().data.getDelay_hours(), response.body().data.getDelay_date());
                        }
                    }
                }
//                    facultyPojos.clear();
            }

            @Override
            public void onFailure(Call<DelayResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }


    void showDelayDialog(int hours, long delay) {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delay);
        Button dialogButton = dialog.findViewById(R.id.confirm);
        Button cancelButton = dialog.findViewById(R.id.cancel);
        final TextView hoursTxt = dialog.findViewById(R.id.hours);
        Date date = new Date(delay);
        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm:ss");
        String dateText = df2.format(date);
        hoursTxt.setText("We are sorry for the inconvenience. There's a problem with the center's printers. We're working to fix it within " + hours + " hours since " + dateText);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void showPromoDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_promocode);
        Button dialogButton = dialog.findViewById(R.id.addPromoCode);
        final EditText promocodename = dialog.findViewById(R.id.promocodename);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (promocodename.getText().toString().isEmpty() || promocodename.getText().toString() == null) {
                    Toast.makeText(getActivity(), "enter value", Toast.LENGTH_LONG).show();
                } else {
                    checkPomoCode(promocodename.getText().toString());
                }
                dialog.dismiss();
            }
        });


        dialog.show();
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
