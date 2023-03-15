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

public class PainterDetailsPage extends AppCompatActivity {
    RecyclerView painterRv;
    ArrayList<AllServiceModel> workerdetail = new ArrayList<>();
    AllServiceAdapter allServiceAdapter;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter_details_page);

        painterRv = findViewById(R.id.painterRecyclerview);
        firebaseFirestore = FirebaseFirestore.getInstance();


        getSupportActionBar().setTitle("Painter");

        painterRv.setHasFixedSize(true);
        painterRv.setLayoutManager(new LinearLayoutManager(this));
        allServiceAdapter = new AllServiceAdapter(workerdetail,this);
        painterRv.setAdapter(allServiceAdapter);

        final LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(this,R.anim.layout_animation_up_to_down);
        painterRv.setLayoutAnimation(controller);
        painterRv.scheduleLayoutAnimation();


        Task<QuerySnapshot> worker =firebaseFirestore.collection("Worker").get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot d:list){
                if (d.getString("isPainter") != null && d.contains("emailVerified")){
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