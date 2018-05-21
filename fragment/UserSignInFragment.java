package com.tattleup.app.tattleup.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignInFragment extends BaseFragment {
    private EditText txtUserEmail, txtUserPassword;
    private TattleUp tattleUp;
    private View rootView;
    String email,password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_sign_in, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        txtUserPassword = (EditText) rootView.findViewById(R.id.txtPassword);
        txtUserEmail = (EditText) rootView.findViewById(R.id.txtEmail);
        Button signin = (Button) rootView.findViewById(R.id.btnSign_In);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(validate()){
                    setProfileDetails();
                }
            }
        });
        return rootView;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private boolean isValidPassword(String password) {
        if (password != null && password.length() > 6) {
            return true;
        }
        return false;
    }


    private boolean validate() {
        boolean temp=true;
        password = txtUserPassword.getText().toString();
        email = txtUserEmail.getText().toString();


        if (!isValidEmail(email)) {
            txtUserEmail.setError("Invalid Email");
            temp=false;

        }


        if (!isValidPassword(password)) {
            txtUserPassword.setError("Invalid Password");
            temp=false;

        }


        return temp;

    }
    private void setProfileDetails() {

        String subUrl =  "&EmailAddress=" + URLEncoder.encode(email) + "&Password=" +
                URLEncoder.encode(password) ;

        BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {

            @Override
            public String OnAsynResult(String result) {

                if (result.equals("error")) {
                    Log.e("error", "Error");
                } else {
                    try {
                        Log.e("result", result);
                        Log.e("Else error", "Error");
                        String re = result;
                        JSONArray jsonArray = null;

                        JSONObject obj = new JSONObject(result);
                        //     if (jsonObject.has("SubcategoriesList")) {
                        //Log.e("finalResult", status);


                        JSONObject c = obj.getJSONObject("userInfo");

                        // Storing  JSON item in a Variable

                        String user_id = c.getString("Id");
                        String user_name = c.getString("UserName");
                        String user_email = c.getString("EmailAddress");
                        String user_Dob=c.getString("Dob");

                        tattleUp.setDATA(AppUtils.LOGIN_USER_EMAIL, user_email);
                        tattleUp.setDATA(AppUtils.LOGIN_USER_NAME, user_name);
                        tattleUp.setDATA(AppUtils.LOGIN_USER_ID, user_id);
                        tattleUp.setUserLogIn(AppUtils.IS_USER_LOGGED_IN, true);

                        String status = obj.getString("status");

                        if(status == "true"){


                            UserAddressFragment userAddressFragment = new UserAddressFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame_login, userAddressFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }else{


                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"Email and Password not match", Toast.LENGTH_SHORT).show();

                        // goToApp();
                        Log.e("Else error", "ErrorException");
                        e.printStackTrace();
                    }

                }
                return result;
            }
        };

        String url = AppUtils.SERVICE_BASE_API + "login?" + subUrl;
        BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
        basicPostAsync.execute();
    }


}