package ken.example.miniprojects;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create an instance of this fragment.
 */
public class WorkerProfileFragment extends Fragment {

    CircleImageView wProfImg;
    String name,password,email,service,phone,city,image,price;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Uri imagePath;
    TextView wProfName,wProfEmail,wProfContact,wProfCity,wProfService,wProfChange,wProfCharge;
    FloatingActionButton wProfEdit;
    Bitmap bitmap;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = firebaseUser.getUid();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_worker_profile, container, false);

        wProfImg = view.findViewById(R.id.w_prof_image);
        wProfName = view.findViewById(R.id.w_prof_name);
        wProfEmail = view.findViewById(R.id.w_prof_email);
        wProfContact = view.findViewById(R.id.w_prof_contact);
        wProfCity = view.findViewById(R.id.w_prof_city);
        wProfService = view.findViewById(R.id.w_prof_service);
        wProfEdit =view.findViewById(R.id.w_prof_edit);
        wProfChange = view.findViewById(R.id.w_prof_change);
        wProfCharge = view.findViewById(R.id.w_prof_charge);

        ActivityResultLauncher<String> intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), result -> {
                    imagePath= result;
                    try {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(imagePath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        wProfImg.setImageBitmap(bitmap);
                        uploadImageToFireBase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        DocumentReference documentReference = firebaseFirestore.collection("Worker").document(currentUid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
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

        wProfEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),WorkerProfileEdit.class);
            startActivity(intent);
        });

        wProfImg.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WorkerProfileShow.class);
            intent.putExtra("userimage",image);
            startActivity(intent);

        });

        wProfChange.setOnClickListener(v -> Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        intentActivityResultLauncher.launch("image/*");

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());
        return view;

    }


    private void uploadImageToFireBase() {
        if (imagePath != null){

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("File Uploading..");
            progressDialog.show();

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference reference = firebaseStorage.getReference().child(currentUid);

            reference.putFile(imagePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Image uploaded...", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Uri uri= task.getResult();
                                final DocumentReference documentReference = firebaseFirestore.collection("Worker").document(currentUid);

                                HashMap<String,Object> map = new HashMap<>();
                                map.put("name",name);
                                map.put("password",password);
                                map.put("email",email);
                                map.put("serviceType",service);
                                map.put("contact",phone);
                                map.put("uid",currentUid);
                                map.put("city",city);
                                map.put("serviceCharge",price);
                                switch (service){
                                    case "Plumber":
                                        map.put("isPlumber","1");
                                        break;
                                    case "Carpenter":
                                        map.put("isCarpenter","1");
                                        break;
                                    case "Electrician":
                                        map.put("isElectrician","1");
                                        break;
                                    case "Tutor":
                                        map.put("sTutor","1");
                                        break;
                                    case "Driver":
                                        map.put("isDriver","1");
                                        break;
                                    case "Painter":
                                        map.put("isPainter","1");
                                        break;
                                }
                                map.put("image",uri.toString());
                                //map.put();
                                documentReference.update(map);
                            }
                        });

                    }).addOnProgressListener(snapshot -> {
                float percent = ((float) 100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading " + (int) percent + "%");
            });
        }else {
            Toast.makeText(getActivity(), "Please Select image..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Worker").document(currentUid);
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profName = task.getResult().getString("name");
                String profEmail = task.getResult().getString("email");
                String profService = task.getResult().getString("serviceType");
                String profCity = task.getResult().getString("city");
                String profContact = task.getResult().getString("contact");
                String profCharge = task.getResult().getString("serviceCharge");

                wProfName.setText(profName);
                wProfEmail.setText(profEmail);
                wProfService.setText(profService);
                wProfCity.setText(profCity);
                wProfContact.setText(profContact);
                wProfCharge.setText(profCharge);
                Glide.with(wProfImg.getContext()).load(task.getResult().getString("image")).into(wProfImg);

            }
        });

    }
}