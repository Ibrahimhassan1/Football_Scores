package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ibrahimhassan on 9/15/15.
 */
public class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    private ArrayList<String> items = new ArrayList<>();

    private Context ctxt = null;
    private int appWidgetId;
    private Cursor mCursor;

    String selectedDate;

    public ListViewRemoteViewsFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        String date = dateFormat.format(cal.getTime());

        selectedDate = intent.getStringExtra(FootballScoresAppWidget.SELECTED_DAY);
        if (selectedDate != null) {
            date = selectedDate;
        }
//        Log.d("selectedDate", "selectedDate " + selectedDate);
        refreshCursor();

    }

    void refreshCursor() {
        // read from database
        Uri scoreWithDateUri = DatabaseContract.scores_table.buildScoreWithDate();

        mCursor = ctxt.getContentResolver().query(scoreWithDateUri,
                SCORE_COLUMNS,
                null,
                new String[]{selectedDate},
                DatabaseContract.scores_table.HOME_GOALS_COL + " ASC");
        if (mCursor != null && mCursor.getCount() > 0) {
            items = new ArrayList<>();
            while (mCursor.moveToNext()) {
//                Log.d("Cursor", "" + mCursor.getString(0));
                items.add(mCursor.getString(4) + " : " + mCursor.getString(0) + " " + mCursor.getString(2) + "\n             " + mCursor.getString(1) + " " + mCursor.getString(3));
            }
            mCursor.close();
        }
    }

    @Override
    public void onCreate() {
        // no-op

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return (items.size());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_row);

        row.setTextViewText(R.id.appwidget_text, items.get(position));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            row.setContentDescription(R.id.appwidget_text, items.get(position));
        }


        Intent i = new Intent();
        Bundle extras = new Bundle();

        extras.putString(FootballScoresAppWidget.EXTRA_WORD, items.get(position));
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.appwidget_text, i);

        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}
