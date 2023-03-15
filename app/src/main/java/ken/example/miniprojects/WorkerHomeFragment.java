package ken.example.miniprojects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * create an instance of this fragment.
 */
public class WorkerHomeFragment extends Fragment {
    RecyclerView workRecyclerView;
    TextView workerHomeTextView;
    ArrayList<RequestModel> requestArray = new ArrayList<>();
    RequestAdapter requestAdapter;
    public static FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    public static String currentUid = firebaseUser.getUid();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_worker_home, container, false);
        workRecyclerView = view.findViewById(R.id.work_recyclerview);
        workerHomeTextView = view.findViewById(R.id.worker_home_textview);
        serviceRequests(workRecyclerView,workerHomeTextView);

        return view;
    }

    private void serviceRequests(RecyclerView recyclerView,TextView workerHomeTextView) {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestAdapter = new RequestAdapter(requestArray);
        recyclerView.setAdapter(requestAdapter);

        Task<QuerySnapshot> worker = firebaseFirestore.collection("Worker")
                .document(currentUid)
                .collection("Messages").orderBy("date")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            requestArray.clear();
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

            for (DocumentSnapshot documentSnapshot : list) {
                RequestModel model = documentSnapshot.toObject(RequestModel.class);
                requestArray.add(model);
            }
            requestAdapter.notifyDataSetChanged();

            if (requestAdapter.getItemCount() != 0){
                workerHomeTextView.setVisibility(View.GONE);
            }
        });
    }
}