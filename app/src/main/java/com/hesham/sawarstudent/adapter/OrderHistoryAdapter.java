package com.hesham.sawarstudent.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.OrderPojo;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.DelayResponse;
import com.hesham.sawarstudent.data.response.OrderResponse;
import com.hesham.sawarstudent.data.response.PaperResponse;
import com.hesham.sawarstudent.data.response.StatusResponse;
import com.hesham.sawarstudent.data.response.WaitingResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.order.OrderActivity;
import com.hesham.sawarstudent.utils.PrefManager;
import com.hesham.sawarstudent.utils.rateBar.RatingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<OrderPojo> facultyPojos;
    private EventListener listener;

    private PrefManager prefManager;
    public RatingBar ratingBar;
    RatingDialog mRatingDialog;
    private float rateValue;

    public OrderHistoryAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public OrderHistoryAdapter(Context context, List<OrderPojo> facultyPojos, EventListener listener) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.prefManager = new PrefManager(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_unready_order, parent, false);
        return new OrderHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        if (facultyPojos.size() == 0) {
            prefManager.setOrderCenterId(0);
        } else {
            prefManager.setOrderCenterId(1);
        }
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ordernumber, date, price, time, waiting, orderfrom;
        public Button detailsBtn, statusBtn, inprogressBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            ordernumber = itemView.findViewById(R.id.ordernumber);
            orderfrom = itemView.findViewById(R.id.orderfrom);
            waiting = itemView.findViewById(R.id.waiting);

            date = itemView.findViewById(R.id.orderdate);
            time = itemView.findViewById(R.id.ordertime);
            price = itemView.findViewById(R.id.orderprice);
            detailsBtn = itemView.findViewById(R.id.ordersDetails);
            statusBtn = itemView.findViewById(R.id.statusBtn);
            inprogressBtn = itemView.findViewById(R.id.inprogress);

        }

        public void bind(final OrderPojo orderPojo) {
            ordernumber.setText(orderPojo.getNum() + "" + orderPojo.getDay() + orderPojo.getVar());
            orderfrom.setText(orderPojo.getCc());
            waiting.setText(orderPojo.getWaiting() + "");

            date.setText(orderPojo.getDate());
//            time.setText(orderPojo.getDate());

            Date date = new Date(orderPojo.getLongDate());
            String pm = convertTime(orderPojo.getFormattedDate());
            SimpleDateFormat df2 = new SimpleDateFormat("hh:mm");
            String dateText = df2.format(date);
            time.setText(pm);


            inprogressBtn.setBackgroundResource(R.drawable.custom_third_button);
            inprogressBtn.setText("Order in progress");
            price.setText("" + orderPojo.getTotal_price());
            if (orderPojo.getReady() == 1) {
                inprogressBtn.setBackgroundResource(R.drawable.custom_green_button);
                inprogressBtn.setText("Ready");
            }

            statusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    checkStatus(orderPojo);
                    showDelayDialog(orderPojo);

                }
            });
            detailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderActivity.class);
                    intent.putExtra("orderid", orderPojo.getId());
                    intent.putExtra("ordertotal", orderPojo.getTotal_price());
                    intent.putExtra("orderservice", orderPojo.getService());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String convertTime(String time) {
        String processingTime = "";

        try {
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            processingTime = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
        }
        return processingTime;
    }

    public interface EventListener {
        void onRemove(List<OrderPojo> orderPojo);

        void onReceive();

        void onResend();
    }

    public void checkStatus(OrderPojo orderPojo) {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.getOrderStatus(orderPojo.getId());
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body().status & response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    showDelayDialog(response.body().data.get(0));

                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    public void updateList(List<OrderPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


    void showDelayDialog(final OrderPojo orderPojo) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_status);
        Button recevedBtn = dialog.findViewById(R.id.recevedBtn);
        Button resendOrder = dialog.findViewById(R.id.resendOrder);
        Button removeOrder = dialog.findViewById(R.id.removeOrder);

        Button cancelorder = dialog.findViewById(R.id.cancelorder);
        final TextView statusText = dialog.findViewById(R.id.statusText);

        if (orderPojo.getDelay_hours() != 0 && orderPojo.getReady() == 0 && checkDelayDateIfExceed(orderPojo.getDelay_hours(), orderPojo.getDelay_date())) {
            showDelayProblemDialog(orderPojo);
        } else if (orderPojo.getCancel_cc() == 1 && orderPojo.getRecieve() == 1) {
            resendOrder.setVisibility(View.GONE);
            removeOrder.setVisibility(View.GONE);
            dialog.show();
            // when remove
//            recevedBtn.setVisibility(View.GONE);
//            resendOrder.setVisibility(View.GONE);
//            cancelorder.setVisibility(View.GONE);
//            removeOrder.setVisibility(View.VISIBLE);
//            dialog.show();

        } else if (isBeforeNow(orderPojo.getLongDate())) {
            resendOrder.setVisibility(View.GONE);
            removeOrder.setVisibility(View.GONE);
            dialog.show();

        } else {
            statusText.setText(" You haven't received the order for 24 hours \n please choose an action  ");
            removeOrder.setVisibility(View.GONE);
            dialog.show();
        }


        // if button is clicked, close the custom dialog
        recevedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                receiveOrder(orderPojo , 4);
                if (orderPojo.getReady() == 0) {
                    Toast.makeText(context, "The order hasn't been ready yet", Toast.LENGTH_LONG).show();
                } else {
                    initRateDialog(orderPojo);
                    rate();
                    dialog.dismiss();
                }

            }
        });
        resendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resendOrder(orderPojo);
                checkProblem(orderPojo);
                dialog.dismiss();
            }
        });
        removeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrder(orderPojo);
                dialog.dismiss();
            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cancelOrder(orderPojo);
//                dialog.dismiss();
                showCancelDialog(orderPojo);
                dialog.dismiss();
            }
        });

    }

    boolean isBeforeNow(long dt) {

        long after = dt + 86400000;
        long today = 0;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String output = sdf.format(c.getTime());
            Date date = sdf.parse(output);

            today = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (after > today) {
            return true;
        }
        return false;
    }

    boolean checkDelayDateIfExceed(long numofHours, long dt) {

        long after = dt + 60 * 60 * 1000 * numofHours;
        long today = 0;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String output = sdf.format(c.getTime());
            Date date = sdf.parse(output);

            today = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (after > today) {
            return true;
        }
        return false;
    }

    public void receiveOrder(final OrderPojo orderPojo, int rate, String comment) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.recieveRateOrder(orderPojo.getId(), prefManager.getStudentData().getId(), rate, comment);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");
                        facultyPojos.remove(orderPojo);
                        listener.onRemove(facultyPojos);
                        listener.onReceive();
                        int orderNum = prefManager.getOrderNumber();
                        prefManager.setOrderNumber(--orderNum);
                        notifyDataSetChanged();
                        boolean isRated = prefManager.getIsRateApp();
                        if (!isRated) {
                            rateApp();
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }

    public void removeOrder(final OrderPojo orderPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.removeOrder(orderPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");
                        facultyPojos.remove(orderPojo);
                        listener.onRemove(facultyPojos);
                        int orderNum = prefManager.getOrderNumber();
                        prefManager.setOrderNumber(--orderNum);
                        notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }

    public void cancelOrder(final OrderPojo orderPojo, String comment) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.cancelOrder(orderPojo.getId(), comment);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");

                        facultyPojos.remove(orderPojo);
                        listener.onRemove(facultyPojos);
                        int orderNum = prefManager.getOrderNumber();
                        prefManager.setOrderNumber(--orderNum);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }


    public void resendOrder(final OrderPojo orderPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.resendOrder(orderPojo.getId(), orderPojo.getCc_id());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");
                        facultyPojos.remove(orderPojo);
//                    listener.onRemove(facultyPojos);
                        listener.onResend();
//                    int orderNum = prefManager.getOrderNumber();
//                    prefManager.setOrderNumber(--orderNum);
                        notifyDataSetChanged();

                    }
                }
            }


            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }


    public void initRateDialog(final OrderPojo orderPojo) {
        mRatingDialog = new RatingDialog(context);
        mRatingDialog.setDefaultRating((int) orderPojo.getRate());
        mRatingDialog.setEnable(true);

        mRatingDialog.setRatingDialogListener(new RatingDialog.RatingDialogInterFace() {
            @Override
            public void onDismiss() {
                Log.v("RATELISTERNER", "onDismiss ");
            }

            @Override
            public void onSubmit(float rating, String commentTxt) {
                Log.v("RATELISTERNER", "onSubmit " + rating);
                rateValue = rating;
                mRatingDialog.setDefaultRating((int) rating);
                receiveOrder(orderPojo, (int) rateValue, commentTxt);

            }

            @Override
            public void onRatingChanged(float rating) {
                Log.v("RATELISTERNER", "onRatingChanged " + rating);
                mRatingDialog.setDefaultRating((int) rating);
            }
        });

    }

    public void rate() {
        mRatingDialog.showDialog();
    }

    void showDelayProblemDialog(final OrderPojo orderPojo) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delay);
        Button dialogButton = dialog.findViewById(R.id.confirm);
        Button cancelButton = dialog.findViewById(R.id.cancel);
        final TextView hoursTxt = dialog.findViewById(R.id.hours);
        Date date = new Date(orderPojo.getDelay_date());
        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm:ss");
        String dateText = df2.format(date);
        hoursTxt.setText("We are sorry for the inconvenience. There's a problem with the center's printers. We're working to fix it within "
                + orderPojo.getDelay_hours() + " hours since " + dateText);
//        hoursTxt.setText(orderPojo.getDelay_hours() + " hours since " + dateText);
        // if button is clicked, close the custom dialog
        dialogButton.setText("Wait");
        cancelButton.setText("Cancel Order");

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cancelOrder(orderPojo);
//                dialog.dismiss();
                showCancelDialog(orderPojo);
            }
        });

        dialog.show();

    }

    void showCancelDialog(final OrderPojo orderPojo) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel);
        Button cancelButton = dialog.findViewById(R.id.cancelOrder);
        final TextView commentTxt = dialog.findViewById(R.id.comment);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(orderPojo, commentTxt.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void checkProblem(final OrderPojo orderPojo) {
        Call<DelayResponse> call = Apiservice.getInstance().apiRequest.
                checkProblem(prefManager.getCenterId());
        call.enqueue(new Callback<DelayResponse>() {
            @Override
            public void onResponse(Call<DelayResponse> call, Response<DelayResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (response.body().data.getDelay_hours() == 0) {
                            getWaiting(orderPojo);
                        } else {
                            showDelayDialog(response.body().data.getDelay_hours(), response.body().data.getDelay_date(), orderPojo);
                        }
                    }
//                    facultyPojos.clear();
                }
            }

            @Override
            public void onFailure(Call<DelayResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }


    void showDelayDialog(int hours, long delay, final OrderPojo orderPojo) {
        final Dialog dialog = new Dialog(context);
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
                resendOrder(orderPojo);
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

    void getWaiting(final OrderPojo orderPojo) {
        Call<WaitingResponse> call = Apiservice.getInstance().apiRequest.
                getWaiting(prefManager.getCenterId());
        call.enqueue(new Callback<WaitingResponse>() {
            @Override
            public void onResponse(Call<WaitingResponse> call, Response<WaitingResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        confirmOrder(response.body().data.getOrders(), orderPojo);
                    }
                }
            }

            @Override
            public void onFailure(Call<WaitingResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    void confirmOrder(int waiting, final OrderPojo orderPojo) {
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setMessage("Once you have confirmed you have to receive the order within 24 hours \n waiting: " + waiting)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resendOrder(orderPojo);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    void rateApp() {
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle("Rate App")
                .setMessage("")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        prefManager.setIsRateApp(true);
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.hesham.sawarstudent"));
                        context.startActivity(viewIntent);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

}

