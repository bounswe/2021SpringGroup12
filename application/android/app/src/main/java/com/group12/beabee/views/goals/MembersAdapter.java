package com.group12.beabee.views.goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private List<User> memberList;
    private IOnMemberListClickedListener onItemClickedListener;

    public void setData(List<User> goalShorts){
        memberList = goalShorts;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(memberList.get(position));
    }

    @Override
    public int getItemCount() {
        return memberList!=null ? memberList.size():0;
    }

    public void setItemClickListener(IOnMemberListClickedListener listener) {
        onItemClickedListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_member_name)
        TextView tvMember;
        @BindView(R.id.item_parent)
        View itemParent;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void BindData(User goalShort) {

            tvMember.setText(goalShort.username);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnMemberListClicked(goalShort.user_id));
        }
    }
}
