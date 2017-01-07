package local.nicolas.letsfan;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dell on 2017/1/7.
 */

public class EventDialog extends DialogFragment {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter <InvitationAttendees,mViewHolder> mFirebaseAdapter;

    public static class mViewHolder extends RecyclerView.ViewHolder {
        public TextView attendeeNickNameTextView;
        public ImageView attendeeImageView;
        public TextTime attendeeStartTextTime;
        public TextTime attendeeEndTextTime;

        public mViewHolder(View v) {
            super(v);
            attendeeNickNameTextView = (TextView) itemView.findViewById(R.id.attendeeNickName);
            attendeeImageView = (ImageView) itemView.findViewById(R.id.attendee_image);
            attendeeStartTextTime = (TextTime) itemView.findViewById(R.id.attendee_start);
            attendeeEndTextTime = (TextTime) itemView.findViewById(R.id.attendee_end);
         }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle mBundle = getArguments();
        final String pushId = mBundle.getString("pushId");
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.invitation_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO close the dialog
                        EventDialog.this.getDialog().cancel();
                    }
                });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.attendee_recycler);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<InvitationAttendees, mViewHolder>(
                InvitationAttendees.class,
                R.layout.invitation_dialog_item,
                mViewHolder.class,
                FirebaseDatabase.getInstance().getReference("invitationAttendees").child(pushId)
        ){
            @Override
            protected void populateViewHolder(mViewHolder viewHolder, InvitationAttendees invitation, final int position) {
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.attendeeNickNameTextView.setText(invitation.getNickName());
                viewHolder.attendeeStartTextTime.setTime(invitation.getStartTimeHour().intValue(), invitation.getStartTimeMinute().intValue());
                viewHolder.attendeeEndTextTime.setTime(invitation.getEndTimeHour().intValue(), invitation.getEndTimeMinute().intValue());
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int invitationCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (invitationCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);

        return builder.create();
    }




}