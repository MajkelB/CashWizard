package eu.pp.cashwizard.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import eu.pp.cashwizard.R;
import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.dict.AnswerType;
import eu.pp.cashwizard.dict.YesNo;
import eu.pp.cashwizard.view.adapters.other.DialogCallback;
import eu.pp.cashwizard.view.adapters.other.DialogQuestionCallback;
import eu.pp.cashwizard.view.adapters.other.PickDateCallback;
import eu.pp.cashwizard.view.adapters.other.ViewCaller;

public class AUtil {

    public static void logE( String comm ) {
        Log.e(Conf.TAG, comm );
    }
    public static void logE( String comm, Exception e ) {
        Log.e(Conf.TAG, comm, e );
    }

    public static void logI( String comm ) {
        Log.i(Conf.TAG, comm );
    }
    public static void logI( String comm, Exception e ) {
        Log.i(Conf.TAG, comm, e );
    }

    public static void logD( String comm ) {
        Log.d(Conf.TAG, comm );
    }
    public static void logD( String comm, Exception e ) {
        Log.d(Conf.TAG, comm, e );
    }

    public static void logV( String comm ) {
        Log.v(Conf.TAG, comm );
    }
    public static void logV( String comm, Exception e ) {
        Log.v(Conf.TAG, comm, e );
    }

    public static void logW( String comm ) {
        Log.w(Conf.TAG, comm );
    }
    public static void logW( String comm, Exception e ) {
        Log.w(Conf.TAG, comm, e );
    }

    public static void logWTF( String comm ) {
        Log.wtf(Conf.TAG, comm );
    }
    public static void logWTF( String comm, Exception e ) {
        Log.wtf(Conf.TAG, comm, e );
    }



    public static void showYesNoDialog(Activity activity, String title, String message, DialogQuestionCallback callback, String communicationId ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        Context context = activity.getApplicationContext();
        builder.setCancelable(false)
                .setIcon( R.drawable.no )
                .setTitle( title )
                .setMessage( message )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing!
                        if( callback != null ) callback.yesAnswer( communicationId );
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.yes ) )
                .setNegativeButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing!
                        if( callback != null ) callback.noAnswer( communicationId );
                    }
                })
                .setNegativeButtonIcon( context.getDrawable( R.drawable.no ) )
                .show();
    }

    public static void hideKeyBoard(View v, final Activity activity, boolean hasFocus)
    {
        if( hasFocus ) {
            logI( "Hide" );
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0 );
        }
//        else {
            logI( "No focus" );

//        }
    }

    public static void showDateCalendar( Activity activity, PickDateCallback callback, ViewCaller viewCaller ) {
        final Calendar myCal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                callback.pickDate( myCal.getTime(), viewCaller);
            }

        };

        new DatePickerDialog(activity, date, myCal
                .get(Calendar.YEAR), myCal.get(Calendar.MONTH),
                myCal.get(Calendar.DAY_OF_MONTH)).show();

    }

    public static boolean writePhoto( Activity activity, String fileName, Bitmap bitmap) {
        FileOutputStream outputStream;
        Context context = activity.getApplicationContext();
        try {
            //outputStream = context.openFileOutput( fileName, Context.MODE_PRIVATE );
            File f = new File( fileName );
            outputStream = new FileOutputStream( f );
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            logI( "writePhoto ( " + bitmap.getWidth() + "/" + bitmap.getHeight() + " ): " + fileName );
            outputStream.flush();
            outputStream.close();

            ExifInterface newExif = new ExifInterface( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/test.jpg" );
            newExif.setAttribute( ExifInterface.TAG_IMAGE_WIDTH, String.valueOf(222) );
            newExif.setAttribute( ExifInterface.TAG_PIXEL_X_DIMENSION, String.valueOf(155) );
            newExif.setAttribute( ExifInterface.TAG_IMAGE_DESCRIPTION, "Pikna fota" );
            newExif.setAttribute( ExifInterface.TAG_ARTIST, "To on" );
//            newExif.saveAttributes();
//            newExif.saveAttributes();

//            String fAbsolutePath = f.getAbsolutePath();
            String fAbsolutePath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/aaa.jpg";
            f = null;
            System.gc();
            return saveFileAttributes(fAbsolutePath, bitmap.getWidth(), bitmap.getHeight() );
        } catch (Exception e) {
            logE( "Write photo error for file: " + fileName + " * ", e );
            return false;
        }
    }



    public static File getFileFromName( String fileName ) {
        if( fileName == null ) return null;
        return new File( fileName );
    }


    public static Bitmap getBitmapFromName( String fileName ) {
        if( fileName == null ) return null;
        return BitmapFactory.decodeFile( fileName );
    }


    public static int px2DP( Context context, int val ) {
        float dpCalculation = context.getResources().getDisplayMetrics().density;
        return (int) (val * dpCalculation);
    }

//    Nie działa mi ta funkcja - zostawiam na razie bo nie jest zbyt istotne
    public static boolean saveFileAttributes(String absPath, int x, int y ) {
        logI( "Setting metadata attributes for file: " + absPath + " * x=" + x + " , y=" + y );
        try {
            ExifInterface newExif = new ExifInterface( absPath );
            newExif.setAttribute( ExifInterface.TAG_IMAGE_WIDTH, String.valueOf(222) );
            newExif.setAttribute( ExifInterface.TAG_PIXEL_X_DIMENSION, String.valueOf(155) );
            newExif.setAttribute( ExifInterface.TAG_IMAGE_DESCRIPTION, "Pikna fota" );
            newExif.setAttribute( ExifInterface.TAG_ARTIST, "To on" );
            newExif.saveAttributes();

            logI( "Attributes set" );
            return  true;
        } catch (IOException e) {
            logE( "Save file attributes IO error for file: " + absPath + " * ", e );
            return false;
        } catch (Exception e ) {
            logE( "Save file attributes unknown error for file: " + absPath + " * ", e );
            return false;
        }
    }


    public static void showInfo(Context context, String title, String message, YesNo close ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setTitle( title )
                .setMessage( message )
                .setIcon( R.drawable.info5 )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing!
                        if( close.isTrue() ) ((Activity)context).finish();
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .show();
    }

    public static void showInfo(Context context, String title, String message, DialogCallback callback, int communicationId ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setTitle( title )
                .setMessage( message )
                .setIcon( R.drawable.info5 )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( callback != null ) callback.answer( communicationId, AnswerType.OK );
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .show();
    }

    public static void showConfirmation( Context context, String title, String message, YesNo close ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setTitle( title )
                .setMessage( message )
                .setIcon( R.drawable.ok2 )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( close.isTrue() ) ((Activity)context).finish();
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .show();
    }

    public static void showConfirmation(Context context, String title, String message, DialogCallback callback, int communicationId ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setTitle( title )
                .setMessage( message )
                .setIcon( R.drawable.ok2 )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( callback != null ) callback.answer( communicationId, AnswerType.OK );
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .show();
    }

    public static void showError( Context context, String title, String message, YesNo close ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setTitle( title )
                .setMessage( message )
                //.setIconAttribute( android.R.attr.alertDialogIcon )
                .setIcon( R.drawable.cancel2 )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( close.isTrue() ) ((Activity)context).finish();

                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .show();
    }

    public static void showError(Context context, String title, String message, DialogCallback callback, int communicationId ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setTitle( title )
                .setMessage( message )
                //.setIconAttribute( android.R.attr.alertDialogIcon )
                .setIcon( R.drawable.cancel2 )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( callback != null ) callback.answer( communicationId, AnswerType.OK );
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .show();
    }

    public static void showYesNoDialog(Context context, String title, String message, DialogCallback callback, int communicationId ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setCancelable(false)
                .setIcon( R.drawable.question )
                .setTitle( title )
                .setMessage( message )
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing!
                        if( callback != null ) callback.answer( communicationId, AnswerType.YES );
                    }
                })
                .setPositiveButtonIcon( context.getDrawable( R.drawable.ok2 ) )
                .setNegativeButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing!
                        if( callback != null ) callback.answer( communicationId, AnswerType.NO );
                    }
                })
                .setNegativeButtonIcon( context.getDrawable( R.drawable.cancel2 ) )
                .show();
    }

}
