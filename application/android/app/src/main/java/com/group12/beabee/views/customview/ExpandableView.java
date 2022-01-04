package com.group12.beabee.views.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.ExpandableListItem;

import java.util.List;

public class ExpandableView extends FrameLayout {
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private View noContent;
    private View expandableTitle;
    private View expandablePart;
    private ImageView ivExpandIcon;

    private boolean isExpanded = false;
    private ExpandableListAdapter rvAdapter;

    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExpandableView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_expandable_view, this);
        recyclerView = findViewById(R.id.rv_expanded_list);
        rvAdapter = new ExpandableListAdapter();
        recyclerView.setAdapter(rvAdapter);
        tvTitle = findViewById(R.id.tv_title);
        noContent = findViewById(R.id.tv_no_content);
        ivExpandIcon = findViewById(R.id.iv_expandable_icon);
        expandableTitle = findViewById(R.id.titlePart);
        expandablePart = findViewById(R.id.expandable_part);

        expandableTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded)
                    Collapse();
                else
                    Expand();
            }
        });
        SetDataList(null);
        Collapse();
    }

    public void Collapse(){
        expandablePart.setVisibility(GONE);
        ivExpandIcon.setImageResource(R.drawable.ic_drop_down);
        isExpanded = false;
    }

    public void Expand(){
        isExpanded = true;
        expandablePart.setVisibility(VISIBLE);
        ivExpandIcon.setImageResource(R.drawable.ic_drop_up);
    }

    public void SetTitle(String title){
        tvTitle.setText(title);
    }

    public void SetDataList(List<ExpandableListItem> dataList){
        if (dataList==null || dataList.size()==0){
            noContent.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        }else{
            rvAdapter.setData(dataList);
            noContent.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }

    }
}
