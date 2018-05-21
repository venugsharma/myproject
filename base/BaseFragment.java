package com.tattleup.app.tattleup.base;

/**
 * Created by Shiva on 6/21/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

//import sic.co.sadmin.helper.AppUtils;


public class BaseFragment extends Fragment {
    public static final String LOG_TYPE_VERBOSE = "verbose";
    public static final String LOG_TYPE_WARNING = "warning";
    public static final String LOG_TYPE_ERROR = "error";
    private static final String ARG_POSITION = "position";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void initializeFragment(View rootView) {
        initializeComponents(rootView);
        bindEvents();
    }

    public View initializeComponents(View rootView) {

        return rootView;
    }

    public void log(String message, String type) {
        if (getActivity() == null) return; // Fragment not active anymore, bail out
//        AppUtils.log(getActivity(), message, type);
    }

    public void bindEvents() {

    }

    public void showShortToast(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException ex) {
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputmethodmanager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputmethodmanager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlertDialog showMessage(String title, String message, String positiveButtonText,
                                   DialogInterface.OnClickListener positiveCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveButtonText, positiveCallback);
        AlertDialog alert = builder.create();
        return alert;
    }

    public static Fragment newInstance(int position) {
        Fragment f = new Fragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }


}