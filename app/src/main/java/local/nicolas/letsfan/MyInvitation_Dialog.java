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

import java.util.ArrayList;

/**
 * Created by dell on 2017/1/7.
 */

public class MyInvitation_Dialog extends DialogFragment {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter <InvitationAttendees,mViewHolder> mFirebaseAdapter;

    private ImageView mImageView;
    private TextView mRestaurantName;
    private TextTime mStartTime;
    private TextTime mEndTime;
    private TextDate mEventDate;

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
        final User user = (User) getArguments().getSerializable("currentUser");
        final Invitation invitation = (Invitation) getArguments().getSerializable("invitation");
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.invitation_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final ArrayList<Integer> AvatarList = new ArrayList<>();
        AvatarList.add(R.drawable.corgia);
        AvatarList.add(R.drawable.dog);
        AvatarList.add(R.drawable.dribbble_corgi);
        AvatarList.add(R.drawable.fox_dribbble);
        AvatarList.add(R.drawable.notacorgi);
        AvatarList.add(R.drawable.paco);
        AvatarList.add(R.drawable.puppy);
        AvatarList.add(R.drawable.schnauzer);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MyInvitation_Dialog.this.getDialog().cancel();
                    }
                });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.attendee_recycler);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<InvitationAttendees, mViewHolder>(
                InvitationAttendees.class,
                R.layout.invitation_dialog_item,
                mViewHolder.class,
                FirebaseDatabase.getInstance().getReference("invitationAttendees").child(pushId)
        ){
            int i = 0;
            @Override
            protected void populateViewHolder(mViewHolder viewHolder, InvitationAttendees invitation, final int position) {
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.attendeeNickNameTextView.setText(invitation.getNickName());
                viewHolder.attendeeStartTextTime.setTime(invitation.getStartTimeHour().intValue(), invitation.getStartTimeMinute().intValue());
                viewHolder.attendeeEndTextTime.setTime(invitation.getEndTimeHour().intValue(), invitation.getEndTimeMinute().intValue());
                ((ImageView) viewHolder.itemView.findViewById(R.id.attendee_image)).setImageResource(AvatarList.get(i++ % AvatarList.size()));
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


        mImageView = (ImageView) v.findViewById(R.id.restaurant_image_opened);
        mRestaurantName = (TextView) v.findViewById(R.id.invitation_restaurant_opened);
        mStartTime = (TextTime) v.findViewById(R.id.invitation_start_opened);
        mEndTime = (TextTime) v.findViewById(R.id.invitation_end_opened);
        mEventDate = (TextDate) v.findViewById(R.id.invitation_date_opened);

        if(invitation.getRestaurantName().equals("蛤乐餐厅")) {mImageView.setImageResource(R.drawable.canteen_1);}
        else if(invitation.getRestaurantName().equals("第一餐饮大楼一层")){mImageView.setImageResource(R.drawable.canteen_2);}
        else if(invitation.getRestaurantName().equals("第一餐饮大楼二层")){mImageView.setImageResource(R.drawable.canteen_3);}
        mRestaurantName.setText(invitation.getRestaurantName());
        mStartTime.setTime(invitation.getStartTimeHour().intValue(), invitation.getStartTimeMinute().intValue());
        mEndTime.setTime(invitation.getEndTimeHour().intValue(), invitation.getEndTimeMinute().intValue());
        mEventDate.setDate(invitation.getDateYear().intValue(), invitation.getDateMonth().intValue(), invitation.getDateDay().intValue());

        return builder.create();
    }




}