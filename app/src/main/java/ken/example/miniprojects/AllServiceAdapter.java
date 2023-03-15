package ken.example.miniprojects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllServiceAdapter extends RecyclerView.Adapter<AllServiceAdapter.myViehHolder> {
    public AllServiceAdapter(ArrayList<AllServiceModel> workerdetail,Context context) {
        this.workerdetail = workerdetail;
        this.context = context;
    }

    ArrayList<AllServiceModel> workerdetail;
    Context context;
    @NonNull
    @Override
    public myViehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_worker_details,parent,false);
        return new myViehHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllServiceAdapter.myViehHolder holder, int position) {
        holder.nameText.setText(workerdetail.get(position).getName());
        holder.phoneText.setText(workerdetail.get(position).getContact());
        holder.cityText.setText(workerdetail.get(position).getCity());
        holder.priceText.setText(workerdetail.get(position).getServiceCharge());
        Glide.with(holder.imageView.getContext()).load(workerdetail.get(position).getImage()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkerProfileShow.class);
                intent.putExtra("userimage",workerdetail.get(position).getImage());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MessageActivity.class);
                intent.putExtra("workerUid",workerdetail.get(position).getUid());
                intent.putExtra("workType",workerdetail.get(position).getServiceType());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return workerdetail.size();
    }

    public class myViehHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView nameText,phoneText,cityText,priceText;
        CardView cardView;

        public myViehHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textWName);
            phoneText = itemView.findViewById(R.id.textWContact);
            cityText = itemView.findViewById(R.id.textWCity);
            priceText = itemView.findViewById(R.id.textWPrice);
            imageView = itemView.findViewById(R.id.workerImage);
            cardView= itemView.findViewById(R.id.cardView);

        }
    }
}
