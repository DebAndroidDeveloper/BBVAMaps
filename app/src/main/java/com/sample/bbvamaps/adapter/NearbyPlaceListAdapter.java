package com.sample.bbvamaps.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sample.bbvamaps.R;
import com.sample.bbvamaps.model.PlaceDetails;
import com.sample.bbvamaps.util.BBVAMapsLog;

import java.util.List;

public class NearbyPlaceListAdapter extends RecyclerView.Adapter<NearbyPlaceListAdapter.ViewHolder>{
    private static final String TAG = NearbyPlaceListAdapter.class.getCanonicalName();

    private Context mContext;
    private List<PlaceDetails> mPlaceList;
    private Listener mListener;

    public interface Listener {
        void onRowItemClicked(@NonNull PlaceDetails placeDetails);
    }

    public NearbyPlaceListAdapter(@NonNull Context context, @NonNull List<PlaceDetails> placeDetailsList,
                                  @NonNull Listener listener){
        this.mContext = context;
        this.mPlaceList = placeDetailsList;
        this.mListener = listener;
    }
    @Override
    public NearbyPlaceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nearby_place_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NearbyPlaceListAdapter.ViewHolder holder, int position) {
        PlaceDetails placeDetails =  mPlaceList.get(position);
        BBVAMapsLog.d(TAG,"Place name : "+placeDetails.getPlaceName() + ":: Rating :"+placeDetails.getRating());
        holder.txtPlaceName.setText(placeDetails.getPlaceName());
        holder.txtPlaceDistance.setText(String.valueOf(placeDetails.getVicinity() +" miles"));
        holder.itemView.setOnClickListener(createClickListener(placeDetails));
    }

    @Override
    public int getItemCount() {
        return this.mPlaceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPlaceName;
        public TextView txtPlaceDistance;

        public ViewHolder(View view){
            super(view);
            this.txtPlaceName = (TextView) view.findViewById(R.id.place_name_textView);
            this.txtPlaceDistance = (TextView) view.findViewById(R.id.place_distance_textView);
        }
    }

    private View.OnClickListener createClickListener(@NonNull final PlaceDetails placeDetails) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRowItemClicked(placeDetails);
            }
        };
    }
}