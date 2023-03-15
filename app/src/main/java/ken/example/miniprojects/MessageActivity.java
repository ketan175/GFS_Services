package ken.example.miniprojects;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    EditText msgName, msgContact, msgProblem, msgAddress;
    Button send;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String currentUser = firebaseUser.getUid();
    Bundle bundle;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String workeruid;
    String workerType;
    private final LoadingProgressBar progressDialog = new LoadingProgressBar(MessageActivity.this);

    ///////Rating Layout

    public static int initialRating;
    public static Long txtRating;
    public static LinearLayout starRating;
    TextView txtTotalRating;
    LinearLayout txtCountRating;
    TextView txtAvgRating;
    LinearLayout ratingProgressbar;
    DocumentSnapshot documentSnapshot;
    public static boolean running_rating_query = false;

    ///////Rating Layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        msgName = findViewById(R.id.msgName);
        msgAddress = findViewById(R.id.msgAddress);
        msgContact = findViewById(R.id.msgContact);
        msgProblem = findViewById(R.id.msgProblem);
        send = findViewById(R.id.send);

        bundle = getIntent().getExtras();
        workeruid = bundle.getString("workerUid");
        workerType = bundle.getString("workType");

        //////// Rating Layout

        starRating = findViewById(R.id.star_rating_container);
        txtTotalRating = findViewById(R.id.txt_total_rating);
        txtCountRating = findViewById(R.id.txt_count_rating);
        ratingProgressbar = findViewById(R.id.ratingProgress);
        txtAvgRating = findViewById(R.id.txt_avg_rating);

        initialRating = -1;
        //////// Rating Layout



        //////// Rating Layout

        firebaseFirestore.collection("Worker").document(workeruid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                documentSnapshot = task.getResult();
                txtTotalRating.setText((long) documentSnapshot.get("totalRating") + " ratings");
                txtAvgRating.setText(documentSnapshot.get("averageRating").toString());
                txtRating = ((long) documentSnapshot.get("totalRating"));
                for (int x = 0; x < 5; x++) {
                    TextView rating = (TextView) txtCountRating.getChildAt(x);
                    rating.setText(String.valueOf((long) documentSnapshot.get(5 - x + "_star")));
                    //Log.d("checkingValue", String.valueOf((long) documentSnapshot.get(5 - x + "_star")));

                    ProgressBar progressBar = (ProgressBar) ratingProgressbar.getChildAt(x);
                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("totalRating")));
                    progressBar.setMax(maxProgress);
                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                }
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            progressDialog.dismissDialog();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        });

        if (DbQueries.myRating.size() == 0) {
            DbQueries.loadRating(MessageActivity.this);
        }
        if (DbQueries.myRateIds.contains(workeruid)) {
            int index = DbQueries.myRateIds.indexOf(workeruid);
            initialRating = Integer.parseInt(String.valueOf(DbQueries.myRating.get(index))) - 1;
            setRating(initialRating);
        }

        for (int x = 0; x < starRating.getChildCount(); x++) {
            final int starPosition = x;
            starRating.getChildAt(x).setOnClickListener(v -> {
                progressDialog.showDialog();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (starPosition != initialRating) {
                    if (!running_rating_query) {
                        running_rating_query = true;
                        setRating(starPosition);
                        Map<String, Object> updateRating = new HashMap<>();

                        if (DbQueries.myRateIds.contains(workeruid)) {
                            TextView oldRating = (TextView) txtCountRating.getChildAt(5 - initialRating - 1);
                            TextView finalRating = (TextView) txtCountRating.getChildAt(5 - starPosition - 1);

                            updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                            updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                            updateRating.put("averageRating", calculateAverageRating((long) starPosition - initialRating, true));
                        } else {
                            updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                            updateRating.put("totalRating", (long) documentSnapshot.get("totalRating") + 1);
                            updateRating.put("averageRating", calculateAverageRating((long) starPosition + 1, false));
                        }
                        firebaseFirestore.collection("Worker").document(workeruid).update(updateRating)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> myRatings = new HashMap<>();
                                        if (DbQueries.myRateIds.contains(workeruid)) {
                                            myRatings.put("rating_" + DbQueries.myRateIds.indexOf(workeruid), (long) starPosition + 1);
                                        } else {
                                            myRatings.put("list_size", (long) DbQueries.myRateIds.size() + 1);
                                            myRatings.put("worker_id_" + DbQueries.myRateIds.size(), workeruid);
                                            myRatings.put("rating_" + DbQueries.myRateIds.size(), (long) starPosition + 1);
                                        }

                                        Log.d("messageGet", "how r");

                                        firebaseFirestore.collection("Customer").document(currentUser).collection("User Data")
                                                .document("My_Ratings").update(myRatings).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (DbQueries.myRateIds.contains(workeruid)) {
                                                        DbQueries.myRating.set(DbQueries.myRateIds.indexOf(workeruid), (long) starPosition + 1);
                                                        TextView oldRating = (TextView) txtCountRating.getChildAt(5 - initialRating - 1);
                                                        TextView finalRating = (TextView) txtCountRating.getChildAt(5 - starPosition - 1);
                                                        oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                        finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                    } else {
                                                        Log.d("messageFound", "Thank");
                                                        TextView rating = (TextView) txtCountRating.getChildAt(5 - starPosition - 1);
                                                        rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                        DbQueries.myRateIds.add(workeruid);
                                                        DbQueries.myRating.add((long) starPosition + 1);
                                                        txtTotalRating.setText((long) documentSnapshot.get("totalRating") + 1 + " ratings");
                                                        txtRating = ((long) documentSnapshot.get("totalRating") + 1);
                                                        Log.d("testingData", txtRating.toString());
                                                        Toast.makeText(MessageActivity.this, "Thank you For Rating", Toast.LENGTH_SHORT).show();
                                                    }
                                                    for (int x1 = 0; x1 < 5; x1++) {
                                                        TextView ratingFigures = (TextView) txtCountRating.getChildAt(x1);

                                                        ProgressBar progressBar = (ProgressBar) ratingProgressbar.getChildAt(x1);
                                                        int maxProgress = Integer.parseInt(String.valueOf(txtRating));
                                                        progressBar.setMax(maxProgress);
                                                        progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                    }
                                                    initialRating = starPosition;
                                                    txtAvgRating.setText(calculateAverageRating(0, true));
                                                    progressDialog.dismissDialog();
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                } else {
                                                    progressDialog.showDialog();
                                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(MessageActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_rating_query = false;
                                            }
                                        });

                                    } else {
                                        progressDialog.dismissDialog();
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        running_rating_query = false;
                                        setRating(initialRating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(MessageActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }

        //////// Rating Layout


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss a");
        String dateToStr = format.format(today);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName = msgName.getText().toString();
                String mAddress = msgAddress.getText().toString();
                String mContact = msgContact.getText().toString();
                String mProblem = msgProblem.getText().toString();


                if (mName.isEmpty()) {
                    msgName.setError("Please Enter Name");
                } else if (mAddress.isEmpty()) {
                    msgAddress.setError("Please Enter Address");
                } else if (mContact.isEmpty()) {
                    msgContact.setError("Please Enter Contact");
                }else if (!Patterns.PHONE.matcher(mContact).matches()) {
                    msgContact.setError("Enter valid contact");
                } else if (mContact.length() < 10 || mContact.length() > 12) {
                    msgContact.setError("Enter Valid contact");
                } else if (mProblem.isEmpty()) {
                    msgProblem.setError("Please Enter Problem");
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
                    builder.setTitle("Send");
                    builder.setMessage("Are you sure do want to send message");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.showDialog();
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            DocumentReference documentReference = firebaseFirestore.collection("Worker").document(workeruid).collection("Messages").document();
                            Map<String, Object> map = new HashMap<String, Object>();
                            String message = "Name: " + msgName.getText().toString() + "\nNeed: " + workerType +
                                    "\nAddress: " + msgAddress.getText().toString() + "\nContact: " + msgContact.getText().toString()
                                    + "\nProblem: " + msgProblem.getText().toString();

                            map.put("userMessage", message);
                            map.put("date", dateToStr);
                            map.put("problem",msgProblem.getText().toString());
                            map.put("userUid",firebaseAuth.getUid());
                            map.put("workerUid",workeruid);
                            map.put("status","pending");

                            documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismissDialog();
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Toast.makeText(MessageActivity.this, "Message send successfully...", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MessageActivity.this, CustomerNavigationActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        progressDialog.dismissDialog();
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Toast.makeText(MessageActivity.this, "Sending failed...\nTry Again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();

                }
            }
        });
    }

    public static void setRating(int starPosition) {

        for (int i = 0; i < starRating.getChildCount(); i++) {

            ImageView starBtn = (ImageView) starRating.getChildAt(i);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#89000000")));
            if (i <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#DDC707")));
            }

        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        double totalStars = 0;
        for (int i = 1; i < 6; i++) {
            TextView countRating = (TextView) txtCountRating.getChildAt(5 - i);
            totalStars = totalStars + (Long.parseLong(countRating.getText().toString()) * i);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / txtRating).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (txtRating + 1)).substring(0, 3);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismissDialog();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.showDialog();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (DbQueries.myRating.size() == 0) {
            DbQueries.loadRating(MessageActivity.this);
        }
        if (DbQueries.myRateIds.contains(workeruid)) {
            int index = DbQueries.myRating.indexOf(workeruid);
            initialRating = Integer.parseInt(String.valueOf(DbQueries.myRating.get(index))) - 1;
            setRating(initialRating);

        }

    }
}