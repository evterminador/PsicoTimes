package com.example.formandocodigo.psicotimes.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.PackageInformation;
import com.example.formandocodigo.psicotimes.view.RecordDetailActivity;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 27/12/2017.
 */

public class HistoricStateAdapterRecyclerView extends RecyclerView.Adapter<HistoricStateAdapterRecyclerView.HistoricStateViewHolder> {

    private ArrayList<HistoricState> historicStates;
    private int resource;
    private Activity activity;

    private PackageInformation packageInformation;

    public HistoricStateAdapterRecyclerView(ArrayList<HistoricState> historicStates, int resource, Activity activity) {
        this.historicStates = historicStates;
        this.resource = resource;
        this.activity = activity;
        packageInformation = new PackageInformation(activity);
    }

    @Override
    public HistoricStateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new HistoricStateAdapterRecyclerView.HistoricStateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoricStateViewHolder holder, int position) {
        HistoricState historicState = historicStates.get(position);
        holder.txtNum.setText("#"+(position + 1));

        holder.txtNameTopHistoric.setText(historicState.getNameAppTop());

        Drawable icon = packageInformation.getIconAppByName(historicState.getNameAppTop());

        if (icon != null) {
            holder.imgAppHistoric.setImageDrawable(icon);
        }

        holder.txtDateHistoric.setText(Converts.convertTimestampToStringShort(historicState.getCreated_at()));

        holder.txtUseTimeHistoric.setText(Converts.convertLongToTimeChar(historicState.getTimeUse()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RecordDetailActivity.class);
                intent.putExtra("id", historicState.getId());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Explode explode = new Explode();
                    explode.setDuration(1000);
                    activity.getWindow().setExitTransition(explode);
                    activity.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, v, activity.getString(R.string.transitionname_record)).toBundle());

                }else {
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historicStates.size();
    }

    public class HistoricStateViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNum;
        private ImageView imgAppHistoric;
        private TextView txtNameTopHistoric;
        private TextView txtDateHistoric;
        private TextView txtUseTimeHistoric;

        public HistoricStateViewHolder(View itemView) {
            super(itemView);

            txtNum = itemView.findViewById(R.id.txt_num);
            imgAppHistoric = itemView.findViewById(R.id.img_app_historic);
            txtNameTopHistoric = itemView.findViewById(R.id.txt_name_top_historic);
            txtDateHistoric = itemView.findViewById(R.id.txt_date_historic);
            txtUseTimeHistoric = itemView.findViewById(R.id.txt_use_time_historic);
        }
    }
}
