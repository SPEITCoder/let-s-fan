package local.nicolas.letsfan;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

/**
 * Created by soshy on 07/01/2017.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int hour;
    private int minute;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        hour = getArguments().getInt("hour");
        minute = getArguments().getInt("minute");
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        int mid = getArguments().getInt("id");
        TextTime mTextTime = (TextTime) getActivity().findViewById(mid);
        mTextTime.setTime(hourOfDay, minute);
    }
}
