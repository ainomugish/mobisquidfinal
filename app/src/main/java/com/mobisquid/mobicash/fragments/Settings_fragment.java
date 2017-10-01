package com.mobisquid.mobicash.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mobisquid.mobicash.BuildConfig;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatMain;
import com.mobisquid.mobicash.activities.QrcodeUpdates;
import com.mobisquid.mobicash.activities.RegistrationActivity;
import com.mobisquid.mobicash.activities.SmsActivity;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = Settings_fragment.class.getSimpleName();
    View rootview;
    Vars vars;
    Alerter alerter;
    SwitchCompat switchCompat;
    TextView swittext;

    private OnFragmentInteractionListener mListener;

    public Settings_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings_fragment newInstance(String param1, String param2) {
        Settings_fragment fragment = new Settings_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        alerter = new Alerter(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_settings_fragment, container, false);
        TextView version = (TextView) rootview.findViewById(R.id.version);
        swittext= (TextView) rootview.findViewById(R.id.swittext);
        switchCompat = (SwitchCompat) rootview.findViewById(R.id.compatSwitch);
        //vars.chaton =true;
        if(vars.chaton){
            switchCompat.setChecked(true);
            swittext.setText("Turn off Chat");
        }else{
            switchCompat.setChecked(false);
            swittext.setText("Turn on Chat");
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vars.log("chats==="+isChecked);
                if(isChecked){
                    swittext.setText("Turn off Chat");
                }else {
                    swittext.setText("Turn on Chat");
                }
                if(vars.chaton && !isChecked){
                    SwitchoffChat();
                }else if(!vars.chaton && isChecked){
                    if(vars.social==null){
                        Intent notice = new Intent(getActivity(), RegistrationActivity.class);
                        notice.putExtra("login","login");
                        notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()
                                , rootview.findViewById(R.id.swittext), "profile");
                        ActivityCompat.startActivity(getActivity(), notice, options.toBundle());

                    }else {
                        Intent notice = new Intent(getActivity(), ChatMain.class);
                        notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.swittext), "money");
                        ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
                    }
                }
            }
        });
        version.setText("Version "+ BuildConfig.VERSION_NAME+" . "+BuildConfig.VERSION_CODE);
        rootview.findViewById(R.id.paymentoption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    Intent notice = new Intent(getActivity(), QrcodeUpdates.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.paymentoption), "payment");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());
                }
            }
        });
        rootview.findViewById(R.id.change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent notice = new Intent(getActivity(),SmsActivity.class);
                notice.putExtra("changepass","changepass");
                notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(), rootview.findViewById(R.id.change_password), "login");
                ActivityCompat.startActivity(getActivity(), notice, options.toBundle());

            }
        });
        rootview.findViewById(R.id.terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    TermsandCondiftions firstfragment = new TermsandCondiftions();
                    firstfragment.setArguments(getArguments());
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_setting, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    TermsandCondiftions firstfragment = new TermsandCondiftions();
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_setting, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        rootview.findViewById(R.id.changelanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Slide slideTransition = new Slide(Gravity.LEFT);
                    slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                    Changelanguage firstfragment = new Changelanguage();
                    firstfragment.setArguments(getArguments());
                    firstfragment.setReenterTransition(slideTransition);
                    firstfragment.setExitTransition(slideTransition);
                    firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_setting, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                } else {
                    System.out.println("===========t============");
                    Changelanguage firstfragment = new Changelanguage();
                    firstfragment.setArguments(getArguments());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_setting, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        rootview.findViewById(R.id.changepin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vars.active==null) {
                    alerter.login("Financial services", "You are currently logged out, please login or register");
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        ChangePin firstfragment = new ChangePin();
                        firstfragment.setArguments(getArguments());
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_setting, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        ChangePin firstfragment = new ChangePin();
                        firstfragment.setArguments(getArguments());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container_setting, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });


        return rootview;
    }
    private void sociallogin(){
        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                .setTitleText("NOTE")
                .setContentText("Please login to social or create an account first.")
                .setConfirmText("Login or create")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                }).show();
    }

    private void SwitchoffChat(){
        new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Please note!!")
                .setContentText("You are about to turn off social chat,\n you won't be able to receive or send messages\n" +
                        "from anyone.")
                .setConfirmText("Turn off")
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        swittext.setText("Turn off Chat");
                        switchCompat.setChecked(true);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        swittext.setText("Turn on Chat");
                        switchCompat.setChecked(false);
                        deleteAccount();
                    }
                }).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void deleteAccount(){
        AccountManager am = AccountManager.get(getActivity());
        Account[] accounts = am.getAccounts();
        for(Account ac:accounts){
            Log.d(TAG,"AC=="+ac.type);
            if(ac.type.equalsIgnoreCase(Globals.ACCOUNT_TYPE)){
                  if (accounts.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                am.removeAccount(accounts[0],  getActivity(), new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bnd = future.getResult();
                            Log.d(TAG, String.valueOf(bnd));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }else {
                Account accountToRemove = accounts[0];
                am.removeAccount(accountToRemove, null, null);
            }
        }

            }

        }
        vars.edit.remove("chaton");
        vars.edit.apply();
        ContactDetailsDB.deleteAll(ContactDetailsDB.class);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

        super.onResume();
    }
}
