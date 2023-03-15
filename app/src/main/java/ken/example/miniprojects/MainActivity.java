package ken.example.miniprojects;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    LinearLayout customerLayout, workerLayout;

//    static SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        customerLayout = findViewById(R.id.customer_layout);
        workerLayout = findViewById(R.id.worker_layout);

    /*    sp = getSharedPreferences("login", MODE_PRIVATE);


        if (sp.getBoolean("customerLogged", true)) {
            Intent intent = new Intent(MainActivity.this, CustomerNavigationActivity.class);
            startActivity(intent);
            finish();
        } else if (sp.getBoolean("workerLogged", true)) {
            Intent intent = new Intent(MainActivity.this, WorkerNavigationActivity.class);
            startActivity(intent);
            finish();
        }
*/
        customerLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CustomerLoginPage.class);
            startActivity(intent);
            finish();
        });

        workerLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorkerLoginPage.class);
            startActivity(intent);
            finish();
        });
//        new checkLogin(this).execute();



    }

//    class checkLogin extends AsyncTask {
//
//        Context context;
//
//        checkLogin(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            if (sp.getBoolean("customerLogged", false)) {
//                Intent intent = new Intent(context, CustomerNavigationActivity.class);
//                context.startActivity(intent);
//                ((Activity) context).finish();
//            } else if (sp.getBoolean("workerLogged", false)) {
//                Intent intent = new Intent(context, WorkerNavigationActivity.class);
//                context.startActivity(intent);
//                ((Activity) context).finish();
//            }
//
//        }
//    }
}