package com.tattleup.app.tattleup.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.DatePickerFragment;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 9/16/2017.
 */

public class UserSignUpFragment extends BaseFragment implements View.OnClickListener {

    private TattleUp tattleUp;

    private View rootView;
    private String stringDate = "";
    private EditText txtUserDob;

    private EditText txtUserEmail, txtUserFirstName, txtUserLastName, txtUserPassword,txtUserConfirmPassword;
    private RadioButton radioMale, radioFemale, radioOthers;

    private RadioGroup genderRadioGroup;

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "http://api.tattleup.leoinfotech.in/index.php/api/version1/mRegistration";
    ProgressDialog progressDialog;
    String fname,email,dob,password,lname,cpassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_signup, container, false);
        //
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);


        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        txtUserDob = (EditText) rootView.findViewById(R.id.txtUserDOB);
        txtUserDob.setOnClickListener(this);
        txtUserDob.addTextChangedListener(tw);
        txtUserFirstName = (EditText) rootView.findViewById(R.id.txtUser_FirstName);
        txtUserLastName = (EditText) rootView.findViewById(R.id.txtUserLastName);
        txtUserPassword = (EditText) rootView.findViewById(R.id.txtUserPassword);
        txtUserConfirmPassword = (EditText) rootView.findViewById(R.id.txtUserConfirmPassword);
        txtUserEmail = (EditText) rootView.findViewById(R.id.txtUserEmail);
        radioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) rootView.findViewById(R.id.radioFemale);
        radioOthers = (RadioButton) rootView.findViewById(R.id.radioOthers);

        Button btnsignup = (Button) rootView.findViewById(R.id.btnSign_up);
        rootView.findViewById(R.id.btnSign_up).setOnClickListener(this);
        String email = txtUserEmail.getEditableText().toString().trim();


        ImageView calender = (ImageView) rootView.findViewById(R.id.calender);
        calender.setOnClickListener(this);

        txtUserFirstName.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z ]+")){
                            return cs;
                        }
                        return "";
                    }
                }
        });
        txtUserLastName.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z ]+")){
                            return cs;
                        }
                        return "";
                    }
                }
        });

        return rootView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calender:
                DatePickerFragment datePickerFragment1 = new DatePickerFragment();
                String[] birthDate = txtUserDob.getText().toString().split("/");
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

            case R.id.btnSign_up:
                if(validate()){
                    setProfileDetails();
                }


                break;

            default:
                break;
        }
    }



    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = /*"^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,3}$";*/
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z]+(\\.[COMcom]{3})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidFname(String fname) {

        String FNAME_PATTERN = "^[a-zA-Z]{1,8}";

        Pattern pattern = Pattern.compile(FNAME_PATTERN);
        Matcher matcher = pattern.matcher(fname);
        return matcher.matches();

    }

    private boolean isValidLname(String lname) {

        String LNAME_PATTERN = "^[a-zA-Z]{1,8}";


        Pattern pattern = Pattern.compile(LNAME_PATTERN);
        Matcher matcher = pattern.matcher(lname);
        return matcher.matches();


    }

    private boolean isValidDOB(String dob) {
        if (dob != null && dob.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isValidPassword(String password) {

        String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+_?-])(?=\\S+$).{8,}";


        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }
//    private boolean isValidcPassword(String cpassword) {
////
////        String CPASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+_?-])(?=\\S+$).{8,}";
//
//
//        Pattern pattern = Pattern.compile(CPASSWORD_PATTERN);
//        Matcher matcher = pattern.matcher(cpassword);
//        return matcher.matches();
//
//    }

    private boolean validate() {
        boolean temp=true;
        fname = txtUserFirstName.getText().toString();
        password = txtUserPassword.getText().toString();
        cpassword = txtUserConfirmPassword.getText().toString();
        email = txtUserEmail.getText().toString();
        lname= txtUserLastName.getText().toString();
        dob = txtUserDob.getText().toString();

        tattleUp.setDATA(AppUtils.LOGIN_USER_ADDRESS, dob);
        if(!password.equals(cpassword)){
//            Toast.makeText(getActivity(),"Password Not matching",Toast.LENGTH_SHORT).show();
            txtUserConfirmPassword.setError("Password Not matching");
            temp=false;
        }

        if (!isValidEmail(email)) {
            txtUserEmail.setError("Invalid Email");
            temp=false;

        }

        if (!isValidLname(lname)) {
            txtUserLastName.setError("Invalid LastName");
            temp=false;

        }

        if (!isValidFname(fname)) {
            txtUserFirstName.setError("Invalid FirstName");
            temp=false;

        }

        if (!isValidPassword(password)) {
            txtUserPassword.setError("password must be eight characters including one uppercase letter,one lowercase letter, one special character");
            temp=false;

        }
//        if (!isValidcPassword(cpassword)) {
//            txtUserConfirmPassword.setError("Invalid confirm Password");
//            temp=false;
//
//        }
        if (!isValidDOB(dob)) {
            txtUserDob.setError("Invalid DOB");
            temp=false;

        }

        return temp;

    }
    private void setProfileDetails() {

        String name=fname+' '+lname;

        String chkGender = "";
        if (radioMale.isChecked()) {
            chkGender = "1";
        } else if (radioFemale.isChecked()) {
            chkGender = "2";
        } else if (radioOthers.isChecked()) {
            chkGender = "3";
        }
        tattleUp.setDATA(AppUtils.LOGIN_USER_GENDER,chkGender);
        String subUrl = "&UserName=" + URLEncoder.encode(name)+ "&EmailAddress=" + URLEncoder.encode(email) + "&Password=" +
                URLEncoder.encode(password) +"&Dob=" + URLEncoder.encode(dob) + "&GenderId=" + URLEncoder.encode(chkGender);

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

                        String user_id = obj.getString("user_id");
                        //    tattleUp.setDATA(AppUtils.LOGIN_USER_ID, user_id);

                        if(user_id != null){


                            UserSignInFragment userSignInFragment = new UserSignInFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame_login, userSignInFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }else{


                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"Email Id already Exist", Toast.LENGTH_SHORT).show();

                        // goToApp();
                        Log.e("Else error", "ErrorException");
                        e.printStackTrace();
                    }

                }
                return result;
            }
        };

        String url = AppUtils.SERVICE_BASE_API + "mRegistration?" + subUrl;
        BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
        basicPostAsync.execute();
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    if(mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                txtUserDob.setText(current);
                txtUserDob.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };
}