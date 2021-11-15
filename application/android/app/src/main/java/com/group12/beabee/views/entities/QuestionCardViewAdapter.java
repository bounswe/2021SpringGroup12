package com.group12.beabee.views.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.QuestionShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionCardViewAdapter extends RecyclerView.Adapter<QuestionCardViewAdapter.ViewHolder> {


    private List<QuestionShort> questionShortList;
    private IOnQuestionClickedListener onItemClickedListener;

    public void setData(List<QuestionShort> questionShorts){
        questionShortList = questionShorts;
        notifyDataSetChanged();
    }

    public void setItemClickListener(IOnQuestionClickedListener listener){
        onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public QuestionCardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_cardview, parent, false);
        return new QuestionCardViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionCardViewAdapter.ViewHolder holder, int position) {
        holder.BindData(questionShortList.get(position));
    }

    @Override
    public int getItemCount() {

        return 0;//questionShortList.size();
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

        public void BindData(QuestionShort questionShort){
            tvtitle.setText(questionShort.title);
            tvdescription.setText(questionShort.description);
            itemParent.setOnClickListener(v -> onItemClickedListener.OnQuestionClicked(questionShort.id));
        }

    }
}
