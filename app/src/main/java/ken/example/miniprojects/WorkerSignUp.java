package ken.example.miniprojects;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class WorkerSignUp extends AppCompatActivity {
    AutoCompleteTextView workerCity, workerService;
    EditText workerName, workerPassword, workerEmail, workerContact, workerCharge;

    FirebaseFirestore firebaseFirestore;
    LinearLayout workerSignBtn;
    RelativeLayout wSignIn;
    FirebaseAuth firebaseAuth;
    Uri imagePath;


    String[] service = {"Plumber", "Carpenter", "Electrician", "Tutor", "Driver", "Painter"};
    String[] cities = {"Agra", "Mathura"};
    ArrayAdapter<String> adpService, adpCity;

    private final LoadingProgressBar progressDialog = new LoadingProgressBar(WorkerSignUp.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_sign_up);


        workerName = findViewById(R.id.workerUsername);
        workerEmail = findViewById(R.id.workerEmail);
        workerContact = findViewById(R.id.workerContact);
        workerPassword = findViewById(R.id.workerPassword);
        workerSignBtn = findViewById(R.id.workerSignBtn);
        workerCity = findViewById(R.id.workerCity);
        workerService = findViewById(R.id.workerService);
        workerCharge = findViewById(R.id.workerCharge);
        wSignIn = findViewById(R.id.workerTextLayout);


        adpCity = new ArrayAdapter<>(WorkerSignUp.this, R.layout.spinner_list, cities);
        workerCity.setAdapter(adpCity);
        adpService = new ArrayAdapter<>(WorkerSignUp.this, R.layout.spinner_list, service);
        workerService.setAdapter(adpService);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        workerSignBtn.setOnClickListener(v -> {
            String wName = workerName.getText().toString();
            String wEmail = workerEmail.getText().toString().trim();
            String wPassword = workerPassword.getText().toString();
            String wContact = workerContact.getText().toString().trim();
            String wCity = workerCity.getText().toString();
            String wService = workerService.getText().toString();
            String wCharge = workerCharge.getText().toString();

            if (wName.isEmpty()) {
                workerName.setError("Fill the name");
            } else if (wPassword.isEmpty()) {
                workerPassword.setError("Fill the password");
            } else if (wPassword.length() < 6) {
                workerPassword.setError("Password should be at least 6 Characters.");
            } else if (wEmail.isEmpty()) {
                workerEmail.setError("Fill the email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(wEmail).matches()) {
                workerEmail.setError("Enter Valid email");
            } else if (wContact.isEmpty()) {
                workerContact.setError("Fill the contact");
            } else if (!Patterns.PHONE.matcher(wContact).matches()) {
                workerContact.setError("Enter valid contact");
            } else if (wContact.length() < 10 || wContact.length() > 12) {
                workerContact.setError("Enter Valid contact");
            } else if (wCharge.isEmpty()) {
                workerCharge.setError("Fill The Service Charge");
            } else if (wCity.isEmpty()) {
                workerCity.setError("Select the City");
            } else if (wService.isEmpty()) {
                workerService.setError("Select the Service");
            } else {
                progressDialog.showDialog();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                firebaseAuth.createUserWithEmailAndPassword(wEmail, wPassword).addOnSuccessListener(authResult -> {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            uploadImageToFireBase(wName, wPassword, wEmail, wContact, wCity, wService, wCharge);
                            Toast.makeText(WorkerSignUp.this, "Verify your email", Toast.LENGTH_SHORT).show();
                        }
                    });

                }).addOnFailureListener(e -> {
                    progressDialog.dismissDialog();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(WorkerSignUp.this, "Sign Up Failed.\n Or details already exists \n Try Again!", Toast.LENGTH_SHORT).show();
                });
            }
        });

        wSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(WorkerSignUp.this, WorkerLoginPage.class);
            startActivity(intent);
            finish();
        });

    }


    private void uploadImageToFireBase(String wName, String wPassword, String wEmail, String wContact, String wCity, String wService, String wCharge) {
        imagePath = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();
        if (imagePath != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference reference = firebaseStorage.getReference().child(currentUser);

            reference.putFile(imagePath)
                    .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            Map<String, Object> workerDetail = new HashMap<>();
                            DocumentReference documentReference = firebaseFirestore.
                                    collection("Worker")
                                    .document(firebaseUser.getUid());
                            workerDetail.put("name", wName);
                            workerDetail.put("password", wPassword);
                            workerDetail.put("email", wEmail);
                            workerDetail.put("contact", wContact);
                            workerDetail.put("city", wCity);
                            workerDetail.put("uid", firebaseUser.getUid());
                            workerDetail.put("serviceCharge", wCharge);
                            switch (wService) {
                                case "Plumber":
                                    workerDetail.put("serviceType", wService);
                                    workerDetail.put("isPlumber", "1");
                                    break;
                                case "Carpenter":
                                    workerDetail.put("serviceType", wService);
                                    workerDetail.put("isCarpenter", "1");
                                    break;
                                case "Electrician":
                                    workerDetail.put("serviceType", wService);
                                    workerDetail.put("isElectrician", "1");
                                    break;
                                case "Tutor":
                                    workerDetail.put("serviceType", wService);
                                    workerDetail.put("isTutor", "1");
                                    break;
                                case "Driver":
                                    workerDetail.put("serviceType", wService);
                                    workerDetail.put("isDriver", "1");
                                    break;
                                case "Painter":
                                    workerDetail.put("serviceType", wService);
                                    workerDetail.put("isPainter", "1");
                                    break;
                            }
                            workerDetail.put("image", uri.toString());
                            for (int i = 1; i < 6; i++) {
                                workerDetail.put((i + "_star"), (long) 0);
                            }
                            workerDetail.put("totalRating", (long) 0);
                            workerDetail.put("averageRating", "0");
                            documentReference.set(workerDetail);
                            Toast.makeText(WorkerSignUp.this, "Account Created..", Toast.LENGTH_SHORT).show();
                            progressDialog.dismissDialog();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Intent intent = new Intent(WorkerSignUp.this, WorkerLoginPage.class);
                            startActivity(intent);
                            finish();
                        }
                    }));
        } else {
            progressDialog.dismissDialog();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(WorkerSignUp.this, "Account Not Created...\nTry Again..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismissDialog();
    }
}