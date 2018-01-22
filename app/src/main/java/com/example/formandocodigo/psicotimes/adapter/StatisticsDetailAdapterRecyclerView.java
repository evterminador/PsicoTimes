package com.example.formandocodigo.psicotimes.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.PackageInformation;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 20/01/2018.
 */

public class StatisticsDetailAdapterRecyclerView extends RecyclerView.Adapter<StatisticsDetailAdapterRecyclerView.StatisticsDetailViewHolder> {

    private ArrayList<StatisticsDetail> statisticsDetails;
    private int resource;
    private Activity activity;

    public StatisticsDetailAdapterRecyclerView(ArrayList<StatisticsDetail> statisticsDetails, int resource, Activity activity) {
        this.statisticsDetails = statisticsDetails;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public StatisticsDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new StatisticsDetailAdapterRecyclerView.StatisticsDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatisticsDetailViewHolder holder, int position) {
        StatisticsDetail detail = statisticsDetails.get(position);
        holder.txtNum.setText("#" + (position + 1));

        if (detail.getImage() != null || !detail.getImage().equals("")) {
            try {
                Drawable icon = activity.getPackageManager().getApplicationIcon(detail.getImage());
                holder.imgApp.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        holder.txtNameApp.setText(detail.getNameApp());
        holder.txtValueTimeUse.setText(Converts.convertLongToTimeChar(detail.getTimeUse()));
        holder.txtValueQuantity.setText(detail.getQuantity() + "");
    }

    @Override
    public int getItemCount() {
        return statisticsDetails.size();
    }

    public class StatisticsDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNum;
        private ImageView imgApp;
        private TextView txtNameApp;
        private TextView txtValueTimeUse;
        private TextView txtValueQuantity;

        public StatisticsDetailViewHolder(View itemView) {
            super(itemView);

            txtNum = itemView.findViewById(R.id.txt_num);
            imgApp = itemView.findViewById(R.id.img_app);
            txtNameApp = itemView.findViewById(R.id.txt_name_app);
            txtValueTimeUse = itemView.findViewById(R.id.txt_value_time_use);
            txtValueQuantity = itemView.findViewById(R.id.txt_value_quantity);
        }
    }
}
