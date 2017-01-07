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
import java.util.Locale;

public class TextTime extends TextView {

    private static final CharSequence DEFAULT_FORMAT_12_HOUR = "h:mm a";
    private static final CharSequence DEFAULT_FORMAT_24_HOUR = "H:mm";

    private CharSequence mFormat12;
    private CharSequence mFormat24;
    private CharSequence mFormat;

    private boolean mAttached;

    private int mHour;
    private int mMinute;

    private final ContentObserver mFormatChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            chooseFormat();
            updateTime();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            chooseFormat();
            updateTime();
        }
    };

    @SuppressWarnings("UnusedDeclaration")
    public TextTime(Context context) {
        this(context, null);
    }

    @SuppressWarnings("UnusedDeclaration")
    public TextTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTime(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFormat12Hour(get12ModeFormat(0.22f /* amPmRatio */));
        setFormat24Hour(get24ModeFormat());

        chooseFormat();
    }

    @SuppressWarnings("UnusedDeclaration")
    public CharSequence getFormat12Hour() {
        return mFormat12;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setFormat12Hour(CharSequence format) {
        mFormat12 = format;

        chooseFormat();
        updateTime();
    }

    @SuppressWarnings("UnusedDeclaration")
    public CharSequence getFormat24Hour() {
        return mFormat24;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setFormat24Hour(CharSequence format) {
        mFormat24 = format;

        chooseFormat();
        updateTime();
    }

    private void chooseFormat() {
        final boolean format24Requested = DateFormat.is24HourFormat(getContext());
        if (format24Requested) {
            mFormat = mFormat24 == null ? DEFAULT_FORMAT_24_HOUR : mFormat24;
        } else {
            mFormat = mFormat12 == null ? DEFAULT_FORMAT_12_HOUR : mFormat12;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            registerObserver();
            updateTime();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            unregisterObserver();
            mAttached = false;
        }
    }

    private void registerObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
    }

    private void unregisterObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.unregisterContentObserver(mFormatChangeObserver);
    }

    public void setTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
        updateTime();
    }

    private void updateTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        final CharSequence text = DateFormat.format(mFormat, calendar);
        setText(text);
        // Strip away the spans from text so talkback is not confused
        setContentDescription(text.toString());
    }

    public static CharSequence get12ModeFormat(float amPmRatio) {
        String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "hma");
        if (amPmRatio <= 0) {
            pattern = pattern.replaceAll("a", "").trim();
        }

        // Replace spaces with "Hair Space"
        pattern = pattern.replaceAll(" ", "\u200A");
        // Build a spannable so that the am/pm will be formatted
        int amPmPos = pattern.indexOf('a');
        if (amPmPos == -1) {
            return pattern;
        }

        final Spannable sp = new SpannableString(pattern);
        sp.setSpan(new RelativeSizeSpan(amPmRatio), amPmPos, amPmPos + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(Typeface.NORMAL), amPmPos, amPmPos + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new TypefaceSpan("sans-serif"), amPmPos, amPmPos + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sp;
    }

    public static CharSequence get24ModeFormat() {
        return DateFormat.getBestDateTimePattern(Locale.getDefault(), "Hm");
    }

    public int getHour() {return mHour;}
    public int getMinute() {return mMinute;}
}