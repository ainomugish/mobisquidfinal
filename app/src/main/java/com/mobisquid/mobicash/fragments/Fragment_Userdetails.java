package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.MainSupportChat;
import com.mobisquid.mobicash.model.SupportObj;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;
import com.mobisquid.mobicash.wedget.drawer.views.GifImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_Userdetails extends Fragment {
    EditText mobile,name;
    Vars vars;
    View rootview;
    TextInputLayout inp_username,inp_number;
    SupportObj user;
    String whichlayout=null;
    GifImageView gifImageView;
    TextView counter,username;
    int i =120;
    CountDownTimer cdt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        vars.log("counrty====code here====="+vars.country_code);
        if (getArguments() != null) {
            whichlayout = getArguments().getString("layout");
            user = new Gson().fromJson(getArguments().getString("user"),SupportObj.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if(whichlayout!=null&&whichlayout.equalsIgnoreCase("userdetails")){
            rootview = inflater.inflate(R.layout.fragment_user_support, container, false);
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Support Details");
            name =(EditText) rootview.findViewById(R.id.username);
            mobile =(EditText) rootview.findViewById(R.id.mobile);
            inp_username = (TextInputLayout) rootview.findViewById(R.id.inp_username);
            inp_number= (TextInputLayout) rootview.findViewById(R.id.inp_number);

            Utils.hidekBoard(rootview.findViewById(R.id.drawer_layout),getActivity());
            rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
            rootview.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(name.getText().toString().isEmpty()){
                        inp_username.setError("Name required");
                    }else if(vars.country_code!=null &&
                            !vars.Checknumber(getActivity(),vars.country_code,mobile.getText().toString().trim())){
                        inp_username.setErrorEnabled(false);
                        inp_number.setError("Invalid number");
                    }else {
                        user = new SupportObj();
                        user.setFullname(name.getText().toString().trim());
                        user.setMobile(mobile.getText().toString().trim());
                        Bundle extras = new Bundle();
                        extras.putString("user",new Gson().toJson(user));
                        extras.putString("layout","chatring");
                        Utils.changeFragment(getActivity()
                                ,new Fragment_Userdetails(),getActivity().getSupportFragmentManager().beginTransaction()
                                ,extras,R.id.support_container,true,false);
                      /*  Intent notice = new Intent(getActivity(), MainSupportChat.class);
                        notice.putExtra("user", new Gson().toJson(user));
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                getActivity().findViewById(R.id.chaticon), "chat");
                        ActivityCompat.startActivity(getActivity(), notice, options.toBundle());*/
                    }
                }
            });
        }else if(whichlayout!=null&&whichlayout.equalsIgnoreCase("chatring")){
            rootview = inflater.inflate(R.layout.fragment_chatring, container, false);
            counter =(TextView) rootview.findViewById(R.id.counter);
            username = (TextView) rootview.findViewById(R.id.username);
            username.setText("Hello "+user.getFullname()+",");
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Connecting you ...");
            gifImageView = (GifImageView) rootview.findViewById(R.id.GifImageView);
            gifImageView.setGifImageResource(R.drawable.chatgif);
            rootview.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
            //counter stuff
            int oneMin= 120 * 1500;
            cdt = new CountDownTimer(oneMin,1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    i--;
                    counter.setText(""+i);
                    if(i==0){
                        counter.setText("0");
                        cdt.cancel();
                        if(getActivity()!=null){
                            getActivity().finish();
                        }

                    }
                   /* if(i==10){
                        counter.setBackgroundResource(R.drawable.roundcircle);
                    }
                    if(i==20){
                        counter.setBackgroundResource(R.drawable.blueround);
                    }*/
                }

                @Override
                public void onFinish() {
                    //Do what you want
                    i--;
                    counter.setText("0");
                }
            };
            cdt.start();

        }


        return rootview;
    }


    @Override
    public void onResume(){
        super.onResume();


    }

}
