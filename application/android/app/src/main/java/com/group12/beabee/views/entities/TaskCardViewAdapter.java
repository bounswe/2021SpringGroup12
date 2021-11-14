package com.group12.beabee.views.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.TaskShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskCardViewAdapter extends RecyclerView.Adapter<TaskCardViewAdapter.ViewHolder> {


    private List<TaskShort> taskShortList;
    private IOnTaskClickedListener onItemClickedListener;

    public void setData(List<TaskShort> taskShorts){
        taskShortList = taskShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnTaskClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(taskShortList.get(position));
    }

    @Override
    public int getItemCount() {

        return taskShortList.size();
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

        public void BindData(TaskShort taskShort){
            tvtitle.setText(taskShort.title);
            tvdescription.setText(taskShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnTaskClicked(taskShort.id));
        }

    }
}
