/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package local.nicolas.letsfan.auth;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import local.nicolas.letsfan.auth.ui.ExtraConstants;

/**
 * A container that encapsulates the result of authenticating with an Identity Provider.
 */
public class IdpResponse implements Parcelable {

    private final String mProviderId;
    @Nullable private final String mEmail;
    private final String mToken;
    private final String mSecret;

    public IdpResponse(String providerId, @Nullable String email) {
        this(providerId, email, null, null);
    }

    public IdpResponse(
            String providerId, @Nullable String email, @Nullable String token) {
        this(providerId, email, token, null);
    }

    public IdpResponse(
            String providerId,
            @Nullable String email,
            @Nullable String token,
            @Nullable String secret) {
        mProviderId = providerId;
        mEmail = email;
        mToken = token;
        mSecret = secret;
    }

    public static final Creator<IdpResponse> CREATOR = new Creator<IdpResponse>() {
        @Override
        public IdpResponse createFromParcel(Parcel in) {
            return new IdpResponse(
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString()
            );
        }

        @Override
        public IdpResponse[] newArray(int size) {
            return new IdpResponse[size];
        }
    };

    /**
     * Get the type of provider. e.g. {@link AuthUI#GOOGLE_PROVIDER}
     */
    public String getProviderType() {
        return mProviderId;
    }

    /**
     * Get the token received as a result of logging in with the specified IDP
     */
    @Nullable
    public String getIdpToken() {
        return mToken;
    }

    /**
     * Twitter only. Return the token secret received as a result of logging in with Twitter.
     */
    @Nullable
    public String getIdpSecret() {
        return mSecret;
    }

    /**
     * Get the email used to sign in.
     */
    @Nullable
    public String getEmail() {
        return mEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProviderId);
        dest.writeString(mEmail);
        dest.writeString(mToken);
        dest.writeString(mSecret);
    }

    /**
     * Extract the {@link IdpResponse} from the flow's result intent.
     *
     * @param resultIntent The intent which {@code onActivityResult} was called with.
     * @return The IdpResponse containing the token(s) from signing in with the Idp
     */
    @Nullable
    public static IdpResponse fromResultIntent(Intent resultIntent) {
        return resultIntent.getParcelableExtra(ExtraConstants.EXTRA_IDP_RESPONSE);
    }
}
