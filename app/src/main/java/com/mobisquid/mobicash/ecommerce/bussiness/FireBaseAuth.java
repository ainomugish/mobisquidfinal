package com.mobisquid.mobicash.ecommerce.bussiness;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mobisquid.mobicash.ecommerce.callbacks.ICode;
import com.mobisquid.mobicash.ecommerce.callbacks.ISignInSuccess;
import com.mobisquid.mobicash.ecommerce.utitlities.LogUtility;

import java.util.concurrent.TimeUnit;


/**
 * Created by Zeera on 7/15/2017 bt ${File}
 */

public class FireBaseAuth {
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private Activity mContext;
    private boolean mVerificationInProgress;
    private FirebaseAuth mAuth;
    private String mVerificationId="";
    private ISignInSuccess mListener;
    private ICode mCodeListener;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    public FireBaseAuth(final Activity mContext, ISignInSuccess listener) {
        mListener = listener;
        this.mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                LogUtility.debugLog(phoneAuthCredential.toString());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                LogUtility.debugLog(e.toString());
                if(mCodeListener!=null)
                    mCodeListener.codeSent(false);
                Toast.makeText(mContext,"Invalid Phone Number please enter valid format " +
                        "[country code][subscriber number including area code]",Toast.LENGTH_LONG).show();

            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                LogUtility.debugLog( "onCodeSent:" + verificationId);
                mCodeListener.codeSent(true);
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(mContext,"Verification Code is send to your number",Toast.LENGTH_LONG).show();
                // ...
            }
        };
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
    }

    public void startPhoneNumberVerification(String phoneNumber, ICode listener) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                mContext,               // Activity (for callback binding)
                mCallback);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        mCodeListener = listener;
        mVerificationInProgress = true;
    }

    public void verifyPhoneNumberWithCode(String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        // [END verify_with_code]

        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    public void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                mContext,               // Activity (for callback binding)
                mCallback,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            LogUtility.debugLog("signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            if (mListener != null) {
                                mListener.isProceed(true);
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            LogUtility.debugLog( "signInWithCredential:failure"+ task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(mContext,"Invalid code entered",Toast.LENGTH_LONG).show();
                            }
                            if (mListener != null) {
                                mListener.isProceed(false);
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
}
