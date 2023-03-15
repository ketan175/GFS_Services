package ken.example.miniprojects;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.requestVH> {
ArrayList<RequestModel> requestArray;

    public RequestAdapter(ArrayList<RequestModel> requestArray) {
        this.requestArray = requestArray;
    }

    @NonNull
    @Override
    public requestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_page_design,parent,false);
        return new requestVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.requestVH holder, int position) {
        holder.msgText.setText(requestArray.get(position).getUserMessage());
        holder.dateText.setText(requestArray.get(position).getDate());


        if ((requestArray.get(position).getStatus()).equals("pending")) {
            holder.btnAccept.setOnClickListener(v -> {
                Map<String, Object> map = new HashMap<>();
                WorkerHomeFragment.firebaseFirestore.collection("Worker").document(requestArray.get(position).workerUid)
                        .collection("Messages").get().addOnCompleteListener(task -> {
                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : list) {
                        if (documentSnapshot.get("userUid").equals(requestArray.get(position).getUserUid()) && documentSnapshot.get("date").equals(requestArray.get(position).getDate())) {
                            map.put("status", "accept");
                            DocumentReference documentReference = WorkerHomeFragment.firebaseFirestore.collection("Worker").document(requestArray.get(position).workerUid)
                                    .collection("Messages").document(documentSnapshot.getId());
                            documentReference.update(map);
                            holder.btnDecline.setVisibility(View.GONE);
                            holder.btnAccept.setVisibility(View.GONE);
                            holder.txtStatus.setVisibility(View.VISIBLE);
                            holder.txtStatus.setText("Request Accepted");
                            holder.txtStatus.setTextColor(Color.GREEN);
                        }
                    }

                });
            });
            holder.btnDecline.setOnClickListener(v -> {
                Map<String, Object> map = new HashMap<>();
                WorkerHomeFragment.firebaseFirestore.collection("Worker").document(requestArray.get(position).workerUid)
                        .collection("Messages").get().addOnCompleteListener(task -> {
                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : list) {
                        if (documentSnapshot.get("userUid").equals(requestArray.get(position).getUserUid()) && documentSnapshot.get("date").equals(requestArray.get(position).getDate())) {
                            map.put("status", "decline");
                            DocumentReference documentReference = WorkerHomeFragment.firebaseFirestore.collection("Worker").document(requestArray.get(position).workerUid)
                                    .collection("Messages").document(documentSnapshot.getId());
                            documentReference.update(map);

                            holder.btnDecline.setVisibility(View.GONE);
                            holder.btnAccept.setVisibility(View.GONE);
                            holder.txtStatus.setVisibility(View.VISIBLE);
                            holder.txtStatus.setText("Request Declined");
                            holder.txtStatus.setTextColor(Color.GREEN);
                        }
                    }

                });
            });
        }
        if ((requestArray.get(position).getStatus().equals("accept"))){
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnDecline.setVisibility(View.GONE);
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setText("Request Accepted");
            holder.txtStatus.setTextColor(Color.GREEN);

        }
        if ((requestArray.get(position).getStatus().equals("decline"))){
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnDecline.setVisibility(View.GONE);
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtStatus.setText("Request Declined");
            holder.txtStatus.setTextColor(Color.GREEN);

        }

    }

    @Override
    public int getItemCount() {
        return requestArray.size();
    }

    public class requestVH extends RecyclerView.ViewHolder {
        TextView msgText;
        TextView dateText;
        TextView txtStatus;
        Button btnAccept,btnDecline;

        public requestVH(@NonNull View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.msgText);
            dateText = itemView.findViewById(R.id.dateText);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }
}
