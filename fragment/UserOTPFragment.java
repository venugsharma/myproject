package com.tattleup.app.tattleup.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserOTPFragment extends BaseFragment implements View.OnClickListener {

    Button btnSendOTP,btnVerify;
    private View rootView;


    public UserOTPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tattleup_dialog_otp_check, container, false);
        //
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);


        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
         btnVerify = (Button) rootView.findViewById(R.id.btn_verify);
        btnSendOTP = (Button) rootView.findViewById(R.id.btn_sendOTP);


        btnSendOTP.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify:
                UserAddressFragment newUserAddressFragment = new UserAddressFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_login, newUserAddressFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.btn_sendOTP:



            default:
                break;

        }
    }
}

