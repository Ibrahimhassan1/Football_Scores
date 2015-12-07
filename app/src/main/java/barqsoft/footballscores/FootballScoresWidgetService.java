package barqsoft.footballscores;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ibrahimhassan on 9/15/15.
 */
public class FootballScoresWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }


}