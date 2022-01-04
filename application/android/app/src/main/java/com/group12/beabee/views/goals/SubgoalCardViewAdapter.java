package com.group12.beabee.views.goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.responses.SubgoalShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubgoalCardViewAdapter extends RecyclerView.Adapter<SubgoalCardViewAdapter.ViewHolder> {

    private List<SubgoalShort> subgoalShortList;
    private IOnSubgoalClickedListener onItemClickedListener;

    public void setData(List<SubgoalShort> subgoalShorts){
        subgoalShortList = subgoalShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnSubgoalClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subgoal_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(subgoalShortList.get(position));
    }

    @Override
    public int getItemCount() {
        return subgoalShortList!=null ? subgoalShortList.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.item_parent)
        View itemParent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void BindData(SubgoalShort subgoalShort) {
            tvTitle.setText(subgoalShort.title);
            tvDescription.setText(subgoalShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnSubgoalClicked(subgoalShort.id));
        }
    }
}
