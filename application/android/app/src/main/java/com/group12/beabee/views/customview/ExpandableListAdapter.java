package com.group12.beabee.views.customview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.ExpandableListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandableListAdapter extends RecyclerView.Adapter<ExpandableListAdapter.ViewHolder> {

    private List<ExpandableListItem> dataList;

    public void setData(List<ExpandableListItem> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expandable_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList!=null ? dataList.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_goal_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void BindData(ExpandableListItem goalShort) {
            tvTitle.setText(goalShort.title);
            tvDescription.setText(goalShort.description);
        }
    }
}
