package net.loizbak.sonak.fischerclick;

import android.os.AsyncTask;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init tempo picker
        final NumberPicker tempoPicker = ( NumberPicker ) findViewById( R.id.tempoPicker );
        tempoPicker.setMinValue( 20 );
        tempoPicker.setMaxValue(280);
        tempoPicker.setValue(85);

        // get beam click
        final View beamClick = findViewById( R.id.beamClick );

        Timer timer = null;
        beamClick.setVisibility( View.INVISIBLE );

        FloatingActionButton startAction = (FloatingActionButton) findViewById( R.id.start );
        startAction.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        startTimer( beamClick, tempoPicker.getValue() );
                    }
                }
        );

        FloatingActionButton stopAction = (FloatingActionButton) findViewById( R.id.stop );
        stopAction.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        stopTimer(beamClick);

                    }
                }
        );

    }

    @Override
    protected void onStop() {

        // stop timer
        stopTimer( null );

        super.onStop();

    }

    @Override
    protected void onDestroy() {

        // stop timer
        stopTimer( null );

        super.onDestroy();

    }

    private void startTimer( View beamClick, int tempo ) {

        if ( timer != null ) {
            stopTimer( beamClick );
        }

        timer = new Timer( "FisherClick", true );
        ClickTimerTask ctt = new ClickTimerTask( beamClick );

        // convert tempo to ms
        float fPeriodMs = 60 * 1000f / ( float ) tempo;

        int periodMs = Math.round( fPeriodMs );
        System.out.println( "tempo = " + tempo + ", ms = " + periodMs );

        timer.scheduleAtFixedRate(ctt, 0, periodMs);

    }

    private void stopTimer( View beamClick ) {

        if ( timer != null ) {
            timer.cancel();
        }

        timer = null;

        if ( beamClick != null ) {
            beamClick.setVisibility( View.INVISIBLE );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ClickTimerTask extends TimerTask {


        private final View beamClick;

        public ClickTimerTask(View beamClick) {
            super();
            this.beamClick = beamClick;
        }

        @Override
        public void run() {

            System.out.println( "Click!" );
            runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {
                        toggleVisibility(beamClick);
                    }
                }
            );

        }

    }

    private void toggleVisibility( View view ) {

        // get visibility
        int nextVisibility = 0;
        int curVisibility = view.getVisibility();

        switch ( curVisibility ) {

            case View.VISIBLE: {

                // hide
                nextVisibility = View.INVISIBLE;
                break;
            }

            case View.INVISIBLE: {

                // hide
                nextVisibility = View.VISIBLE;
                break;
            }

            default: {
                nextVisibility = View.VISIBLE;

            }
        }

        view.setVisibility( nextVisibility );

    }

}
