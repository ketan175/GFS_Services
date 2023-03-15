package ken.example.miniprojects;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class WorkerProfileShow extends AppCompatActivity {
ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile_change);

        profileImage = findViewById(R.id.profile_image);


        Log.d("img",getIntent().getStringExtra("userimage"));
        Glide.with(profileImage.getContext()).load(getIntent().getStringExtra("userimage")).into(profileImage);
    }
}