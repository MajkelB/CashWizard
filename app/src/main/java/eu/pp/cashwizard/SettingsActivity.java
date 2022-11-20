package eu.pp.cashwizard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.data.parameter.ParametersHelper;
import eu.pp.cashwizard.data.settlement.SettlementsHelper;
import eu.pp.cashwizard.dict.Action;
import eu.pp.cashwizard.dict.YesNo;
import eu.pp.cashwizard.model.Bill;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.BillsListAdapter;
import eu.pp.cashwizard.view.model.BillsListRow;

public class SettingsActivity extends AppCompatActivity  {

    //@BindView( R.id.lBillsList ) ListView lBillsList;
    @BindView( R.id.settingsTestModeSwitch ) Switch testModeSwitch;
    @BindView( R.id.settingsLoadOnStartupSwitch ) Switch loadOnStartupSwitch;
    @BindView( R.id.settingsSaveOnExitSwitch ) Switch saveOnExitSwitch;
    @BindView( R.id.settingsVersion ) TextView version;

    @BindView( R.id.bSettingsNew ) Button bNew;
    @BindView( R.id.bSettingsLoad ) Button bLoad;
    @BindView( R.id.bSettingsSave ) Button bSave;



    YesNo testModeL;
    YesNo loadOnStartupL;
    YesNo saveOnExitL;


    static final int PICK_CONTACT_REQUEST = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();

        View backgroundImage = findViewById(R.id.settingsBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);

        version.setText( Conf.getVersionString() );

        testModeL = Conf.testMode();
        testModeSwitch.setChecked( testModeL.isTrue() );
        testModeSwitch.setOnCheckedChangeListener((compoundButton, checked) -> testModeL = YesNo.decode( checked ));


        loadOnStartupL = YesNo.decode( Conf.getStringProperty( "data.loadOnStartup" ) );
        loadOnStartupSwitch.setChecked( loadOnStartupL.isTrue() );
        loadOnStartupSwitch.setOnCheckedChangeListener((compoundButton, checked) -> loadOnStartupL = YesNo.decode( checked ));

        saveOnExitL = YesNo.decode( Conf.getStringProperty( "data.saveOnClose" ) );
        saveOnExitSwitch.setChecked( saveOnExitL.isTrue() );
        saveOnExitSwitch.setOnCheckedChangeListener((compoundButton, checked) -> saveOnExitL = YesNo.decode( checked ));



    }

    @OnClick( R.id.bSettingsNew )
    public void settingsNew() {
        //DataRepository.clearSettlement();
//        Toast.makeText( this, "New: " + DataRepository.getSettlement().getId(), Toast.LENGTH_LONG).show();
        Parameter p = new Parameter( "test.param", "Ala ma kota" );
        Toast.makeText( this, "New: " + DataRepository.getSettlement().getId(), Toast.LENGTH_LONG).show();
    }
    @OnClick( R.id.bSettingsLoad )
    public void settingsLoad() {
        Toast.makeText( this, "***********", Toast.LENGTH_LONG).show();
        DataRepository.loadSettlement();
        Toast.makeText( this, "Load: " + DataRepository.getSettlement().getId(), Toast.LENGTH_LONG).show();
    }
    @OnClick( R.id.bSettingsSave )
    public void settingsSave() {
        Toast.makeText( this, "Ala ma kota33", Toast.LENGTH_LONG).show();
        AUtil.logD( "Save1: " + DataRepository.getSettlement().getId() );
//        DataRepository.saveSettlement();
//        SettlementsHelper.saveSettlementAsync( DataRepository.getSettlement() );
//        AUtil.logD( "Save2: " + DataRepository.getSettlement().getId() );
        try {
            ParametersHelper.saveParameterAsync(new Parameter("data.lastSettlementId", "" + DataRepository.getSettlement().getId()));
        } catch ( RuntimeException rte ) {
            AUtil.logD( "### ", rte );
        } catch ( Exception e ) {
            AUtil.logD( "^^ ", e );
        }
        Toast.makeText( this, "Save: " + DataRepository.getSettlement().getId(), Toast.LENGTH_LONG).show();
    }

    @OnClick( R.id.bSettingsOk )
    public void settingsOk() {

        AUtil.logD( "Clicked" );
        Conf.setStringProperty( "test.mode" , testModeL.getName() );
        Conf.setStringProperty( "data.loadOnStartup" , loadOnStartupL.getName() );
        Conf.setStringProperty( "data.saveOnClose" , saveOnExitL.getName() );
        SettingsActivity.this.finish();
    }
    @OnClick( R.id.bSettingsCancel )
    public void settingsCancel() {
        SettingsActivity.this.finish();
    }
}
