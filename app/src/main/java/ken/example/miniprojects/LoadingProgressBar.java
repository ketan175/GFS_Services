package ken.example.miniprojects;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;

public class LoadingProgressBar {
    Activity activity;
    Dialog alertDialog;

    LoadingProgressBar(Activity activity){
        this.activity = activity;
    }
    void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_layout,null));
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();
    }
}
