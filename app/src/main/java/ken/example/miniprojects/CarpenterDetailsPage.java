package ken.example.miniprojects;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CarpenterDetailsPage extends AppCompatActivity {

RecyclerView carpenterRv;
ArrayList<AllServiceModel> workerdetail = new ArrayList<>();
AllServiceAdapter allServiceAdapter;
FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpenter_details_page);


        getSupportActionBar().setTitle("Carpenter");

        carpenterRv = findViewById(R.id.carpenterRecyclerview);
        firebaseFirestore = FirebaseFirestore.getInstance();

        carpenterRv.setHasFixedSize(true);
        carpenterRv.setLayoutManager(new LinearLayoutManager(this));
        allServiceAdapter = new AllServiceAdapter(workerdetail,this);
        carpenterRv.setAdapter(allServiceAdapter);

        final LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(this,R.anim.layout_animation_up_to_down);
        carpenterRv.setLayoutAnimation(controller);
        carpenterRv.scheduleLayoutAnimation();


        Task<QuerySnapshot> worker =firebaseFirestore.collection("Worker").get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot d:list){
                if (d.getString("isCarpenter") != null && d.contains("emailVerified")){
                    Log.d("confirm","success");
                    AllServiceModel model = d.toObject(AllServiceModel.class);
                    Log.i("dataSuccess","hii");
                    workerdetail.add(model);
                }
            }
            allServiceAdapter.notifyDataSetChanged();
        });


    }
}