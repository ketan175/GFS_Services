package ken.example.miniprojects;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerSignUppage extends AppCompatActivity {

    EditText customerName, customerPassword, customerEmail, customerContact;
    RelativeLayout haveAnAccount;
    FirebaseFirestore firebaseFirestore;
    LinearLayout signup_btn;
    AutoCompleteTextView customerCity;
    TextView view;
    FirebaseAuth firebaseAuth;
    Uri imagePath;
    private final LoadingProgressBar progressDialog = new LoadingProgressBar(CustomerSignUppage.this);

    String cities[] = {"Agra","Mathura"};
    ArrayAdapter<String> adpCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_uppage);


        view = findViewById(R.id.spinner_text);
        customerName = findViewById(R.id.username);
        customerPassword = findViewById(R.id.password);
        customerEmail = findViewById(R.id.user_email);
        customerCity = findViewById(R.id.user_city);
        customerContact = findViewById(R.id.user_contact);
        haveAnAccount = findViewById(R.id.textLayout);
        signup_btn = findViewById(R.id.signup_btn);

        adpCity = new ArrayAdapter<String>(CustomerSignUppage.this, R.layout.spinner_list);
        adpCity.addAll(cities);
        customerCity.setAdapter(adpCity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cName = customerName.getText().toString();
                String cPassword = customerPassword.getText().toString().trim();
                String cEmail = customerEmail.getText().toString().trim();
                String cPhoneNumber = customerContact.getText().toString().trim();
                String city = customerCity.getText().toString();

                if (cName.isEmpty()) {
                    customerName.setError("Fill the name");
                } else if (cPassword.isEmpty()) {
                    customerPassword.setError("Fill the password");
                } else if (cPassword.length() < 6) {
                    customerPassword.setError("Password should be at least 6 Characters.");
                } else if (cEmail.isEmpty()) {
                    customerEmail.setError("Fill the email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(cEmail).matches()) {
                    customerEmail.setError("Enter Valid email");
                } else if (cPhoneNumber.isEmpty()) {
                    customerContact.setError("Fill the contact");
                } else if (!Patterns.PHONE.matcher(cPhoneNumber).matches()) {
                    customerContact.setError("Enter valid contact");
                } else if (city.isEmpty()) {
                    customerCity.setError("Select the City");
                } else {
                    progressDialog.showDialog();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    firebaseAuth.createUserWithEmailAndPassword(customerEmail.getText().toString().trim(), customerPassword.getText().toString().trim())
                            .addOnSuccessListener(authResult -> {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        uploadImageToFireBase(cName, cPassword, cEmail, cPhoneNumber, city);
                                        Toast.makeText(CustomerSignUppage.this, "Please Verify your email...", Toast.LENGTH_SHORT).show();
                                    }
                                });



                                
/*

                                Log.i("hii","success");
                                FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
                                Map<String,Object> userDetail =new HashMap<>();
                                DocumentReference documentReference = firebaseFirestore.
                                        collection("Customer").document(firebaseUser.getUid());

                                userDetail.put("name",cName);
                                userDetail.put("password",cPassword);
                                userDetail.put("email",cEmail);
                                userDetail.put("contact",cPhoneNumber);
                                userDetail.put("city",city);
                                userDetail.put("isCustomer","1");

                                documentReference.set(userDetail);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CustomerSignUppage.this, "Sign Up Successfully...", Toast.LENGTH_SHORT).show();
*/
                               /* customerName.setText("");
                                customerEmail.setText("");
                                customerContact.setText("");
                                customerPassword.setText("");
                                customerCity.setText("");*/
                            }).addOnFailureListener(e -> {
                        String error = e.getMessage();
                        progressDialog.dismissDialog();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(CustomerSignUppage.this, error, Toast.LENGTH_SHORT).show();
                        customerName.setText("");
                        customerEmail.setText("");
                        customerContact.setText("");
                        customerPassword.setText("");
                        customerCity.setAdapter(null);
                    });
                }
            }

        });

        haveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerSignUppage.this, CustomerLoginPage.class);
            startActivity(intent);
            finish();
        });
    }


    private void uploadImageToFireBase(String cName, String cPassword, String cEmail, String cPhoneNumber, String city) {
        imagePath = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();
        if (imagePath != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference reference = firebaseStorage.getReference().child(currentUser);

            reference.putFile(imagePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri uri = task.getResult();
                                    Map<String, Object> userDetail = new HashMap<>();
                                    DocumentReference documentReference = firebaseFirestore.
                                            collection("Customer").document(currentUser);

                                    userDetail.put("name", cName);
                                    userDetail.put("password", cPassword);
                                    userDetail.put("email", cEmail);
                                    userDetail.put("contact", cPhoneNumber);
                                    userDetail.put("city", city);
                                    userDetail.put("isCustomer", "1");
                                    userDetail.put("image", uri.toString());

                                    documentReference.set(userDetail).addOnCompleteListener(task1 -> {
                                        CollectionReference collectionReference = firebaseFirestore.collection("Customer").document(firebaseUser.getUid()).collection("User Data");

                                        List<String> documentNames = new ArrayList<>();
                                        documentNames.add("My_Ratings");
                                        documentNames.add("My_Request_History");

                                        Map<String, Object> requestHistoryMap = new HashMap<>();
                                        requestHistoryMap.put("list_size", (long) 0);

                                        Map<String, Object> myRatingMap = new HashMap<>();
                                        myRatingMap.put("list_size", (long) 0);

                                        List<Map<String, Object>> documentFields = new ArrayList<>();
                                        documentFields.add(myRatingMap);
                                        documentFields.add(requestHistoryMap);

                                        for (int x = 0; x < documentNames.size(); x++) {
                                            final int finalX = x;
                                            collectionReference.document(documentNames.get(x))
                                                    .set(documentFields.get(x))
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            if (finalX == documentNames.size() - 1) {
                                                                progressDialog.dismissDialog();
                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                Toast.makeText(CustomerSignUppage.this, "Sign Up Successfully...", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(CustomerSignUppage.this, CustomerLoginPage.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        }

                                    });

                                }
                            }
                        });

                    });
        } else {
            progressDialog.dismissDialog();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(CustomerSignUppage.this, "Account Not Created...\nTry Again..", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismissDialog();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}