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
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 16/12/2017.
 */

public class StateUseByDateAdapterRecyclerView extends RecyclerView.Adapter<StateUseByDateAdapterRecyclerView.StateUseByDateViewHolder> {

    private ArrayList<StateUse> stateUses;
    private int resource;
    private Activity activity;
    private int c;

    public StateUseByDateAdapterRecyclerView(ArrayList<StateUse> stateUses, int resource, Activity activity) {
        this.stateUses = stateUses;
        this.resource = resource;
        this.activity = activity;
        c = 1;
    }

    @Override
    public StateUseByDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new StateUseByDateAdapterRecyclerView.StateUseByDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateUseByDateViewHolder holder, int position) {
        StateUse stateUse = stateUses.get(position);
        holder.txtNum.setText("#"+c);
        holder.txtNameHistoric.setText(stateUse.getNameApplication());
        if (stateUse.getImageApp() != null || !stateUse.getImageApp().equals("")) {
            try {
                Drawable icon = activity.getPackageManager().getApplicationIcon(stateUse.getImageApp());
                holder.imgAppHistoric.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        holder.txtMode.setText("Tiempo de uso:");
        holder.imgIconHistoric.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_access_time));
        holder.txtValueHistoric.setText(Converts.convertLongToTimeChar(stateUse.getUseTime()));
        holder.imgIconHistoric.setColorFilter(activity.getResources().getColor(android.R.color.holo_green_light));
        c++;
    }

    @Override
    public int getItemCount() {
        return stateUses.size();
    }

    public class StateUseByDateViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNum;
        private ImageView imgAppHistoric;
        private TextView txtNameHistoric;
        private TextView txtMode;
        private ImageView imgIconHistoric;
        private TextView txtValueHistoric;
        private TextView txtPositionHistoric;

        public StateUseByDateViewHolder(View itemView) {
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
