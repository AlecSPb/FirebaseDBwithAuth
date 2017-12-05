package shaz.com.firebasedbwithauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${Shahbaz} on 12-10-2017.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.editText1)
    protected EditText mEmail;

    @BindView(R.id.editText2)
    protected EditText mPassword;

    @Override
    protected int initRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onPreCreateActivity(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostCreateActivity(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isUserVerified())
            redirect(new Intent(this, MainActivity.class), true, true);
    }

    @OnClick(R.id.button1)
    void login() {
        final String email = mEmail.getText().toString();
        final String pwd = mPassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            showToast("Some fields are empty");
            return;
        }
        if (getFirebaseAuth() != null) {
            getFirebaseAuth().signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (isUserVerified())
                                    redirect(new Intent(LoginActivity.this, MainActivity.class), true, true);
                                else {
                                    if (getFirebaseAuth() != null)
                                        getFirebaseAuth().signOut();
                                    showToast("Please verify your email address first.");
                                }
                            } else {
                                showToast("Authentication failed.");
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.button2)
    void registerUser() {
        redirect(new Intent(this, RegistrationActivity.class), false, false);
    }
}
