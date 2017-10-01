package com.mobisquid.mobicash.ecommerce.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.MainActivity;
import com.mobisquid.mobicash.ecommerce.adapters.BasketAdapter;
import com.mobisquid.mobicash.ecommerce.bussiness.Basket;
import com.mobisquid.mobicash.ecommerce.bussiness.FireBaseAuth;
import com.mobisquid.mobicash.ecommerce.callbacks.ICode;
import com.mobisquid.mobicash.ecommerce.callbacks.ISignInSuccess;
import com.mobisquid.mobicash.ecommerce.callbacks.IUpdateTotal;
import com.mobisquid.mobicash.ecommerce.dialogs.LoadingDialog;
import com.mobisquid.mobicash.ecommerce.dialogs.MobiCashDialog;
import com.mobisquid.mobicash.ecommerce.dialogs.PaymentDialog;
import com.mobisquid.mobicash.ecommerce.dialogs.PinCodeDialog;
import com.mobisquid.mobicash.ecommerce.essentials.SessionClass;
import com.mobisquid.mobicash.ecommerce.models.enums.PaymentMethod;
import com.mobisquid.mobicash.ecommerce.network.APIsEndPoints;
import com.mobisquid.mobicash.ecommerce.network.ApiClient;
import com.mobisquid.mobicash.ecommerce.network.CallApi;
import com.mobisquid.mobicash.ecommerce.network.IApiInterface;
import com.mobisquid.mobicash.ecommerce.network.IResponse;
import com.mobisquid.mobicash.ecommerce.utitlities.LogUtility;
import com.mobisquid.mobicash.ecommerce.utitlities.ViewUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends BaseFragment implements IUpdateTotal, ITaggingECommerce,IResponse,ISignInSuccess {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private BasketAdapter adapter;
    private TextView total;
    private Unbinder mUnBinder;
    @BindView(R.id.btn_checkOut)
    Button btnCheckOut;
    private FireBaseAuth auth;
    private String mPhone;
    private String mPin;
    private LoadingDialog dialog;


    public BasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    public void onViewCreated(View view,Bundle bundle){
        super.onViewCreated(view,bundle);
        mUnBinder = ButterKnife.bind(this,view);
        auth = new FireBaseAuth(getActivity(),this);
        dialog = new LoadingDialog(getContext());
        initializeViews(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    private void initializeViews(View view){
        this.recyclerView=(RecyclerView)view.findViewById(R.id.recylerview);
        layoutManager=new LinearLayoutManager(getActivity());
        adapter=new BasketAdapter(getActivity(),this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        total=(TextView)view.findViewById(R.id.grandtotal);
        updateTotal();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void updateTotal() {
        total.setText(String.valueOf(Basket.getBasket().getTotalOfBasket()));
        btnCheckOut.setAlpha(Basket.getBasket().getTotalOfBasket()==0? (float) 0.5 :1);
        btnCheckOut.setEnabled(Basket.getBasket().getTotalOfBasket()!=0);
    }

    @OnClick(R.id.btn_checkOut)
    public void checkOut(){
        PaymentDialog paymentDialog = new PaymentDialog(getContext());
        paymentDialog.setmListener(new PaymentDialog.IDialogItemSelected() {
            @Override
            public void itemSelected(PaymentMethod method) {
                switch (method){
                    case MOBICASH:
                        showMobiCashDialog();
                        break;
                }
            }
        });
        paymentDialog.show();
    }
    PinCodeDialog pinCodeDialog;
    private void showMobiCashDialog(){
        final MobiCashDialog mobiCashDialog = new MobiCashDialog(getContext());
        mobiCashDialog.setmListener(new MobiCashDialog.IMobiCashPay() {
            @Override
            public void pay(String phone, String pin) {
                ViewUtility.hideKeyboard(getActivity(),mobiCashDialog.getCurrentFocus());
                mPhone = phone;
                mPin = pin;
                dialog.show();
                dialog.setMessage("Sending code");
                auth.startPhoneNumberVerification(mPhone, new ICode() {
                    @Override
                    public void codeSent(boolean isSuccessfull) {
                        dialog.dismiss();
                        if(isSuccessfull){
                            mobiCashDialog.dismiss();
                            pinCodeDialog = new PinCodeDialog(getContext());
                            pinCodeDialog.setmListener(new PinCodeDialog.IVerificationPin() {
                                @Override
                                public void pay(String pin) {

                                    auth.verifyPhoneNumberWithCode(pin);
                                    dialog.show();
                                    dialog.setMessage("Verifying your code");
                                }
                            });
                            pinCodeDialog.show();
                        }

                    }
                });


            }
        });
        mobiCashDialog.show();
    }

    private void callPayApi(String phone,String pin){
        IApiInterface iApiInterface = ApiClient.getRetrofitFiniacial().create(IApiInterface.class);
        Call<ResponseBody> eCommerce = iApiInterface.payAmount(phone, pin, SessionClass.MERCHANT_ACCOUNT,
                Basket.getBasket().getTotalOfBasket(), "ECOMMERCE");
        CallApi callApi = new CallApi(getContext(), this);
        callApi.setLoadingMessage("Checking Your Account");
        callApi.callService(eCommerce, APIsEndPoints.CHECK_ACCOUNT);
    }

    private void onPaymentSuccess(){
        Toast.makeText(getContext(),"Your payment was successful.Thanks for purchasing from us",Toast.LENGTH_LONG).show();
        Basket.getBasket().clearBasket();
        updateTotal();
        updateCount();
    }

    private void updateCount(){
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateCount();
        }
    }


    @Override
    public void onSuccess(String body, String endPoint) {
        // TODO: 7/16/2017 handle success and error case
        try {
            JSONObject jsonObject = new JSONObject(body);
            String result = jsonObject.getString("result");
            String message = jsonObject.getString("message");
            if(result.equals("failed"))
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            else
                onPaymentSuccess();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtility.debugLog(body);
    }

    @Override
    public void onError(String error, String endPoint) {

    }

    @Override
    public void onFailed(Throwable e, String endPoint) {

    }

    @Override
    public void isProceed(boolean isTo) {
        dialog.dismiss();
        if(isTo){
            callPayApi(mPhone,mPin);
            pinCodeDialog.dismiss();
        }

    }
}
