package com.hesham.sawarstudent.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.CategoryPojo;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private List<CategoryPojo> categoryPojoList;
    private EventListener listener;
    Dialog dialog;

    PrefManager prefManager;
    int categoryId;

    public CategoryAdapter() {
        categoryPojoList = new ArrayList<>();
    }

    public CategoryAdapter(Context context, List<CategoryPojo> categoryPojoList , EventListener listener ,  int categoryId) {
        this.context = context;
        this.categoryPojoList = categoryPojoList;
        prefManager = new PrefManager(context);
        this.listener=listener;
        this.categoryId=categoryId;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);
        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        holder.bind(categoryPojoList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public ImageView popupMenuTxt;
        public View categoryViewId;

        public MyViewHolder(View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.catId);
            categoryViewId = itemView.findViewById(R.id.categoryViewId);
            popupMenuTxt = itemView.findViewById(R.id.popupMenuTxt);
        }

        public void bind(final CategoryPojo facultyPojo) {
            categoryName.setText(facultyPojo.getName());
            categoryViewId.setVisibility(View.INVISIBLE);
            categoryName.setTextColor(context.getResources().getColor(R.color.grey1));
            if (categoryId==facultyPojo.getId()){
                categoryViewId.setVisibility(View.VISIBLE);
                categoryName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            popupMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addFavourite(facultyPojo.getId());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    categoryId=facultyPojo.getId();
                    categoryName.setTextColor(R.color.colorPrimary);
                    categoryViewId.setVisibility(View.VISIBLE);
                    listener.onCategoryClick(facultyPojo.getId());
                    notifyDataSetChanged();
                }
            });
        }
    }
    public void addFavourite(final int paperCategory) {//prefManager.getCenterId()

        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle("Add to favourite")
                .setMessage("Are you sure you add it ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addToFavouriteApi(paperCategory);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    void addToFavouriteApi(int paperCategory) {
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.addFavourite(prefManager.getStudentData().getId(), prefManager.getSubjectId(), paperCategory);
        if (NetworkUtilities.isOnline(context)) {
            if (NetworkUtilities.isFast(context)) {
                call.enqueue(new Callback<CustomResponse>() {
                    @Override
                    public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                        if (response.body().status) {
                            Log.d("tag", "articles total result:: ");
                            Toast.makeText(context, "Added to favourite ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<CustomResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        Toast.makeText(context, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(context, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    public interface EventListener {
        void onCheckForEmpty();
        void onCategoryClick(int catId);


    }

}

