package shaz.com.firebasedbwithauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${Shahbaz} on 12-10-2017
 */

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.editText0)
    protected EditText mName;

    @BindView(R.id.editText1)
    protected EditText mEmail;

    @BindView(R.id.editText2)
    protected EditText mPassword;

    @Override
    protected int initRes() {
        return R.layout.activity_registration;
    }

    @Override
    protected void onPreCreateActivity(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostCreateActivity(@Nullable Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button1)
    void register() {
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String pwd = mPassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getFirebaseAuth() != null) {
            getFirebaseAuth().createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showToast("Registration successful.");
                                addUserOnFirebase(name, getFirebaseAuth().getCurrentUser());
                                sendEmailVerification();
                                //redirect(new Intent(RegistrationActivity.this, MainActivity.class), true, true);
                                finish();
                            } else {
                                showToast("Registration failed.");
                            }
                        }
                    });
        }
    }
}
