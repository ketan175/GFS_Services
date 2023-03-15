package ken.example.miniprojects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
ArrayList<HistoryModel> arrayList;
Context context;

    public HistoryAdapter(ArrayList<HistoryModel> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull HistoryAdapter.ViewHolder holder, int position) {
        holder.txtTime.setText(arrayList.get(position).getDate());
        holder.txtUsername.setText(arrayList.get(position).getName());
        holder.txtService.setText(arrayList.get(position).getServiceType());
        holder.txtTotalRate.setText(arrayList.get(position).getTotalRating() + " ratings");
        holder.txtAverageRate.setText(arrayList.get(position).getAverageRating());
        holder.txtPrice.setText(arrayList.get(position).getServiceCharge());
        holder.txtProblem.setText(arrayList.get(position).getProblem());
        Glide.with(holder.imgProfile.getContext()).load(arrayList.get(position).getImage()).into(holder.imgProfile);

        if ((arrayList.get(position).getStatus()).equals("pending")){
            holder.requestText.setText("Request Pending");
            holder.requestText.setTextColor(Color.RED);
        }
        if ((arrayList.get(position).getStatus()).equals("accept")){
            holder.requestText.setText("Request Accepted");
            holder.requestText.setTextColor(Color.GREEN);
        }
        if ((arrayList.get(position).getStatus()).equals("decline")){
            holder.requestText.setText("Request Declined");
            holder.requestText.setTextColor(Color.GREEN);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtService,txtPrice,txtTime,txtUsername,txtAverageRate,txtTotalRate,txtProblem;
        ImageView imgProfile;
        TextView requestText;
        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            txtService = itemView.findViewById(R.id.txtService);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtAverageRate = itemView.findViewById(R.id.txt_average_rating);
            txtTotalRate = itemView.findViewById(R.id.txt_total_rate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtProblem = itemView.findViewById(R.id.txtProblem);
            requestText = itemView.findViewById(R.id.requestText);

        }
    }
}
