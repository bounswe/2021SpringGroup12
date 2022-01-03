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

public class GoalsPrototypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GoalShort> goalShortList1;
    private List<GoalShort> goalShortList2;
    private IOnGoalClickedListener onItemClickedListener;

    public void setData1(List<GoalShort> goalShorts1 ){
        goalShortList1 = goalShorts1;
        notifyDataSetChanged();
    }
    public void setData2(List<GoalShort> goalShorts2 ){
        goalShortList2 = goalShorts2;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnGoalClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal_list_marketplace, parent, false);
            return new ViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_in_rv, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        int idx = getTitleIdx();
        return idx == position ? 1:0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int idx = getTitleIdx();
        if (holder.getItemViewType()==0){
            if (position>idx){
                GoalShort data = goalShortList2.get(position-idx-1);
                ((ViewHolder) holder).BindData(data);
            }else if (position<idx){
                GoalShort data = goalShortList1.get(position);
                ((ViewHolder) holder).BindData(data);
            }
        }
    }

    @Override
    public int getItemCount() {
        int size1 = goalShortList1!=null ? goalShortList1.size():0;
        int size2 = goalShortList2!=null ? goalShortList2.size():0;
        return size1 + size2 + 1;
    }

    private int getTitleIdx(){
        return goalShortList1 == null ? 0 : goalShortList1.size();
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void BindData(GoalShort goalShort) {
            tvTitle.setText(goalShort.title);
            tvDescription.setText(goalShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnGoalClicked(goalShort.id));
            tvUsername.setText(goalShort.username);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
