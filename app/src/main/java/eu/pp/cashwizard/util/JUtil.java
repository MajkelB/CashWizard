package eu.pp.cashwizard.util;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import eu.pp.cashwizard.configuration.Conf;

public class JUtil {

    private static final String DATE_SIMPLE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_SIMPLE_PL_PATTERN = "dd-MM-yyyy";
    private static final String DATE_FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_SIMPLE_PATTERN = "HH:mm:ss";
    private static final String TIME_WITH_MILISECONDS_PATTERN = "HH:mm:ss:SSS";
    private static final String PHOTO_FILE_DATE_PATTERN = "yyyyMMddHHmmss";

    private static final String CURRENCY_FORMAT_MASK = "#,##0.00";
    private static final char CURRENCY_DECIMAL_SEPARATOR = '.';
    private static final char CURRENCY_GROUPING_SEPARATOR = ' ';

//    private static final String NEW_LINE = "\n\r";
    private static final String NEW_LINE = System.getProperty("line.separator");

    public static String getNewLine() {
        return NEW_LINE;
    }

    public static Date getDate( String data ) {
        return getDate( data, DATE_SIMPLE_PATTERN );
    }
    public static Date getDate(String data, String datePattern ) {
        SimpleDateFormat sdf = new SimpleDateFormat( (datePattern==null?DATE_SIMPLE_PATTERN:datePattern) );
        try {
            return sdf.parse(data);
        } catch( NullPointerException npe ) {
            Log.e( Conf.TAG, "Bład parsowania daty - przekazana pusta data" );
        } catch (ParseException e) {
            Log.e( Conf.TAG, "Bład parsowania daty", e );
        }
        return null;
    }

    public static String getDate(Date data) {
        if( data == null ) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_SIMPLE_PATTERN);
        return sdf.format(data);
    }

    public static Date getTimeWithMiliseconds(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_WITH_MILISECONDS_PATTERN);
        try {
            return sdf.parse(data);
        } catch (NullPointerException npe) {
            Log.e( Conf.TAG, "Bład parsowania daty - przekazana pusta data");
        } catch (ParseException e) {
            Log.e( Conf.TAG, "Bład parsowania daty", e);
        }
        return null;
    }

    public static String getTimeWithMiliseconds(Date data) {
        if (data == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_WITH_MILISECONDS_PATTERN);
        return sdf.format(data);
    }

    public static Date GetCurrentDate() {
        return trimDate( now() );
    }

    public static String GetCurrentStringDate() {
        return getDate( new Date() );
    }

    public static String GetCurrentStringTimeWithMiliseconds() {
        return getTimeWithMiliseconds( new Date() );
    }

    public static Date now() {
        return new Date();
    }

    public static String getPhotoFileNameUI() {
        SimpleDateFormat sdf = new SimpleDateFormat(PHOTO_FILE_DATE_PATTERN);
        return sdf.format( now() );
    }


    public static Date trimDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    //@SuppressWarnings("unchecked")
    public static <T> List<T> safeList(List<T> other ) {
        return other == null ? Collections.<T>emptyList() : other;
    }

    //@SuppressWarnings("unchecked")
    public static <T> Set<T> safeSet(Set<T> other) {
        return other == null ? (Collections.<T>emptySet()) : other;
    }

    public static <T> boolean add2List(List<T> firstList, List<T> secondList) {
        if (firstList == null) {
            return false;
        }
        if (secondList == null) {
            return true;
        }
        for (T t : safeList(secondList)) {
            firstList.add(t);
        }

        return true;
    }

    public static boolean isWithin(Date date, Date from, Date to) {
        if (date == null) {
            return false;
        }
        if (from == null && to == null) {
            return true;
        }
        if (!date.before(from) && !date.after(to)) {
            return true;
        } else {
            return false;
        }
    }

    public static String format(BigDecimal bd) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator( CURRENCY_DECIMAL_SEPARATOR );
        decimalFormatSymbols.setGroupingSeparator( CURRENCY_GROUPING_SEPARATOR );
        return new DecimalFormat(CURRENCY_FORMAT_MASK, decimalFormatSymbols).format( bd.setScale( 10, BigDecimal.ROUND_HALF_UP) );
    }

    public static BigDecimal string2BigDecimal( String str ) {
        BigDecimal b;
        try {
            b = BigDecimal.valueOf( Long.parseLong( str ) );
        }catch ( Exception e ) {
            b = BigDecimal.ZERO;
        }
        return b;
    }

    public static String prepareString( String input, Integer numberOfLines, Integer lineLength ) {
        if( input == null ) return "";
        StringBuilder strBld = new StringBuilder();
        int j=0,k=0,l=0;
        if( numberOfLines == null ) numberOfLines = Integer.MAX_VALUE;
        while( j <= input.length() && l++<numberOfLines ) {
            j=k;
            if( j!=0) j+=NEW_LINE.length();
            k = input.indexOf( NEW_LINE, j+1 );
            if( k == -1 ) k=input.length();
            if( j<k) strBld.append( input.substring( j, (lineLength==null?k:j+Math.min( k-j,lineLength ) ) ) ).append( NEW_LINE );
        }
        return strBld.toString();
    }

}
