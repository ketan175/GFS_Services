package ken.example.miniprojects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class ServicesAdapter extends BaseAdapter {
private ArrayList<ServicesModel> servicesModelArrayList;
private Context serviceContext;
    public ServicesAdapter(ArrayList<ServicesModel> servicesModelArrayList,Context serviceContext) {
        this.serviceContext = serviceContext;
        this.servicesModelArrayList = servicesModelArrayList;
    }

/*
    @NonNull
    @Override
    public rsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_recycler_view,null);
        return new rsAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rsAdapter holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return servicesModelArrayList.size();
    }
*/

    @Override
    public int getCount() {
        return servicesModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView ==null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_recycler_view, null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            ImageView servicesImageView;
            TextView servicesTextView;
            CardView servicesCardView;
            servicesImageView = view.findViewById(R.id.recycler_services_image);
            servicesTextView = view.findViewById(R.id.recycler__service_text);
            servicesCardView = view.findViewById(R.id.service_layout);
            servicesImageView.setImageResource(servicesModelArrayList.get(position).getLogoImage());
            servicesTextView.setText(servicesModelArrayList.get(position).getServiceName());
            servicesCardView.setOnClickListener(v -> {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(serviceContext, PlumberDetailsPage.class);
                        serviceContext.startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(serviceContext, CarpenterDetailsPage.class);
                        serviceContext.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(serviceContext, ElectricianDetailsPage.class);
                        serviceContext.startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(serviceContext, TutorDetailsPage.class);
                        serviceContext.startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(serviceContext, PainterDetailsPage.class);
                        serviceContext.startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(serviceContext, DriverDetailsPage.class);
                        serviceContext.startActivity(intent5);
                        break;
                }
            });
        }else {
            view = convertView;
        }
        return view;
    }

    /*public class rsAdapter extends RecyclerView.ViewHolder {
        ImageView servicesImageView;
        TextView servicesTextView;
        CardView servicesCardView;
        public rsAdapter(@NonNull final View itemView) {
            super(itemView);
            servicesImageView = itemView.findViewById(R.id.recycler_services_image);
            servicesTextView = itemView.findViewById(R.id.recycler__service_text);
            servicesCardView = itemView.findViewById(R.id.service_layout);

        }
    }*/




}
