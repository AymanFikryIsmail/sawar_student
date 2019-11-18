package com.hesham.sawarstudent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.PaperPojo;
import com.hesham.sawarstudent.utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<PaperPojo> facultyPojos;
    private EventListener listener;

    PrefManager prefManager;

    String paperType;
    HashMap<Integer, PaperPojo> paperPojoHashMap;
    private ArrayList<PaperPojo> cartPapers;

    public CartAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public CartAdapter(Context context, ArrayList<PaperPojo> facultyPojos, EventListener listener, String paperType) {
        this.context = context;
        this.listener = listener;
        prefManager = new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.paperType = paperType;
        paperPojoHashMap = prefManager.getCartPapers();
        if (paperPojoHashMap == null) {
            paperPojoHashMap = new HashMap<>();
        }
        cartPapers = new ArrayList<>();

    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart, parent, false);
        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView papername,numofcpoies  , price, pages;
        ImageView minuscopy, addcopy;
        Button removecart;

        public MyViewHolder(View itemView) {
            super(itemView);

            papername = itemView.findViewById(R.id.papername);
            numofcpoies = itemView.findViewById(R.id.papercopy);
            addcopy = itemView.findViewById(R.id.addcopy);
            minuscopy = itemView.findViewById(R.id.minuscopy);

            price = itemView.findViewById(R.id.paperprice);
            removecart = itemView.findViewById(R.id.removecart);

        }

        public void bind(final PaperPojo paperPojo) {
            String lectutre="Lecture";
            if (paperPojo.getType().equals("l")){
                lectutre="Lecture";
            }else if (paperPojo.getType().equals("h")){
                lectutre="Handouts";

            }else if (paperPojo.getType().equals("s")){
                lectutre="Sections";

            }else if (paperPojo.getType().equals("c")){
                lectutre="Courses";

            }else if (paperPojo.getType().equals("r")){
                lectutre="Revisions";

            }
            papername.setText(paperPojo.getSubName()+"/"+lectutre+"/"+paperPojo.getName());
//            papername.setText(paperPojo.getName());
            numofcpoies.setText("" + paperPojo.getNo());
            price.setText("" + paperPojo.getPrice());
            addcopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int numOfCopies = paperPojo.getNo();
                    if (paperPojo.getNo() < 5) {
                        paperPojo.setNo(++numOfCopies);
                        notifyDataSetChanged();
                        numofcpoies.setText(paperPojo.getNo() + "");
                        listener.onChange(facultyPojos);
                    } else {
                        Toast.makeText(context, "The maximum number of copies is 5", Toast.LENGTH_LONG).show();
                    }
                    if (prefManager.getCartPapers() == null || prefManager.getCartPapers().size() <= 5) {
                        paperPojo.setP_id(paperPojo.getId());
                        cartPapers.add(paperPojo);
                        paperPojoHashMap.put(paperPojo.getId(), paperPojo);
                        prefManager.setCartPapers(paperPojoHashMap);
                        prefManager.setCartCenterId(prefManager.getCenterId());
                    } else {
                        Toast.makeText(context, "you can't make greater than 5 orders", Toast.LENGTH_LONG).show();
                    }
                }
            });
            minuscopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int numOfCopies = paperPojo.getNo();
                    if (paperPojo.getNo() > 1) {
                        paperPojo.setNo(--numOfCopies);
                        notifyDataSetChanged();
                        numofcpoies.setText(paperPojo.getNo() + "");
                        listener.onChange(facultyPojos);
                    }
                    if (prefManager.getCartPapers() == null || prefManager.getCartPapers().size() <= 5) {
                        paperPojo.setP_id(paperPojo.getId());
                        cartPapers.add(paperPojo);
                        paperPojoHashMap.put(paperPojo.getId(), paperPojo);
                        prefManager.setCartPapers(paperPojoHashMap);
                        prefManager.setCartCenterId(prefManager.getCenterId());
                    } else {
                        Toast.makeText(context, "you can't make greater than 5 orders", Toast.LENGTH_LONG).show();
                    }
                }
            });

            removecart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    facultyPojos.remove(paperPojo);
                    listener.onRemove(facultyPojos);
                    paperPojoHashMap.remove(paperPojo.getId());
                    prefManager.setCartPapers(paperPojoHashMap);
                    if (paperPojoHashMap.size()==0){
                        prefManager.setCartCenterId(0);
                    }


                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    public interface EventListener {
        void onEvent(Fragment data);

        void onRemove(ArrayList<PaperPojo> paperPojos);

        void onChange(ArrayList<PaperPojo> paperPojos);
    }

    public void updateList(ArrayList<PaperPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


}

