package local.nicolas.letsfan;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dell on 2017/1/7.
 */

public class Invitation_Dialog extends DialogFragment {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter <InvitationAttendees,mViewHolder> mFirebaseAdapter;
    public static class mViewHolder extends RecyclerView.ViewHolder {
        public TextView attendeeNickNameTextView;
        //public ImageView attendeeImageView;
        public mViewHolder(View v) {
            super(v);
            attendeeNickNameTextView = (TextView) itemView.findViewById(R.id.attendeeNickName);
            //attendeeImageView = (ImageView) itemView.findViewById(R.id.invitation_image);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        String pushId = mBundle.getString("pushId");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<InvitationAttendees,mViewHolder>(
                InvitationAttendees.class,
                R.layout.invitation_dialog_item,
                mViewHolder.class,
                FirebaseDatabase.getInstance().getReference("invitationAttendees").child(pushId)
        )
        {
            @Override
            protected void populateViewHolder(mViewHolder viewHolder, InvitationAttendees invitation, final int position) {
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.attendeeNickNameTextView.setText(invitation.getNickName());

            }
        };
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.invitation_dialog, null));

        return builder.create();
    }


}