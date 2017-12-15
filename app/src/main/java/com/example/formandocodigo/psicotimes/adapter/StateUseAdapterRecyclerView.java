package com.example.formandocodigo.psicotimes.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 12/12/2017.
 */

public class StateUseAdapterRecyclerView extends RecyclerView.Adapter<StateUseAdapterRecyclerView.StateUseViewHolder> {

    private ArrayList<StateUse> stateUses;
    private int resource;
    private Activity activity;
    private int c;
    private int mode;

    public StateUseAdapterRecyclerView(ArrayList<StateUse> stateUses, int resource, Activity activity, int mode) {
        this.stateUses = stateUses;
        this.resource = resource;
        this.activity = activity;
        this.mode = mode;
        c = 1;
    }

    @Override
    public StateUseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new StateUseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateUseViewHolder holder, int position) {
        StateUse stateUse = stateUses.get(position);
        holder.txtNum.setText("#"+c);
        holder.txtNameHistoric.setText(stateUse.getNameApplication());
        //Picasso.with(activity).load(stateUse.getImg()).into(holder.imgAppHistoric);
        if (mode == 0) {
            holder.txtMode.setText("Tiempo de uso:");
            holder.imgIconHistoric.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_access_time));
            holder.imgIconHistoric.setColorFilter(activity.getResources().getColor(android.R.color.holo_green_light));
            holder.txtValueHistoric.setText(Converts.convertLongToTimeChar(stateUse.getUseTime()));
        } else if (mode == 1) {
            holder.txtMode.setText("Veces usadas");
            holder.txtValueHistoric.setText(stateUse.getQuantity() + "");
        }
        c++;
    }

    @Override
    public int getItemCount() {
        return stateUses.size();
    }

    public class StateUseViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNum;
        private ImageView imgAppHistoric;
        private TextView txtNameHistoric;
        private TextView txtMode;
        private ImageView imgIconHistoric;
        private TextView txtValueHistoric;
        private TextView txtPositionHistoric;

        public StateUseViewHolder(View itemView) {
            super(itemView);

            txtNum = itemView.findViewById(R.id.txt_num);
            imgAppHistoric = itemView.findViewById(R.id.img_app_historic);
            txtNameHistoric = itemView.findViewById(R.id.txt_name_historic);
            txtMode = itemView.findViewById(R.id.txt_mode);
            imgIconHistoric = itemView.findViewById(R.id.img_icon_historic);
            txtValueHistoric = itemView.findViewById(R.id.txt_value_historic);
            txtPositionHistoric = itemView.findViewById(R.id.txt_position_historic);
        }
    }
}
