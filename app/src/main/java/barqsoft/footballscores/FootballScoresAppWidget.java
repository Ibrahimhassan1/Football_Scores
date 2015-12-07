package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import barqsoft.footballscores.service.myFetchService;

/**
 * Implementation of App Widget functionality.
 */
public class FootballScoresAppWidget extends AppWidgetProvider {

    public static String EXTRA_WORD =
            "com.barqsoft.footballscores.WORD";

    private static final String NEXT_DAY_CLICKED = "com.barqsoft.footballscores.nextday";
    private static final String PREVIOUS_DAY_CLICKED = "com.barqsoft.footballscores.previousday";
    public static final String SELECTED_DAY = "com.barqsoft.footballscores.selectedday";

    private static int mCount = -2;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // call score service on every update
        update_scores(context);
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;


        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    private void update_scores(Context context)
    {
        Intent service_start = new Intent(context, myFetchService.class);
        context.startService(service_start);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d("updateAppWidget", "updateAppWidget");
        Intent svcIntent = new Intent(context, FootballScoresWidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.putExtra(SELECTED_DAY, GetSelectedDate());


        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.football_scores_app_widget);

        views.setOnClickPendingIntent(R.id.next_day, getPendingSelfIntent(context, NEXT_DAY_CLICKED));
        views.setOnClickPendingIntent(R.id.previous_day, getPendingSelfIntent(context, PREVIOUS_DAY_CLICKED));

        views.setTextViewText(R.id.data_textview, "" + GetSelectedDate());
        views.setContentDescription(R.id.data_textview,GetSelectedDate());

        views.setRemoteAdapter(appWidgetId, R.id.scores_widget_listview,
                svcIntent);

        Intent clickIntent = new Intent(context, DetailViewActivity.class);
        PendingIntent clickPI = PendingIntent
                .getActivity(context, 0,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.scores_widget_listview, clickPI);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);


        if (NEXT_DAY_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.football_scores_app_widget);
            watchWidget = new ComponentName(context, FootballScoresAppWidget.class);
            mCount++;
            remoteViews.setTextViewText(R.id.data_textview, "" + GetSelectedDate());

            // calling update list view
            Intent svcIntent = new Intent(context, FootballScoresWidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.putExtra(SELECTED_DAY, GetSelectedDate());

            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            remoteViews.setOnClickPendingIntent(R.id.next_day, getPendingSelfIntent(context, NEXT_DAY_CLICKED));
            remoteViews.setOnClickPendingIntent(R.id.previous_day, getPendingSelfIntent(context, PREVIOUS_DAY_CLICKED));


            remoteViews.setRemoteAdapter(appWidgetId, R.id.scores_widget_listview,
                    svcIntent);

            Intent clickIntent = new Intent(context, DetailViewActivity.class);
            PendingIntent clickPI = PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.scores_widget_listview, clickPI);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);


        }
        if (PREVIOUS_DAY_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.football_scores_app_widget);
            watchWidget = new ComponentName(context, FootballScoresAppWidget.class);
            mCount--;
            remoteViews.setTextViewText(R.id.data_textview, "" + GetSelectedDate());

            // calling update list view
            Intent svcIntent = new Intent(context, FootballScoresWidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.putExtra(SELECTED_DAY, GetSelectedDate());

            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            remoteViews.setOnClickPendingIntent(R.id.next_day, getPendingSelfIntent(context, NEXT_DAY_CLICKED));
            remoteViews.setOnClickPendingIntent(R.id.previous_day, getPendingSelfIntent(context, PREVIOUS_DAY_CLICKED));


            remoteViews.setRemoteAdapter(appWidgetId, R.id.scores_widget_listview,
                    svcIntent);

            Intent clickIntent = new Intent(context, DetailViewActivity.class);
            PendingIntent clickPI = PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.scores_widget_listview, clickPI);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);


        }
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, FootballScoresAppWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static String GetSelectedDate() {
        // default selected date is today's date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +mCount);
        String date = dateFormat.format(cal.getTime());
        return date;
    }
    private void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, FootballScoresAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_widget_listview);
    }

}

