package local.nicolas.letsfan;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dell on 2017/1/5.
 */

public class RecyclerAdapter extends Adapter<RecyclerAdapter.ViewHolder> {

    private String[] mDataset;

    /**
     * @Description: TODO
     */
    public RecyclerAdapter(String[] dataset) {
        mDataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        /**
         * @Description: TODO
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }

    /**
     * @Method: getItemCount
     * @Description: TODO
     * @return
     * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    /**
     * @Method: onBindViewHolder
     * @Description: TODO
     * @param holder
     * @param position
     * @see android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder,
     *      int)
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    /**
     * @Method: onCreateViewHolder
     * @Description: TODO
     * @param parent
     * @param viewType
     * @return
     * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup,
     *      int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),
                android.R.layout.simple_list_item_1, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
}

