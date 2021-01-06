package com.example.eventssqliteexample;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRcyAdapter extends RecyclerView.Adapter<MyRcyAdapter.ViewHolder> {

    List<MyEvent> myEvents;

    public MyRcyAdapter(List<MyEvent> events){
        this.myEvents=events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View singleView = inflater.inflate(R.layout.layout_single_event,parent, false);

        return new ViewHolder(singleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyEvent event=myEvents.get(position);
        holder.tvName.setText(event.getName());
        holder.tvDesc.setText(event.getDescription());
        holder.tvID.setText((event.getId()+""));
        holder.tvDate.setText(event.getDate());
        holder.tvLoc.setText(event.getLocation());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.isShow) {
                    holder.ll_loc.setVisibility(View.GONE);
                    holder.isShow=false;
                }
                else {
                    holder.ll_loc.setVisibility(View.VISIBLE);
                    holder.isShow=true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvName, tvDesc, tvDate, tvID, tvLoc;
        CardView cardView;
        LinearLayout ll_loc;
        boolean isShow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isShow=false;
            this.tvName=itemView.findViewById(R.id.tv_event_name);
            this.tvDesc=itemView.findViewById(R.id.tv_event_desc);
            this.tvDate=itemView.findViewById(R.id.tv_event_date);
            this.tvID=itemView.findViewById(R.id.tv_event_id);
            this.cardView=itemView.findViewById(R.id.cv_event);
            this.tvLoc = itemView.findViewById(R.id.tv_event_loc);
            this.ll_loc=itemView.findViewById(R.id.ll_loc);
            ll_loc.setVisibility(View.GONE);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(myEvents.get(getAdapterPosition()).getId(), 630, 0, "Edit");
            menu.add(myEvents.get(getAdapterPosition()).getId(), 631, 0, "Delete");


        }
    }
}
