package shaz.com.firebasedbwithauth;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.textView1)
    protected TextView mTextView;

    @Override
    protected int initRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPreCreateActivity(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostCreateActivity(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                if (getFirebaseAuth() != null) {
                    getFirebaseAuth().signOut();
                    redirect(new Intent(this, LoginActivity.class), true, true);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isUserVerified()) {
            FirebaseUser currentUser = getFirebaseAuth().getCurrentUser();
            getUserReference().child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            mTextView.setText(user.getUsername() + "\n\n" + user.getEmail());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return;
        }
        showToast("Session expired");
        redirect(new Intent(this, LoginActivity.class), true, true);
    }
}
