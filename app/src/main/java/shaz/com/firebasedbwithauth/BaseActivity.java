package shaz.com.firebasedbwithauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static shaz.com.firebasedbwithauth.Util.usernameFromEmail;

/**
 * Created by ${Shahbaz} on 12-10-2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private Toast mToast;

    protected abstract @LayoutRes int initRes();

    protected abstract void onPreCreateActivity(@Nullable Bundle savedInstanceState);
    protected abstract void onPostCreateActivity(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onPreCreateActivity(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(initRes());
        mUnbinder = ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.keepSynced(true);
        onPostCreateActivity(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    protected DatabaseReference getUserReference(){
        return mDatabaseReference.child("users");
    }

    protected boolean isUserVerified() {
        return mFirebaseAuth.getCurrentUser() != null && mFirebaseAuth.getCurrentUser().isEmailVerified();
    }

    protected void redirect(@NonNull Intent intent, boolean clear, boolean finish) {
        if (clear)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (finish)
            finish();
    }

    protected void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected void showToast(String message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setDuration(duration);
        mToast.setText(message);
        mToast.show();
    }

    protected void addUserOnFirebase(String displayName, FirebaseUser firebaseUser) {
        if (firebaseUser == null)
            return;
        if (TextUtils.isEmpty(displayName))
            displayName = usernameFromEmail(firebaseUser.getEmail());
        User user = new User(displayName, firebaseUser.getEmail());
        getUserReference().child(firebaseUser.getUid()).setValue(user);
    }

    protected void sendEmailVerification() {
        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser == null)
            return;
        currentUser.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            showToast("Verification email sent to " + currentUser.getEmail(), Toast.LENGTH_LONG);
                        else
                            showToast("Failed to send verification email.", Toast.LENGTH_LONG);
                    }
                });
    }
}
