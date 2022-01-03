package com.group12.beabee.views.goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.responses.GoalShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoalsPrototypeAdapter1 extends RecyclerView.Adapter<GoalsPrototypeAdapter1.ViewHolder> {

    private List<GoalShort> goalShortList;
    private IOnGoalClickedListener onItemClickedListener;

    public void setData(List<GoalShort> goalShorts){
        goalShortList = goalShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnGoalClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal_list_marketplace, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(goalShortList.get(position));
    }

    @Override
    public int getItemCount() {
        return goalShortList!=null ? goalShortList.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_goal_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.item_parent)
        View itemParent;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.download_number)
        TextView downloadnum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void BindData(GoalShort goalShort) {
            tvTitle.setText(goalShort.title);
            tvDescription.setText(goalShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnGoalClicked(goalShort.id));
            tvUsername.setText(goalShort.username);
            //downloadnum.setText(goalShort.download_count);
        }
    }
}
