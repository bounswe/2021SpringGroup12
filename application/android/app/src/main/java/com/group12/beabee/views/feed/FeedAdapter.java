package com.group12.beabee.views.feed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.User;
import com.group12.beabee.models.responses.ActivityStream;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<ActivityStream> feedList;
    private IOnFeedClickedListener onItemClickedListener;

    public void setData(List<ActivityStream> feed){
        feedList = feed;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_list, parent, false);
        return new FeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        holder.BindData(feedList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedList!=null ? feedList.size():0;
    }

    public void setItemClickListener(IOnFeedClickedListener listener) {
        onItemClickedListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_feedSummary)
        TextView tvFeedSummary;
        @BindView(R.id.arrow_icon)
        View icArrow;
        @BindView(R.id.item_parent)
        View itemParent;
        private int id;
        private int type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemParent.setOnClickListener(view -> {
                if(type < 0)
                    return;

                onItemClickedListener.OnFeedClicked(id, type);
            });
        }


        public void BindData(ActivityStream feed) {
            tvFeedSummary.setText(feed.summary);
            if (feed.type.trim().equals("FOLLOW")) {
                String url = feed.objectschema.url;
                int indexOf = url.lastIndexOf('/');
                id = Integer.parseInt(url.substring(indexOf+1));
                type = 0;
                icArrow.setVisibility(View.VISIBLE);
            }else if (feed.type.trim().equals("ADD") || feed.type.trim().equals("UPDATE")) {
                String url = feed.objectschema.url;
                int indexOf = url.lastIndexOf('/');
                id = Integer.parseInt(url.substring(indexOf+1));
                type = 1;
                icArrow.setVisibility(View.VISIBLE);
            }else {
                type = -1;
                icArrow.setVisibility(View.INVISIBLE);
            }
        }
    }
}

