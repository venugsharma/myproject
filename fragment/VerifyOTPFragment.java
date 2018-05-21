package com.tattleup.app.tattleup.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by admin on 9/16/2017.
 */

public class VerifyOTPFragment extends BaseFragment implements View.OnClickListener {


    private com.kosalgeek.android.photoutil.ImageLoader imageLoader;
    private TattleUp tattleUp;

    private View rootView;

    private EditText txtUserDob;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tattleup_dialog_otp_check, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        ((MainActivity) getActivity()).setPageTitle("");

        txtUserDob = (EditText) rootView.findViewById(R.id.txtUserDOB);
        txtUserDob.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtUserDob:
                DatePickerFragment datePickerFragment1 = new DatePickerFragment();
                String[] birthDate = txtUserDob.getText().toString().split("-");
                Integer currentyear = 1985;
                Integer month = 0;
                Integer day = 1;
                try {
                    if (birthDate[0] != "0000") {
                        currentyear = Integer.parseInt(birthDate[0]);
                        month = Integer.parseInt(birthDate[1]) - 1;
                        day = Integer.parseInt(birthDate[2]);
                    }
                } catch (NumberFormatException nFEx) {
                }
                datePickerFragment1.setDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Log.e("date", year + " " + monthOfYear + " " + dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        txtUserDob.setText(dateFormat.format(calendar.getTime()));
                    }
                });

                Bundle bundle1 = new Bundle();
                bundle1.putInt("year", currentyear);
                bundle1.putInt("month", month);
                bundle1.putInt("day", day);
                datePickerFragment1.setArguments(bundle1);
                datePickerFragment1.setMaxDate();
                datePickerFragment1.show(getChildFragmentManager(), "datepicker");
                break;

        }
    }
}





