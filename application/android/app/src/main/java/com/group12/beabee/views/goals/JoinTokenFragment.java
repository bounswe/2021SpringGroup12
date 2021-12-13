package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GroupGoalDTO;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.SubgoalDetail;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinTokenFragment extends BaseInnerFragment {
    @BindView(R.id.et_description)
    @Nullable
    EditText ettoken;

    public static JoinTokenFragment newInstance() {
        JoinTokenFragment fragment = new JoinTokenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.OnlyBack;
    }

    @Override
    protected String GetPageTitle() {
        return "Join to a Group Goal";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_join_token;
    }

    //btn_token
    /*@POST("groupgoals/{user_id}/join")
    Call<BasicResponse> joinGG(@Path("user_id") int user_id, @Body GroupGoalDTO ggDTO);*/
    @OnClick(R.id.btn_token)
    @Optional
    public void joinGroup(View view) {
        String token=ettoken.getText().toString();
        service.joinGG(BeABeeApplication.userId,token).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Utils.ShowErrorToast(getContext(), "You joined to a Group Goal!");
                    GoBack();
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }
}
//hHCWWYWwaCpXEgsFgRASLJ