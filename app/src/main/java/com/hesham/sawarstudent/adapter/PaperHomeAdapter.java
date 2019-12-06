package com.hesham.sawarstudent.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.data.response.PaperResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperHomeAdapter extends RecyclerView.Adapter<PaperHomeAdapter.MyViewHolder> {
    private Context context;
    private List<PaperPojo> facultyPojos;
    private EventListener listener;
    private Dialog dialog;
    private PrefManager prefManager;
    private int paperCategory;
    private ArrayList<PaperPojo> cartPapers;
    HashMap<Integer, PaperPojo> paperPojoHashMap;

    public PaperHomeAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public PaperHomeAdapter(Context context, EventListener listener, List<PaperPojo> facultyPojos, int paperCategory) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.paperCategory = paperCategory;
        prefManager = new PrefManager(context);
        paperPojoHashMap = prefManager.getCartPapers();
        if (paperPojoHashMap == null) {
            paperPojoHashMap = new HashMap<>();
        }
        cartPapers = new ArrayList<>();
        this.listener = listener;

    }

    @NonNull
    @Override
    public PaperHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_paper, parent, false);
        return new PaperHomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHomeAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView facultyname, date, price, pages;

        public ImageView cartImage;
        public ImageView selctPaper;
        public boolean isChecked;
        int numberOdCopies=0;
        public MyViewHolder(View itemView) {
            super(itemView);

            facultyname = itemView.findViewById(R.id.papername);
            date = itemView.findViewById(R.id.paperdate);
            pages = itemView.findViewById(R.id.paperpages);
            price = itemView.findViewById(R.id.paperprice);
            cartImage = itemView.findViewById(R.id.cartId);
            selctPaper = itemView.findViewById(R.id.selectedPaperId);

        }

        public void bind(final PaperPojo facultyPojo) {
            facultyname.setText("Name: " + facultyPojo.getName());
            date.setText("date: " + facultyPojo.getDate());
            pages.setText("pages: " + facultyPojo.getPage());
            price.setText("price:" + facultyPojo.getPrice());

            if (facultyPojo.getPaper_flag() == 1) {
                selctPaper.setImageResource(R.drawable.checked_icon);
                isChecked=true;
            } else {
                selctPaper.setImageResource(R.drawable.unchecked_icon);
                isChecked=false;
            }
            selctPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isChecked = !isChecked;
                    if (isChecked) {
                        selctPaper.setImageResource(R.drawable.checked_icon);
                    } else {
                        selctPaper.setImageResource(R.drawable.unchecked_icon);
                    }
                    markPaper(facultyPojo, isChecked);

                }
            });

            cartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        cartImage.setClickable(false);
                    if (prefManager.getCartCenterId() == 0 || prefManager.getCartCenterId() == prefManager.getCenterId()) {
                        dialog = new Dialog(context); // making dialog full screen
                        LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View Dview = layoutinflater.inflate(R.layout.dialog_add_copy_tocart, null);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(Dview);
                        TextView dialogButton = dialog.findViewById(R.id.addsubject);
                        final ImageView addcopy = dialog.findViewById(R.id.addcopy);
                        final TextView numofcopies = dialog.findViewById(R.id.numofcopies);
                        final ImageView minuscopy = dialog.findViewById(R.id.minuscopy);
                        numofcopies.setText(facultyPojo.getNo() + "");

                        addcopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int numOfCopies = facultyPojo.getNo();
                                if (numOfCopies < 5) {
                                    numOfCopies++;
                                    facultyPojo.setNo(numOfCopies);
                                    numofcopies.setText(numOfCopies + "");
                                } else {
                                    Toast.makeText(context, "The maximum number of copies is 5", Toast.LENGTH_LONG).show();
                                }
                                numberOdCopies=1;
                            }
                        });
                        minuscopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int numOfCopies = facultyPojo.getNo();
                                if (numOfCopies > 1) {
                                    numOfCopies--;
                                    facultyPojo.setNo(numOfCopies);
                                    numofcopies.setText(numOfCopies + "");
                                } else {
                                    Toast.makeText(context, "The min number of copies is 1", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (numberOdCopies==0){
                                    Toast.makeText(context, "you should choose at least 1 copy", Toast.LENGTH_LONG).show();
                                }else {
                                notifyDataSetChanged();
                                facultyPojo.setP_id(facultyPojo.getId());
//                                if (prefManager.getCartPapers() != null) {
//                                    if (prefManager.getCartPapers().containsKey(facultyPojo.getId())) {
//                                        paperPojoHashMap.put(facultyPojo.getId(), facultyPojo);
//                                    }
//                                }
                                if (prefManager.getCartPapers() == null || prefManager.getCartPapers().size() >= 0) {
                                    facultyPojo.setP_id(facultyPojo.getId());
                                    facultyPojo.setSubName(prefManager.getSubjectName());
                                    cartPapers.add(facultyPojo);
                                    paperPojoHashMap.put(facultyPojo.getId(), facultyPojo);
                                    dialog.dismiss();
                                    prefManager.setCartPapers(paperPojoHashMap);
                                    prefManager.setCartCenterId(prefManager.getCenterId());
                                    listener.onAddToCart();
                                } else {
                                    Toast.makeText(context, "you can't make greater than 5 orders", Toast.LENGTH_LONG).show();
                                }
                            }
                            }

                        });

                        dialog.show();
                    } else {
                        Toast.makeText(context, "you can't order form different copy centers ", Toast.LENGTH_LONG).show();
                    }


                }
            });


        }
    }

    public interface EventListener {
        void onAddToCart();
        void getUpdatePapers();

    }

    public void updateList(List<PaperPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }

    public void markPaper(PaperPojo paperPojo, boolean checked) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                manualmarkPaper(prefManager.getStudentData().getId(), paperPojo.getSub_id(), paperPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                Log.d("tag", "articles total result:: ");
                if (response.body().status){
                    getPapers();
                }else {

                }
//                getPapers();
//                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
//                dialog.dismiss();

            }
        });
    }


    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(paperCategory, prefManager.getSubjectId(), prefManager.getStudentData().getId());
        call.enqueue(new Callback<PaperResponse>() {
            @Override
            public void onResponse(Call<PaperResponse> call, Response<PaperResponse> response) {
                if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PaperResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}

