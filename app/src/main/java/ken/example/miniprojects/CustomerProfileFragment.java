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
public class CustomerProfileFragment extends Fragment {

    CircleImageView cProfImg;
    String name,password,email,phone,city,image;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Uri imagePath;
    TextView cProfName,cProfEmail,cProfContact,cProfCity,cProfChange;
    FloatingActionButton cProfEdit;
    Bitmap bitmap;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = firebaseUser.getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_fragment_profile, container, false);

        cProfName = view.findViewById(R.id.c_prof_name);
        cProfEmail = view.findViewById(R.id.c_prof_email);
        cProfContact = view.findViewById(R.id.c_prof_contact);
        cProfCity = view.findViewById(R.id.c_prof_city);
        cProfChange = view.findViewById(R.id.c_prof_change);
        cProfImg = view.findViewById(R.id.c_prof_image);
        cProfEdit = view.findViewById(R.id.c_prof_edit);


        ActivityResultLauncher<String> intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), result -> {
                    imagePath= result;
                    try {
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(imagePath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        cProfImg.setImageBitmap(bitmap);
                        uploadImageToFireBase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        DocumentReference documentReference = firebaseFirestore.collection("Customer").document(currentUid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                name = task.getResult().getString("name");
                phone = task.getResult().getString("contact");
                email = task.getResult().getString("email");
                password = task.getResult().getString("password");
                city = task.getResult().getString("city");
                image = task.getResult().getString("image");

            }
        });

        cProfEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),CustomerProfileEdit.class);
            startActivity(intent);
        });

        cProfImg.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),WorkerProfileShow.class);
            intent.putExtra("userimage",image);
            startActivity(intent);
        });
        cProfChange.setOnClickListener(v -> Dexter.withActivity(getActivity())
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
                                final DocumentReference documentReference = firebaseFirestore.collection("Customer").document(currentUid);

                                HashMap<String,Object> map = new HashMap<>();
                                map.put("name",name);
                                map.put("password",password);
                                map.put("email",email);
                                map.put("contact",phone);
                                map.put("uid",currentUid);
                                map.put("city",city);

                                map.put("image",uri.toString());
                                documentReference.update(map);
                                //map.put();
                            }
                        });

                    }).addOnProgressListener(snapshot -> {
                float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
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
        reference = firebaseFirestore.collection("Customer").document(currentUid);
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profName = task.getResult().getString("name");
                String profEmail = task.getResult().getString("email");
                String profCity = task.getResult().getString("city");
                String profContact = task.getResult().getString("contact");

                cProfName.setText(profName);
                cProfEmail.setText(profEmail);
                cProfCity.setText(profCity);
                cProfContact.setText(profContact);
                Glide.with(cProfImg.getContext()).load(task.getResult().getString("image")).into(cProfImg);

            }
        });

    }
}