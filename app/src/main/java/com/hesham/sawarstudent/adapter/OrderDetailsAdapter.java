package com.hesham.sawarstudent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.model.OrderDetailsPojo;
import com.hesham.sawarstudent.data.model.OrderPojo;
import com.hesham.sawarstudent.data.response.DetailsResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
        private Context context;
        private List<OrderDetailsPojo> facultyPojos;
    private EventListener listener;

    public OrderDetailsAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public OrderDetailsAdapter(Context context, List<OrderDetailsPojo> facultyPojos ) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;

    }

        @NonNull
        @Override
        public OrderDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_detailsorder, parent, false);
            return new OrderDetailsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetailsAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView ordername , copy ,price  ;
            public Button deleteBtn;
            public MyViewHolder(View itemView) {
                super(itemView);

                ordername = itemView.findViewById(R.id.ordername);
                copy = itemView.findViewById(R.id.orderno);
                price = itemView.findViewById(R.id.orderprice);

            }

            public void bind(final OrderDetailsPojo orderPojo) {
                String lectutre=orderPojo.getPaperCategory();
                String year=orderPojo.getYear();
                String faculty=orderPojo.getFaculty();
                String department=orderPojo.getDepartment();

                ordername.setText(year +"/"+ faculty +"/"+ department +"/"+orderPojo.getSubject() +"/"+lectutre+"/" + orderPojo.getName());//                date.setText("date: "+orderPojo.getDate());
//
//                date.setText("date: "+orderPojo.getDate());
//                time.setText("Time: "+orderPojo.getDate());
                copy.setText(""+orderPojo.getNo());
                price.setText(""+orderPojo.getPrice());

            }
        }

    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<OrderDetailsPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

