package local.nicolas.letsfan;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TextDate extends TextView {

    private CharSequence mFormat = "EEE, yyyy-MM-dd";

    private boolean mAttached;

    private int mYear;
    private int mMonth;
    private int mDay;


    @SuppressWarnings("UnusedDeclaration")
    public TextDate(Context context) {
        this(context, null);
    }

    @SuppressWarnings("UnusedDeclaration")
    public TextDate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextDate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            updateDate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            mAttached = false;
        }
    }

    public void setDate(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        updateDate();
    }

    public void setDate(Date date) {
        mYear = date.getYear() + 1900;
        mMonth = date.getMonth() + 1;
        mDay = date.getDate();
    }

    private void updateDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth - 1, mDay);

        final CharSequence text = DateFormat.format(mFormat, calendar);
        setText(text);
        // Strip away the spans from text so talkback is not confused
        setContentDescription(text.toString());
    }

    public int getYear() {return mYear;}
    public int getMonth() {return mMonth;}
    public int getDayOfMonth() {return mDay;}

}