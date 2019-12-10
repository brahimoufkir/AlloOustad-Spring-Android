package com.ensak.AlloOustad.ui;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;

import com.ensak.AlloOustad.R;
import com.ensak.AlloOustad.database.AppDatabase;
import com.ensak.AlloOustad.database.EnseignantEntry;
import com.squareup.picasso.Picasso;

import java.util.List;


public class EnseignantInfoWidget extends AppWidgetProvider {

    private static EnseignantEntry mEnseignantEntry;

    private static Context context;
    private static AppWidgetManager appWidgetManager;
    private static int appWidgetId;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetId = appWidgetId;

        SavedEnseignant savedEnseignant = new SavedEnseignant();
        savedEnseignant.execute();

    }

    private static void initViews() {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mEnseignantEntry.getLandLineNumber(), null));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.doctor_info_widget);
        views.setTextViewText(R.id.widget_name, mEnseignantEntry.getFirstName() + " " + mEnseignantEntry.getLastName());
        views.setTextViewText(R.id.widget_specialty, mEnseignantEntry.getTitle() + ", " + mEnseignantEntry.getSpecialtyName());
        views.setTextViewText(R.id.widget_location, mEnseignantEntry.getState() + ", " + mEnseignantEntry.getCity() + ", " + mEnseignantEntry.getStreet());
        views.setOnClickPendingIntent(R.id.widget_call_button, pendingIntent);


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(() -> Picasso.with(context)
                .load(mEnseignantEntry.getProfileImage())
                .into(views, R.id.widget_profile_image, new int[]{appWidgetId}));


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static class SavedEnseignant
            extends AsyncTask<Void, Void, EnseignantEntry> {

        final AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());

        @Override
        protected EnseignantEntry doInBackground(Void... voids) {

            List<EnseignantEntry> list = appDatabase.doctorDao().loadEnseignantsFoeWidget();
            return list.get(list.size() - 1);
        }

        @Override
        protected void onPostExecute(EnseignantEntry doctorEntry) {
            super.onPostExecute(doctorEntry);
            mEnseignantEntry = doctorEntry;
            initViews();
        }
    }
}

