package com.appart.hp.nearme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by HP on 7/26/2017.
 */

public class SendMailTask extends AsyncTask {

    private ProgressDialog statusDialog;
    private Activity mainactivity ;

    public SendMailTask(Activity activity) {
        mainactivity = activity;

    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(mainactivity);
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }
    @Override
    protected Object doInBackground(Object[] args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            GMailsender androidEmail = new GMailsender(args[0].toString(),
                    args[1].toString());

            publishProgress("Preparing mail message....");
            publishProgress("Sending email....");
            androidEmail.sendMail(args[3].toString(),args[4].toString(),args[0].toString(),args[2].toString());


            publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }
}
