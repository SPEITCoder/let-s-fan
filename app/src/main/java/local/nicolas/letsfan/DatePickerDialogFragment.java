package local.nicolas.letsfan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Created by soshy on 07/01/2017.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int year;
    private int month;
    private int date;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        year = getArguments().getInt("year");
        month = getArguments().getInt("month") - 1;
        date = getArguments().getInt("date");
        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, date);
    }

    public void onDateSet(DatePicker view, int year, int month, int date) {
        // Do something with the time chosen by the user
        int mid = getArguments().getInt("id");
        TextDate mTextDate = (TextDate) getActivity().findViewById(mid);
        mTextDate.setDate(year, month + 1, date);
    }
}
