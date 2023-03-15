package ken.example.miniprojects;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DbQueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<String> myRateIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static void loadRating(final Context context) {
        if (!MessageActivity.running_rating_query) {
            MessageActivity.running_rating_query = true;

            myRateIds.clear();
            myRating.clear();
            firebaseFirestore.collection("Customer").document(FirebaseAuth.getInstance().getUid()).collection("User Data").document("My_Ratings").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {
                        myRateIds.add((task.getResult().get("worker_id_" + i)).toString());
                        myRating.add((long) task.getResult().get("rating_" + i));
                        if (task.getResult().get("worker_id_" + i).toString().equals(MessageActivity.workeruid)) {
                            MessageActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + i))) - 1;
                            if (MessageActivity.starRating != null) {
                                MessageActivity.setRating(MessageActivity.initialRating);

                            }
                        }
                    }


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

                MessageActivity.running_rating_query = false;
            });

        }
    }

}
