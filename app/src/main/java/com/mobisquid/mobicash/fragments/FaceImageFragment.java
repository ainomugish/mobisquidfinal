package com.mobisquid.mobicash.fragments;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.BuildConfig;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.ChatsetUp;
import com.mobisquid.mobicash.activities.RegistrationActivity;
import com.mobisquid.mobicash.model.ChatClass;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaceImageFragment extends Fragment  {
    Vars vars;
    View rootview;
    TextView string_id_photo,nextimage;
    ImageView my_id_photo;
    AlertDialog dialog_imag_option;
    Button clicktext;
    String imagepath;
    Uri mImageCaptureUri;
    ChatClass chatClass;
    String mCurrentPhotoPath;
    protected static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =7;
    static final int PICK_FROM_CAMERA = 1;
    static final int PICK_FROM_FILE = 3;
    public FaceImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        if(getArguments()!=null && getArguments().getString("chat")!=null){
            chatClass = new Gson().fromJson(getArguments().getString("chat"),ChatClass.class);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.captureface, container, false);
        captureImageInitialization();
        my_id_photo = (ImageView) rootview.findViewById(R.id.my_id_photo);
        string_id_photo =(TextView) rootview.findViewById(R.id.id_photo);
        clicktext =(Button) rootview.findViewById(R.id.take_photo);
        nextimage=(TextView) rootview.findViewById(R.id.nextimage);
        //nextimage.setVisibility(View.GONE);
        string_id_photo.setText("Would you like to take a profile photo of yourself? This is import during social networking.");

        rootview.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeimage ();
            }
        });
        rootview.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeimage ();
            }
        });
        rootview.findViewById(R.id.small_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeimage ();
            }
        });
        rootview.findViewById(R.id.touch_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                vars.hideKeyboard(view,getActivity());
                return false;
            }
        });
        nextimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagepath==null){
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm!!")
                            .setContentText("Are you sure you don't want to update your profile image?")
                            .setCancelText("May be later")
                            .setConfirmText("Update")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent mainpage = new Intent(getActivity(), ChatsetUp.class);
                                    mainpage.putExtra("welcome","welcome");
                                    mainpage.putExtra("myname",chatClass.getFullname());
                                    mainpage.putExtra("userid",String.valueOf(chatClass.getUserid()));
                                    startActivity(mainpage);
                                    getActivity().finish();
                                }
                            }).show();
                }else{
                    Alerter.showdialog(getActivity());
                    try{

                        String url = Globals.SOCILA_SERV+"entities.chat/edituser";

                        JSONObject json = new JSONObject();

                        json.put("mobile", chatClass.getMobile());
                        json.put("profileurl", imagepath);
                        json.put("userid", chatClass.getUserid());
                        json.put("username", chatClass.getUsername());
                        ConnectionClass.JsonString(Request.Method.PUT,getActivity(), url, json, "CHANGE", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Alerter.stopdialog();
                                try {
                                    JSONObject reader = new JSONObject(result);
                                    if (reader.getString("error").equalsIgnoreCase("no_error")) {
                                        new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Success!")
                                                .setContentText("Your profile updated successfully")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismiss();
                                                            Intent mainpage = new Intent(getActivity(), ChatsetUp.class);
                                                            mainpage.putExtra("myname",chatClass.getFullname());
                                                            mainpage.putExtra("userid",String.valueOf(chatClass.getUserid()));
                                                            mainpage.putExtra("welcome","welcome");
                                                            startActivity(mainpage);
                                                            getActivity().finish();
                                                    }
                                                }).show();

                                    } else if(reader.getString("error").equalsIgnoreCase("error")) {
                                        Alerter.Error(getActivity(), "Error", reader.getString("message"));

                                    } else {
                                        vars.log("return ==== " + result);
                                        Alerter.Error(getActivity(), "Error", "Please check your internet connection");
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return rootview;
    }
    private void takeimage (){
        // String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                vars.log("===allowedddddddd==");

            }else{
                dialog_imag_option.show();
            }

        } else {
            dialog_imag_option.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Globals.whichuser="";
    }


    @Override
    public void onResume() {
        super.onResume();
        Globals.whichuser="image";
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Image");

        if(imagepath==null){

        }else{
            nextimage.setText("Upload");
        }
    }
    private void captureImageInitialization() {

        final String[] items = new String[] { "Take from camera",
                "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {
                    vars.log("dispatchTakePictureIntent================");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            return;
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            try {
                                mImageCaptureUri = FileProvider.getUriForFile(getActivity(),
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        createImageFile());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
                        }
                    }else{
                        vars.log("dispatchTakePictureIntent========null========");
                    }

                } else {

                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        dialog_imag_option = builder.create();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {

            case PICK_FROM_CAMERA:
                try{
//
                    Utils.cropimageString(getActivity(), "faceimage", Utils.decodeUri(mImageCaptureUri,
                            getActivity()), my_id_photo, new Utils.StringCallback() {
                        @Override
                        public void onSuccess(String result) {
                            imagepath=null;
                            imagepath = result;
                            nextimage.setText("Upload");
                            clicktext.setText("RETAKE");
                        }
                    });

                }catch(ActivityNotFoundException ex){
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;

            case PICK_FROM_FILE:
                /**
                 * After selecting image from files, save the selected path
                 */
                mImageCaptureUri = data.getData();

                try{
                    Utils.cropimageString(getActivity(), "faceimage", Utils.decodeUri(mImageCaptureUri, getActivity()), my_id_photo, new Utils.StringCallback() {
                        @Override
                        public void onSuccess(String result) {
                            imagepath=null;
                            imagepath = result;
                            nextimage.setText("Upload");
                            clicktext.setText("RETAKE");
                        }
                    });
                    //files.delete();
                }catch(ActivityNotFoundException | IOException ex){
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        vars.log("IMAGE==="+requestCode);
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                vars.log("requestCode====="+requestCode);
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    vars.log("IMAGE==granted="+requestCode);
                    dialog_imag_option.show();
                } else {
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Please note!!")
                            .setContentText("We won't be able to proceed without your permission to access your camera and image folders")
                            .setCancelText("Don't allow")
                            .setConfirmText("Yes,allow")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                                        }else{
                                            dialog_imag_option.show();
                                        }

                                    } else {
                                        dialog_imag_option.show();
                                    }
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            }).show();

                }
            default:
                vars.log("IMAGE==defalt="+requestCode);
                break;

        }

    }
}
