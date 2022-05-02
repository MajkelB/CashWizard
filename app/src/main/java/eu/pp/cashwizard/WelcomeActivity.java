package eu.pp.cashwizard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.data.DBClient;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.data.parameter.ParametersHelper;
import eu.pp.cashwizard.data.settlement.SettlementsHelper;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.util.AUtil;

public class WelcomeActivity extends AppCompatActivity {

    @BindView( R.id.welcomeVersion ) TextView version;

    private static final boolean AUTO_HIDE = true;

    static Intent intent;

    private static final int BAR_TIME = 3000;
    private static final int BAR_TEST_TIME = 500;
    private static final int BAR_STEP = 500;
    private static final int BAR_RAND = 16;

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind( this );
//        getSupportActionBar().hide();
        pb = (ProgressBar) findViewById(R.id.welcomeProgressBar);
        pb.setProgressDrawable( getDrawable( R.drawable.progress_bar ) );

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        AUtil.logI("Width" + width);
        AUtil.logI("Height" + height);
//        DBClient.init( WelcomeActivity.this );
        ParametersHelper.init( WelcomeActivity.this );
        ActivityStarter as = new ActivityStarter();
        as.start();
    }

    private class ActivityStarter extends Thread {

        @Override
        public void run() {

            Conf.loadConf( getApplicationContext() );
            Conf.readLocalConfiguration();
            Conf.print2LogConfiguration();
            Conf.setVersion( getApplicationContext() );
            ParametersHelper.saveParameter( new Parameter( "data.lastSettlementId" , "" + DataRepository.getSettlement().getId() ) );
            DataRepository.loadSettlement();
            version.setText( Conf.getFullVersionString() );
            int l = 0;
            Random rand = new Random( new Date().getTime() );
            pb.setProgress( 0 );
            try {
                // tutaj wrzucamy wszystkie akcje potrzebne podczas ładowania aplikacji
                // Thread.sleep(BAR_TIME);

                for(int i = 0; i<= (Conf.testMode().isTrue()?BAR_TEST_TIME:BAR_TIME); i+= BAR_STEP) {
                    Thread.sleep(BAR_STEP);
                    l = rand.nextInt(BAR_RAND);
                    pb.setProgress( (int) ( (100*i)/ BAR_TIME) + l - 10  );
                    Log.i( "I", "" + i + " / " + BAR_TIME + " * " + ( (int) ( (100*i)/ BAR_TIME) + l - 10 )  + "%" );
                }


            } catch (Exception e) {
                //Log.e("SplashScreen", e.getMessage());
            }

            // Włącz główną aktywność
            WelcomeActivity.this.finish();
            startActivity( getMainActivity() );
        }
    }

    private Intent getMainActivity(){
        if( intent == null ) {
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return intent;
    }
}
