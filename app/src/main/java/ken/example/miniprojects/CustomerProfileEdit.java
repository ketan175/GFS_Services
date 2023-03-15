package ken.example.miniprojects;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileEdit extends AppCompatActivity {


    EditText cEditName,cEditContact, cEditCity;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = firebaseUser.getUid();
    String name, password,email, phone, city, image;
    Button cSave;
    CircleImageView cProfImg;
    private final LoadingProgressBar progressDialog = new LoadingProgressBar(CustomerProfileEdit.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_edit);


        cEditName = findViewById(R.id.c_edit_name);
        cEditCity = findViewById(R.id.c_edit_city);
        cEditContact = findViewById(R.id.c_edit_contact);
        cSave = findViewById(R.id.c_edit_save);
        cProfImg = findViewById(R.id.c_edit_image);

        DocumentReference documentReference = firebaseFirestore.collection("Customer").document(currentUid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                name = task.getResult().getString("name");
                phone = task.getResult().getString("contact");
                email = task.getResult().getString("email");
                password = task.getResult().getString("password");
                city = task.getResult().getString("city");
                image = task.getResult().getString("image");

            }
        });

        cSave.setOnClickListener(v -> {

            String cName = cEditName.getText().toString();
            String cContact = cEditContact.getText().toString().trim();
            String cCity = cEditCity.getText().toString();
            String[] cities = {"Agra","Mathura"};
            if (cName.isEmpty()) {
                cEditName.setError("Fill the name");
            } else if (cContact.isEmpty()) {
                cEditContact.setError("Fill the contact");
            } else if (cCity.isEmpty()) {
                cEditCity.setError("Select the City");
            }else if (!Arrays.asList(cities).contains(cCity)){
                cEditCity.setError("City is not Valid");
            }
            else {
                progressDialog.showDialog();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                uploadImageToFireBase(cName,cContact,cCity);

            }
        });

    }

    private void uploadImageToFireBase(String cName, String cContact, String cCity) {
        Map<String, Object> customerDetail = new HashMap<>();
        DocumentReference documentReference = firebaseFirestore.
                collection("Customer")
                .document(currentUid);
        customerDetail.put("name", cName);
        customerDetail.put("password", password);
        customerDetail.put("email", email);
        customerDetail.put("contact", cContact);
        customerDetail.put("city", cCity);
        customerDetail.put("uid", currentUid);
        customerDetail.put("isCustomer","1");

        customerDetail.put("image", image);
        documentReference.update(customerDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(CustomerProfileEdit.this, "Updated Successfully..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerProfileEdit.this,CustomerProfileFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(CustomerProfileEdit.this, "Data not Updated..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Customer").document(currentUid);
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profName = task.getResult().getString("name");
                String profCity = task.getResult().getString("city");
                String profContact = task.getResult().getString("contact");

                cEditName.setText(profName);
                cEditCity.setText(profCity);
                cEditContact.setText(profContact);
                Glide.with(cProfImg.getContext()).load(task.getResult().getString("image")).into(cProfImg);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismissDialog();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}