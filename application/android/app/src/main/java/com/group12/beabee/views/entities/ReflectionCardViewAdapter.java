package com.group12.beabee.views.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.ReflectionShort;
import com.group12.beabee.models.responses.Entity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReflectionCardViewAdapter extends RecyclerView.Adapter<ReflectionCardViewAdapter.ViewHolder> {


    private List<Entity> reflectionShortList;
    private IOnReflectionClickedListener onItemClickedListener;

    public void setData(List<Entity> reflectionShorts){
        reflectionShortList = reflectionShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnReflectionClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public ReflectionCardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reflection_cardview, parent, false);
        return new ReflectionCardViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReflectionCardViewAdapter.ViewHolder holder, int position) {
        holder.BindData(reflectionShortList.get(position));
    }

    @Override
    public int getItemCount() {
        return reflectionShortList!=null ? reflectionShortList.size():0;
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

        public void BindData(Entity reflectionShort){
            tvtitle.setText(reflectionShort.title);
            tvdescription.setText(reflectionShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnReflectionClicked(reflectionShort.id));
        }

    }
}
