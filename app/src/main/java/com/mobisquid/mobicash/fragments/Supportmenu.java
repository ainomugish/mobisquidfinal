package com.mobisquid.mobicash.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.MainSupportChat;
import com.mobisquid.mobicash.model.SupportObj;
import com.mobisquid.mobicash.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Supportmenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Supportmenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Supportmenu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    SupportObj user;
    View rootview;

    public Supportmenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Supportmenu.
     */
    // TODO: Rename and change types and number of parameters
    public static Supportmenu newInstance(String param1, String param2) {
        Supportmenu fragment = new Supportmenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            user = new Gson().fromJson(getArguments().getString("user"),SupportObj.class);
        }else{
            user = new SupportObj();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_supportmenu, container, false);
        rootview.findViewById(R.id.frq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.changeFragment(getActivity()
                        ,new Frequentlyasked(),getActivity().getSupportFragmentManager().beginTransaction()
                        ,null,R.id.support_container,true,true);
            }
        });
        rootview.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getFullname()==null){
                    Bundle extras = new Bundle();
                    extras.putString("layout","userdetails");

                  Utils.changeFragment(getActivity()
                          ,new Fragment_Userdetails(),getActivity().getSupportFragmentManager().beginTransaction()
                          ,extras,R.id.support_container,true,true);


                }else{
                    Bundle extras = new Bundle();
                    extras.putString("user",new Gson().toJson(user));
                    extras.putString("layout","chatring");
                    Utils.changeFragment(getActivity()
                            ,new Fragment_Userdetails(),getActivity().getSupportFragmentManager().beginTransaction()
                            ,extras,R.id.support_container,true,false);
                   /* Intent notice = new Intent(getActivity(), MainSupportChat.class);
                    notice.putExtra("user",new Gson().toJson(user));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), getActivity().findViewById(R.id.chaticon), "chat");
                    ActivityCompat.startActivity(getActivity(), notice, options.toBundle());*/
                }

            }
        });
        rootview.findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] option = {"Direct call","Built-in calls"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select an option")
                        .setItems(option, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });
                builder.create().show();
            }
        });
        rootview.findViewById(R.id.video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootview;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
