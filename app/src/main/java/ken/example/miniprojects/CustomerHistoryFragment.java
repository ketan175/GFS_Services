package ken.example.miniprojects;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * create an instance of this fragment.
 */

public class CustomerHistoryFragment extends Fragment {

    RecyclerView historyRecyclerView;
    TextView txtNoDataHistory;
    ArrayList<HistoryModel> arrayList = new ArrayList<>();
    HistoryAdapter historyAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public CustomerHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_history, container, false);

        historyRecyclerView = view.findViewById(R.id.history_recyclerview);
        txtNoDataHistory = view.findViewById(R.id.txtNoDataHistory);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new HistoryAdapter(arrayList,getContext());
        historyRecyclerView.setAdapter(historyAdapter);

        firebaseFirestore.collection("Worker").get().addOnCompleteListener(task -> {
            Log.i("middleTesting","found");
            List<DocumentSnapshot> list = task.getResult().getDocuments();
            for (DocumentSnapshot documentSnapshot : list) {
                Log.i("afterTesting","found");
                firebaseFirestore.collection("Worker").document(documentSnapshot.getId()).collection("Messages").get().addOnCompleteListener(task12 -> {
                    for (DocumentSnapshot documentSnapshot1 : task12.getResult().getDocuments()) {
                        if (documentSnapshot1.getString("userUid").equals(firebaseAuth.getUid())) {
                            String name = documentSnapshot1.get("date").toString();
                            Log.i("testingName",name);
                            arrayList.add(new HistoryModel((documentSnapshot1.get("date")).toString(),(documentSnapshot1.get("problem")).toString(),(documentSnapshot.get("serviceCharge")).toString()
                                    ,(documentSnapshot1.get("status")).toString(), (documentSnapshot.get("name")).toString()
                                    , (documentSnapshot.get("serviceType")).toString(), (documentSnapshot.get("image")).toString()
                                    , (documentSnapshot.getLong("totalRating")), (documentSnapshot.get("averageRating").toString())));
                            Log.d("testingValue",arrayList.toString());

                        }
                    }
                    historyAdapter.notifyDataSetChanged();
                    if (historyAdapter.getItemCount() != 0){
                        txtNoDataHistory.setVisibility(View.GONE);
                    }
                });
            }
        });

        return view;
    }
}

//