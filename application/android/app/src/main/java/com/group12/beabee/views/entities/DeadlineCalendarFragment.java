package com.group12.beabee.views.entities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.group12.beabee.R;

import java.util.Calendar;

import butterknife.ButterKnife;

public class DeadlineCalendarFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener listener;
    public DeadlineCalendarFragment(DatePickerDialog.OnDateSetListener l) {
        listener = l;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }


}
