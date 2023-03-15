package ken.example.miniprojects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WorkerLoginPage extends AppCompatActivity {
    EditText wLoginEmail,wLoginPassword;
    LinearLayout wLoginBtn;
    RelativeLayout wLoginCreateAccount;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    boolean valid = true;
    private final LoadingProgressBar progressDialog = new LoadingProgressBar(WorkerLoginPage.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login_page);

        wLoginEmail = findViewById(R.id.wLoginName);
        wLoginPassword = findViewById(R.id.wLoginPassword);
        wLoginBtn = findViewById(R.id.wLoginBtn);
        wLoginCreateAccount = findViewById(R.id.wLoginTextLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        wLoginBtn.setOnClickListener(v -> {
            isValid(wLoginEmail);
            isValid(wLoginPassword);
            if (valid) {
                progressDialog.showDialog();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                firebaseAuth.signInWithEmailAndPassword(wLoginEmail.getText().toString(),wLoginPassword.getText().toString())
                        .addOnSuccessListener(authResult -> {
//                            checkUserType(authResult.getUser().getUid());

                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                checkUserType(authResult.getUser().getUid());
                            }else {
                                progressDialog.dismissDialog();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(WorkerLoginPage.this, "Check Your email to Verify your email Address", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(e -> {
                            progressDialog.dismissDialog();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(WorkerLoginPage.this, "Login Failed..", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        wLoginCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(WorkerLoginPage.this,WorkerSignUp.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserType(String uid) {
        Map<String,Object> map = new HashMap<>();
        map.put("emailVerified",true);
        final DocumentReference documentReference = firebaseFirestore.collection("Worker").document(uid);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG","on success" + documentSnapshot.getData());

            if (documentSnapshot.getString("isPlumber") != null){
//                MainActivity.sp.edit().putBoolean("workerLogged",true).apply();
                Toast.makeText(WorkerLoginPage.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(WorkerLoginPage.this,WorkerNavigationActivity.class));
                finish();
                documentReference.update(map);
            }
            else if (documentSnapshot.getString("isCarpenter")!=null){
//                MainActivity.sp.edit().putBoolean("workerLogged",true).apply();
                Toast.makeText(WorkerLoginPage.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(WorkerLoginPage.this,WorkerNavigationActivity.class);
                startActivity(intent);
                finish();
                documentReference.update(map);
            }
            else if (documentSnapshot.getString("isElectrician")!=null){
                Toast.makeText(WorkerLoginPage.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
//                MainActivity.sp.edit().putBoolean("workerLogged",true).apply();
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(WorkerLoginPage.this,WorkerNavigationActivity.class));
                finish();
                documentReference.update(map);
            }
            else if (documentSnapshot.getString("isTutor")!=null){
//                MainActivity.sp.edit().putBoolean("workerLogged",true).apply();
                Toast.makeText(WorkerLoginPage.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(WorkerLoginPage.this,WorkerNavigationActivity.class));
                finish();
                documentReference.update(map);
            }
            else if (documentSnapshot.getString("isDriver")!=null){
//                MainActivity.sp.edit().putBoolean("workerLogged",true).apply();
                Toast.makeText(WorkerLoginPage.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(WorkerLoginPage.this,WorkerNavigationActivity.class));
                finish();
                documentReference.update(map);
            }
            else if (documentSnapshot.getString("isPainter")!=null){
//                MainActivity.sp.edit().putBoolean("workerLogged",true).apply();
                Toast.makeText(WorkerLoginPage.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(new Intent(WorkerLoginPage.this,WorkerNavigationActivity.class));
                finish();
                documentReference.update(map);
            }
            else {
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(WorkerLoginPage.this, "User Not Found...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValid(EditText editText) {
        if (editText.getText().toString().isEmpty()){
            editText.setError("Fill All The Fields");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}