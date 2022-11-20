package eu.pp.cashwizard.configuration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import eu.pp.cashwizard.R;
import eu.pp.cashwizard.data.parameter.ParametersHelper;
import eu.pp.cashwizard.dict.YesNo;
import eu.pp.cashwizard.model.Parameter;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;

public class Conf {


    private static Properties p = new Properties();

    private static final String CONF_FILE_NAME = "cashWizard.properties";
    public static final String DB_FILE_NAME = "cashWizardData.db";

    private static Context mContext;

    public static final String TAG = "CASHWIZARD";
    public static String APP_VERSION = "0.0.9";
    public static int APP_VERSION_CODE = 1;
    private static YesNo newerVersionExists = null;

    public static String DIRECTORY_SMALL_PICTURES = "Pictures33";

    YesNo testMode = YesNo.N;

    public static boolean loadConf( Context context ) {
        mContext = context;
        DIRECTORY_SMALL_PICTURES = (mContext==null?null:mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(CONF_FILE_NAME);
            p.load(inputStream);
            return true;
        } catch (Exception e) {
            AUtil.logE( Conf.getString(R.string.ex_config_load_error), e);
        }


        return false;
    }

    // Properties

    public static String getStringProperty( String name ) {
        return p.getProperty(  name, Conf.getString( R.string.ex_property_not_found ) + ": " + name );
    }

    public static String getStringProperty( String name, String value ) {
        return p.getProperty( name, value );
    }

    public static String getNullableStringProperty( String name ) {
        return p.getProperty( name );
    }

    public static void setStringProperty( String name, String value ) {
        p.setProperty( name, value );
    }

    public static Date getDateProperty(String name ) {
        try {
            return JUtil.getDate( p.getProperty(name) );
        } catch ( Exception e ) {
            Log.e( Conf.TAG, Conf.getString( R.string.ex_property_not_found ) + ": " + name, e );
        }
        return null;
    }

    public static void setDateProperty( String name, Date value ) {
        p.setProperty( name, JUtil.getDate( value ) );
    }

    public static Integer getIntegerProperty( String name ) {
        Integer res;
        try {
            res = Integer.parseInt( getStringProperty( name ) );
        } catch ( NumberFormatException nfe ) {
            Log.e( TAG, getString( R.string.ex_property_parsing_error ) + ": " + name + " * " + getStringProperty( name ), nfe );
            res = null;
        } catch ( Exception e ) {
            Log.e( TAG, getString( R.string.ex_unknown) + ": " + name + " * " + getStringProperty( name ), e );
            res = null;
        }
        return res;
    }

    public static Long getLongProperty( String name ) {
        Long res;
        try {
            res = Long.parseLong( getStringProperty( name ) );
        } catch ( NumberFormatException nfe ) {
            Log.e( TAG, getString( R.string.ex_property_parsing_error ) + ": " + name + " * " + getStringProperty( name ), nfe );
            res = null;
        } catch ( Exception e ) {
            Log.e( TAG, getString( R.string.ex_unknown) + ": " + name + " * " + getStringProperty( name ), e );
            res = null;
        }
        return res;
    }
//    public static Period sessionInactiveTimeout() {
//        Optional<Period> prop = Optional.ofNullable( Period.decodePeriod( p.getProperty( "session.inactive.timeout" ) ) );
//        return prop.orElse( Period.decodePeriod( SESSION_INACTIVE_TIMEOUT ) );
//    }
//
//    public static Period multipleScannigLatency() {
//        Optional<Period> prop = Optional.ofNullable( Period.decodePeriod( p.getProperty( "scanning.multiple.latency" ) ) );
//        return prop.orElse( Period.decodePeriod( MULTIPLE_SCANNING_LATENCY ) );
//    }
//
//    public static Period scannigReadDelay() {
//        Optional<Period> prop = Optional.ofNullable( Period.decodePeriod( p.getProperty( "scanning.read.delay" ) ) );
//        return prop.orElse( Period.decodePeriod( SCANNING_READ_DELAY ) );
//    }
//
//    public static Period connectionCheckTimeout() {
//        Optional<Period> prop = Optional.ofNullable( Period.decodePeriod( p.getProperty( "connection.check.timeout" ) ) );
//        return prop.orElse( Period.decodePeriod( CONNECTION_CHECK_TIMEOUT ) );
//    }
//
//    public static Period syncDataValidity() {
//        Optional<Period> prop = Optional.ofNullable( Period.decodePeriod( p.getProperty( "sync.data.validity" ) ) );
//        return prop.orElse( Period.decodePeriod( SYNC_DATA_VALIDITY ) );
//    }

    // Values

    public static String getString( int id ) {
        return mContext.getResources().getString( id );
    }


    public static YesNo testMode() {
        if( getStringProperty( "test.mode" ).equals("Y") ) return YesNo.Y;
        else return YesNo.N;
    }

    public static String getPhotoDirectory() {
        Optional<String> res = Optional.ofNullable( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "" ); // + "/" + getStringProperty("photo.directory") );
        return res.orElse("");
    }


    public static YesNo newerVersionExists() {
        return newerVersionExists;
    }

    public static void setNewerVersionExists(YesNo newerVersion) {
        Conf.newerVersionExists = newerVersion;
    }

    public static String setVersion( Context context ) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo( context.getPackageName(), 0 );
            Conf.APP_VERSION = pInfo.versionName;
            Conf.APP_VERSION_CODE = pInfo.versionCode;
            return Conf.APP_VERSION;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    public static String getVersionString() {
        return Conf.APP_VERSION + " (" + getString( R.string.welcome_appVersionCode_label ) + ": " + Conf.APP_VERSION_CODE + ")";
    }

    public static String getFullVersionString() {
        return getString( R.string.welcome_appVersion_label ) + ": " + getVersionString();
    }

    public static void print2LogConfiguration() {
        Enumeration<?> eNames = p.propertyNames();
        String pName = null;
        if( eNames != null && eNames.hasMoreElements() ) {
            AUtil.logI( " **************************** Configuration ****************************" );
            while (eNames.hasMoreElements()) {
                pName = (String) eNames.nextElement();
                AUtil.logI(pName + " = " + p.getProperty(pName));
            }
            AUtil.logI( " **************************** *********** ****************************" );
        }
    }

    public static void readLocalConfiguration() {
        AUtil.logI( " ************************* Local Configuration **********************" );
        for( Parameter par: JUtil.safeList( ParametersHelper.getAllParameters() ) ) {
            p.setProperty( par.getName(), par.getValue() );
            AUtil.logI(par.getName() + " = " + par.getValue() );
        }
        AUtil.logI( " **************************** *********** ****************************" );

    }





}
