package com.group12.beabee.views.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.TagShort;
import com.group12.beabee.models.TaskShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagCardViewAdapter extends RecyclerView.Adapter<TagCardViewAdapter.ViewHolder> {


    private List<TagShort> tagShortList;
    private IOnTagClickedListener onItemClickedListener;

    public void setData(List<TagShort> tagShorts){
        tagShortList = tagShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnTagClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindData(tagShortList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagShortList!=null ? tagShortList.size():0;
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvtitle;
        @BindView(R.id.item_parent)
        View itemParent;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void BindData(TagShort tagShort){
            tvtitle.setText(tagShort.title);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnTagClicked(tagShort.id));
        }

    }
}
