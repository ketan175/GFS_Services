package ken.example.miniprojects;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TutorDetailsPage extends AppCompatActivity {
    RecyclerView tutorRv;
    ArrayList<AllServiceModel> workerdetail = new ArrayList<>();
    AllServiceAdapter allServiceAdapter;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details_page);

        tutorRv = findViewById(R.id.tutorRecyclerview);
        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("Tutor");

        tutorRv.setHasFixedSize(true);
        tutorRv.setLayoutManager(new LinearLayoutManager(this));
        allServiceAdapter = new AllServiceAdapter(workerdetail,this);
        tutorRv.setAdapter(allServiceAdapter);

        Task<QuerySnapshot> worker =firebaseFirestore.collection("Worker").get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot d:list){
                if (d.getString("isTutor") != null && d.contains("emailVerified")){
                    
                    AllServiceModel model = d.toObject(AllServiceModel.class);

                    workerdetail.add(model);
                }
            }
            allServiceAdapter.notifyDataSetChanged();
        });
    }
}