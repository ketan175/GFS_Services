package ken.example.miniprojects;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerProfileEdit extends AppCompatActivity {

    EditText wEditName,wEditContact, wEditCity, wEditService,wEditCharge;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = firebaseUser.getUid();
    String name, password,service,email, phone, city, image,price;
    Button wSave;
    CircleImageView wProfImg;

    private final LoadingProgressBar progressDialog = new LoadingProgressBar(WorkerProfileEdit.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile_edit);

        wEditName = findViewById(R.id.w_edit_name);
        wEditContact = findViewById(R.id.w_edit_contact);
        wEditCity = findViewById(R.id.w_edit_city);
        wEditService = findViewById(R.id.w_edit_service);
        wProfImg = findViewById(R.id.w_edit_image);
        wSave = findViewById(R.id.w_edit_save);
        wEditCharge = findViewById(R.id.w_edit_service_charge);

        DocumentReference documentReference = firebaseFirestore.collection("Worker").document(currentUid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                name = task.getResult().getString("name");
                phone = task.getResult().getString("contact");
                email = task.getResult().getString("email");
                service = task.getResult().getString("serviceType");
                password = task.getResult().getString("password");
                city = task.getResult().getString("city");
                image = task.getResult().getString("image");
                price = task.getResult().getString("serviceCharge");

            }
        });


/*

        wEditChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(WorkerProfileEdit.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Please Select Image.."),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
*/

        wSave.setOnClickListener(v -> {
            String wName = wEditName.getText().toString();
            String wContact = wEditContact.getText().toString().trim();
            String wCity = wEditCity.getText().toString();
            String wService = wEditService.getText().toString();
            String wCharge = wEditCharge.getText().toString();
            String[] cities = {"Agra","Mathura"};
            String[] service = {"Plumber","Carpenter","Electrician","Tutor","Driver","Painter"};
            if (wName.isEmpty()) {
                wEditName.setError("Fill the name");
            } else if (wContact.isEmpty()) {
                wEditContact.setError("Fill the contact");
            } else if (wCity.isEmpty()) {
                wEditCity.setError("Fill the City");
            } else if (wService.isEmpty()) {
                wEditService.setError("Fill the Service");
            }else if (!Arrays.asList(cities).contains(wCity)){
                wEditCity.setError("City is not Valid");
            }
            else if (!Arrays.asList(service).contains(wService)){
                wEditService.setError("Service is not Valid");
            }
            else if (wCharge.isEmpty()){
                wEditCharge.setError("Fill the Service Charge");
            }
            else {
                progressDialog.showDialog();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                 uploadImageToFireBase(wName, wContact, wCity, wService);
            }
        });
    }



    private void uploadImageToFireBase(String wName, String wContact, String wCity, String wService) {
        Map<String, Object> workerDetail = new HashMap<>();
        DocumentReference documentReference = firebaseFirestore.
                collection("Worker")
                .document(currentUid);
        workerDetail.put("name", wName);
        workerDetail.put("password", password);
        workerDetail.put("email", email);
        workerDetail.put("contact", wContact);
        workerDetail.put("city", wCity);
        workerDetail.put("uid", currentUid);
        workerDetail.put("serviceCharge",price);
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
        workerDetail.put("image", image);
        documentReference.update(workerDetail).addOnCompleteListener(task -> {
            progressDialog.dismissDialog();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(WorkerProfileEdit.this, "Updated Successfully..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WorkerProfileEdit.this,WorkerProfileFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }).addOnFailureListener(e -> {
            progressDialog.dismissDialog();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(WorkerProfileEdit.this, "Data Not Updated...", Toast.LENGTH_SHORT).show();

        });
                            /*Intent intent = new Intent(WorkerProfileEdit.this, WorkerLoginPage.class);
                                        startActivity(intent);
                                        finish();*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Worker").document(currentUid);
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profName = task.getResult().getString("name");
                String profService = task.getResult().getString("serviceType");
                String profCity = task.getResult().getString("city");
                String profContact = task.getResult().getString("contact");
                String profCharge = task.getResult().getString("serviceCharge");

                wEditName.setText(profName);
                wEditService.setText(profService);
                wEditCity.setText(profCity);
                wEditContact.setText(profContact);
                wEditCharge.setText(profCharge);
                Glide.with(wProfImg.getContext()).load(task.getResult().getString("image")).into(wProfImg);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismissDialog();
    }
}