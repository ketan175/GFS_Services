package ken.example.miniprojects;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerLoginPage extends AppCompatActivity {

    EditText password, user_email;
    LinearLayout login_btn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    boolean valid = true;
    RelativeLayout loginTextLayout;

    private final LoadingProgressBar progressDialog = new LoadingProgressBar(CustomerLoginPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);*/


        user_email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        loginTextLayout = findViewById(R.id.loginTextLayout);
        firebaseAuth = FirebaseAuth.getInstance();


        loginTextLayout.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerLoginPage.this, CustomerSignUppage.class);
            startActivity(intent);
            finish();
        });

        login_btn.setOnClickListener(v -> {
            checkField(user_email);
            checkField(password);

            if (valid) {
                progressDialog.showDialog();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                firebaseAuth.signInWithEmailAndPassword(user_email.getText().toString().trim(),
                        password.getText().toString().trim())
                        .addOnSuccessListener(authResult -> {

                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                checkUserType(authResult.getUser().getUid());
                            }else {
                                progressDialog.dismissDialog();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(CustomerLoginPage.this, "Check Your email to Verify your email Address", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                    progressDialog.dismissDialog();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    String error = e.getMessage();
                    Toast.makeText(CustomerLoginPage.this, error, Toast.LENGTH_SHORT).show();
                });
            }

        });

    }

    private void checkUserType(String uid) {
        DocumentReference documentReference = firebaseFirestore.collection("Customer").document(uid);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getString("isCustomer") != null) {
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(CustomerLoginPage.this, CustomerNavigationActivity.class);
                startActivity(intent);
                finish();
//                MainActivity.sp.edit().putBoolean("customerLogged",true).apply();
                Toast.makeText(CustomerLoginPage.this, "Login Successfully..", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(CustomerLoginPage.this, "user not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* @Override
     protected void onStart() {
         super.onStart();
         firebaseAuth.addAuthStateListener(mAuthStateListener);
     }
 */
    public boolean checkField(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Fill Your The Fields");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}