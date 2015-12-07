package barqsoft.footballscores;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class DetailViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        getSupportActionBar().hide();

        String word=getIntent().getStringExtra(FootballScoresAppWidget.EXTRA_WORD);

        if (word==null) {
            word="We did not get a word!";
        }

//        Toast.makeText(this, word, Toast.LENGTH_LONG).show();

        TextView tv = (TextView)findViewById(R.id.detail_text);
        tv.setText(word);
    }


}
