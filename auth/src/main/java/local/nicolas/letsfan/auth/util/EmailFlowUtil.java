package local.nicolas.letsfan.auth.util;

import android.content.Context;
import android.content.Intent;

import local.nicolas.letsfan.auth.ui.FlowParameters;
import local.nicolas.letsfan.auth.ui.email.EmailHintContainerActivity;
import local.nicolas.letsfan.auth.ui.email.SignInNoPasswordActivity;

/**
 * Helper class to kick off the Email/Password sign-in flow.
 */
public class EmailFlowUtil {

    /**
     * Return an intent for either {@link EmailHintContainerActivity} or
     * {@link SignInNoPasswordActivity} depending on if SmartLock is enabled.
     */
    public static Intent createIntent(Context context, FlowParameters parameters) {
        if (parameters.smartLockEnabled) {
            return EmailHintContainerActivity.createIntent(context, parameters);
        } else {
            return SignInNoPasswordActivity.createIntent(context, parameters, null);
        }
    }

}
