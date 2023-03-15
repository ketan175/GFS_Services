package ken.example.miniprojects;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.navigation.Navigation.findNavController;

public class WorkerNavigationActivity extends AppCompatActivity {

    private AppBarConfiguration wAppBarConfiguration;
    TextView workUserName, workUserEmail;
    View workView;
    FirebaseAuth firebaseAuth;
    CircleImageView workImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_navigation);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.worker_toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.work_drawer_layout);
        NavigationView navigationView = findViewById(R.id.work_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        wAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.work_nav_home,R.id.work_nav_about_us,R.id.work_nav_help_and_feedback,R.id.work_nav_profile)
                .setOpenableLayout(drawer)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.work_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();


        workView = navigationView.getHeaderView(0);

        workUserName = (TextView) workView.findViewById(R.id.work_userName);
        workUserEmail = (TextView) workView.findViewById(R.id.work_userEmail);
        workImageView = (CircleImageView) workView.findViewById(R.id.work_imageView);

        NavigationUI.setupActionBarWithNavController(this, navController, wAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.worker_navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = findNavController(this, R.id.work_nav_host_fragment);
        return NavigationUI.navigateUp(navController, wAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.work_logout) {
            Dialog dialog = new Dialog(WorkerNavigationActivity.this,R.style.Dialoge);
            dialog.setContentView(R.layout.dialoge_layout);
            dialog.show();
            TextView yesBtn,noBtn;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            noBtn = dialog.findViewById(R.id.noBtn);
            yesBtn.setOnClickListener(v -> {
                firebaseAuth.signOut();
                Intent intent = new Intent(WorkerNavigationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Toast.makeText(WorkerNavigationActivity.this, "Logout successfully...", Toast.LENGTH_SHORT).show();
            });
            noBtn.setOnClickListener(v -> dialog.dismiss());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Worker").document(currentUid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String name1 = task.getResult().getString("name");
                    String email1 = task.getResult().getString("email");
                    workUserName.setText(name1);
                    workUserEmail.setText(email1);
                    Glide.with(workImageView.getContext()).load(task.getResult().getString("image")).into(workImageView);
                }
            }
        });
    }
}