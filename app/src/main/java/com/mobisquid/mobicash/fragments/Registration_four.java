package com.mobisquid.mobicash.fragments;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobisquid.mobicash.BuildConfig;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
public class Registration_four extends Fragment  {
    String encodedIDString="encodedIDString";
    String encodedIDStringResidence="encodedIDStringResidence";
    String encodedFaceString="encodedFaceString";
    Vars vars;
    View rootview;
    TelephonyManager imei;
    TempStore store;
    TextView string_id_photo,clicktext;
    ImageView my_id_photo;
    AlertDialog dialog_imag_option;
    Button nextimage;
    TempStore stored ;
    String imagepath;
    protected static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =7;
    Uri mImageCaptureUri;
    static final int PICK_FROM_CAMERA = 1;
    static final int PICK_FROM_FILE = 3;
    String mCurrentPhotoPath;
    public Registration_four() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        stored = TempStore.findById(TempStore.class,1);
        imei = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_registration_four, container, false);
        Utils.hidekBoard(rootview.findViewById(R.id.scroll),getActivity());
        captureImageInitialization();
        my_id_photo = (ImageView) rootview.findViewById(R.id.my_id_photo);
        string_id_photo =(TextView) rootview.findViewById(R.id.id_photo);
        clicktext =(TextView) rootview.findViewById(R.id.take_photo);
        nextimage=(Button) rootview.findViewById(R.id.nextimage);
        nextimage.setVisibility(View.GONE);
        string_id_photo.setText(Html.fromHtml(getString(R.string.idphoto)));

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
                    Toast.makeText(getActivity(),"no imake",Toast.LENGTH_SHORT).show();
                }else{
                    TempStore stored = TempStore.findById(TempStore.class,1);
                    stored.setIdImage(imagepath);
                    stored.save();
                    if(vars.country_code.equalsIgnoreCase("27")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            Registration_five firstfragment = new Registration_five();
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            Registration_five firstfragment = new Registration_five();
                            firstfragment.setArguments(getActivity().getIntent().getExtras());
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                    .addToBackStack(null)
                                    .commit();
                        }

                    }else{
                        Registeruser();
                    }
                }
            }
        });
        if(vars.country_code.equalsIgnoreCase("27")){
            TempStore stored = TempStore.findById(TempStore.class,1);
            if(stored.getIdImage().equalsIgnoreCase("idimage")){

            }else{
                if(vars.review){
                    nextimage.setVisibility(View.VISIBLE);
                    clicktext.setText("RETAKE");
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Slide slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                        Registration_five firstfragment = new Registration_five();
                        firstfragment.setReenterTransition(slideTransition);
                        firstfragment.setExitTransition(slideTransition);
                        firstfragment.setSharedElementEnterTransition(new ChangeBounds());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        System.out.println("===========t============");
                        Registration_five firstfragment = new Registration_five();
                        firstfragment.setArguments(getActivity().getIntent().getExtras());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }

        }else {
            nextimage.setVisibility(View.VISIBLE);
            clicktext.setText("RETAKE");
        }
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
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(vars.country_code.equalsIgnoreCase("27")){
            toolbar.setTitle("ID photo  5/6");
        }else{
            toolbar.setTitle("ID photo  5/5");
        }

        if(stored.getIdImage().equalsIgnoreCase("idimage")){
            Picasso.with(vars.context).load(vars.server_mb + "idphoto.png".trim())
                    .error(R.color.colorAccent)
                    .placeholder(R.color.colorAccent)
                    .into(my_id_photo);

        }else{
            vars.log("===Resume==idimage==");
            imagepath =stored.getIdImage();
            my_id_photo.setImageURI(Uri.parse(new File(stored.getIdImage()).toString()));
        }
        if(imagepath!=null && !vars.country_code.equalsIgnoreCase("27")){
            TempStore filesstore = TempStore.findById(TempStore.class,1);
            String[] params={encodedIDString,encodedFaceString};
            String[] values={filesstore.getIdImage(),filesstore.getFaceImage()};
            ImagestoString imagestoString = new ImagestoString(params,values);
            imagestoString.execute();

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
                    Utils.cropimage(getActivity(), "idimage", Utils.decodeUri(mImageCaptureUri, getActivity()), my_id_photo, new Utils.StringCallback() {
                        @Override
                        public void onSuccess(String result) {
                            imagepath=null;
                            imagepath = result;
                            nextimage.setVisibility(View.VISIBLE);
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
                    Utils.cropimage(getActivity(), "idimage", Utils.decodeUri(mImageCaptureUri, getActivity()), my_id_photo, new Utils.StringCallback() {
                        @Override
                        public void onSuccess(String result) {
                            imagepath=null;
                            imagepath = result;
                            nextimage.setVisibility(View.VISIBLE);
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
    private void Registeruser(){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("PLEASE NOTE")
                .setContentText("By clicking submit you agree to the Terms and Conditions and that information provided is valid")
                .setCancelText("Review")
                .setConfirmText("Submit")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        vars.edit.putBoolean("review",true);
                        vars.edit.commit();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            Registration_one firstfragment = new Registration_one();
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            Registration_one firstfragment = new Registration_one();
                            firstfragment.setArguments(getActivity().getIntent().getExtras());
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                    .commit();
                        }

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String SERVER_URL = null;
                        final TempStore storereg = TempStore.findById(TempStore.class,1);
                        sweetAlertDialog.dismiss();
                        Alerter.showdialog(getActivity());
                        String mobile;
                        if(vars.country_code.equalsIgnoreCase("27")){
                            String num = storereg.getMobile().substring(2);
                            mobile = "0"+num;
                        }else{
                            String num = storereg.getMobile().substring(3);
                            mobile = "0"+num;
                        }
                        String location=null;
                        if(vars.country_code.equalsIgnoreCase("27")){
                           location ="South Africa";
                        }else if(vars.country_code.equalsIgnoreCase("265")){
                          location ="Malawi";
                        }{
                            location="Rwanda";
                        }

                        String[] parameters={"mobile","firstName","lastName","pinCode","password",
                                "idNo","gender","maritalstatus","language","occupation",
                                "dob","email","address","nokname","nokmobile","nokrelation","imei",
                                "location","idtype","tempresnr","idexpiration ","faceImage",
                                "idImage","residencephoto","province","city"};
                        String[] values={ mobile, storereg.getFirstName(),storereg.getSecondname(),
                                storereg.getPin(),storereg.getPassword(),storereg.getIdNo()
                                ,storereg.getGender(),storereg.getMartalStatus(),storereg.getLanguage(),
                                storereg.getOccupation(),storereg.getDob(),storereg.getEmail(),
                                storereg.getAddress(),storereg.getNokName(),storereg.getNokPhone(),
                                storereg.getNokRelation(),imei.getDeviceId(),location,storereg.getIdtype(),
                                storereg.getTempresnr(),storereg.getIdexpiration(),encodedFaceString,
                                encodedIDString,encodedIDStringResidence,storereg.getProvince(),storereg.getCity()};

                        SERVER_URL=vars.financial_server+"enrolClient.php";

                        ConnectionClass.ConnectionClass(getActivity(), SERVER_URL, parameters, values, "Registration", new ConnectionClass.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                vars.log("After reg====="+result);
                                TransactionObj trans =new Gson().fromJson(result,TransactionObj.class);
                                vars.log("error ======"+trans.getResult());
                                if(trans.getResult().equalsIgnoreCase("success")) {
                                    TempStore.deleteAll(TempStore.class);
                                    //alertDialog.dismiss();
                                    SharedPreferences.Editor editor = vars.prefs.edit();
                                    editor.putString("active", "activenow");
                                    vars.edit.putString("email", storereg.getEmail());
                                    vars.edit.putString("mobile", trans.getClient());
                                    vars.edit.putString("profile_number",trans.getClient());
                                    vars.edit.putString("fullname", storereg.getFirstName()+" "+storereg.getSecondname());
                                    vars.edit.putString("firtstime_number",trans.getClient());
                                    vars.edit.apply();
                                    Utils.isFinance(getActivity(),true);
                                    Alerter.stopdialog();
                                    alerterSuccessSimple("Success", "Thank you for registering for our services.\n Your profile will be reviewed and contacted soon");


                                }else{
                                    Alerter.stopdialog();
                                    if(trans.getError().equalsIgnoreCase("Imei is not unique")){
                                        ErrorMessage(getActivity(),"Error", "Hello your device is already registered for this service");
                                    }else {
                                        ErrorMessage(getActivity(),"Error", trans.getError());
                                    }

                                }

                            }
                        });




                    }
                })
                .show();

    }
    public void ErrorMessage(Context context,String header, String message){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(header)
                .setContentText(message)
                .setCancelText("Review")
                .setConfirmText("Try again")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        vars.edit.putBoolean("review",true);
                        vars.edit.commit();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slideTransition = new Slide(Gravity.LEFT);
                            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
                            Registration_one firstfragment = new Registration_one();
                            firstfragment.setReenterTransition(slideTransition);
                            firstfragment.setExitTransition(slideTransition);
                            firstfragment.setSharedElementEnterTransition(new ChangeBounds());
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                    .commit();
                        } else {
                            System.out.println("===========t============");
                            Registration_one firstfragment = new Registration_one();
                            firstfragment.setArguments(getActivity().getIntent().getExtras());
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_finactional_registration, firstfragment, "MAIN")
                                    .commit();
                        }


                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Registeruser();
                    }
                })
                .show();
    }
    private void alerterSuccessSimple(String success, String message) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(success)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Intent mainpage = new Intent(getActivity(), MainActivity.class);
                        startActivity(mainpage);
                        getActivity().finish();
                    }
                })
                .show();
    }

    class ImagestoString extends AsyncTask<Void, Integer, String> {
        String[] parameter;
        String[] values;
        public ImagestoString(String[] parameter,String[] values) {
            this.parameter=parameter;
            this.values=values;

        }
        @Override
        protected String doInBackground(Void... params) {
            File imgFile ;
            Bitmap myBitmap;
            for (int y = 0; y < parameter.length; y++) {

                vars.log("para " + y + "===" + parameter[y] + "====and ==" + "values " + y + "===" + values[y]);
                imgFile = new File(values[y]);

                if (imgFile.exists()) {
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //    files_profile = saveImage(photo, "users", context);
                    byte[] imageBytes = baos.toByteArray();

                    if(parameter[y].equalsIgnoreCase("encodedIDString")){
                        encodedIDString =  Base64.encodeToString(imageBytes,
                                Base64.DEFAULT);
                    }else if(parameter[y].equalsIgnoreCase("encodedIDStringResidence")){
                        encodedIDStringResidence  =  Base64.encodeToString(imageBytes,
                                Base64.DEFAULT);
                    }else if(parameter[y].equalsIgnoreCase("encodedFaceString")){
                        encodedFaceString =  Base64.encodeToString(imageBytes,
                                Base64.DEFAULT);
                    }

                }

            }

            return null;
        }
    }
}
