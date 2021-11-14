package com.group12.beabee.views.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.RoutineShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoutineCardViewAdapter extends RecyclerView.Adapter<RoutineCardViewAdapter.ViewHolder> {


    private List<RoutineShort> routineShortList;
    private IOnRoutineClickedListener onItemClickedListener;

    public void setData(List<RoutineShort> routineShorts){
        routineShortList = routineShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnRoutineClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(routineShortList.get(position));
    }

    @Override
    public int getItemCount() {

        return routineShortList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvtitle;
        @BindView(R.id.tv_description)
        TextView tvdescription;
        @BindView(R.id.item_parent)
        View itemParent;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void BindData(RoutineShort routineShort){
            tvtitle.setText(routineShort.title);
            tvdescription.setText(routineShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnRoutineClicked(routineShort.id));
        }

    }
}
