package com.ensak.AlloOustad.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ensak.AlloOustad.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ensak.AlloOustad.model.models.PreEnseignant;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EnseignantsListAdapter extends RecyclerView.Adapter<EnseignantsListAdapter.EnseignantViewHolder> {

    private final List<PreEnseignant> mEnseignantList;

    private final Context context;

    private final EnseignantAdapterOnClickHandler mClickHandler;

    private NumberFormat formatter;

    public interface EnseignantAdapterOnClickHandler {
        void onClick(int position);
    }


    public EnseignantsListAdapter(List<PreEnseignant> doctorList, Context context, EnseignantAdapterOnClickHandler clickHandler) {
        this.mEnseignantList = doctorList;
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public EnseignantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.doctor_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        formatter = new DecimalFormat("#0.00");

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new EnseignantViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final EnseignantViewHolder holder, int position) {

        String firstName = mEnseignantList.get(position).getProfile().getFirst_name();
        String lastName = mEnseignantList.get(position).getProfile().getLast_name();
        String name = firstName + " " + lastName;
        holder.nameTextView.setText(name);

        if (mEnseignantList.get(position).getMatiers().size() != 0) {
            String specialty = mEnseignantList.get(position).getMatiers().get(0).getName();
            holder.specialtyTextView.setText(specialty);
        }

        if (mEnseignantList.get(position).getCoordonnees().size() != 0) {

            String address = mEnseignantList.get(position).getCoordonnees().get(0).getVisitAddress().getStateLong() + ", " +
                    mEnseignantList.get(position).getCoordonnees().get(0).getVisitAddress().getCity() + ", " +
                    mEnseignantList.get(position).getCoordonnees().get(0).getVisitAddress().getStreet();

            holder.addressTextView.setText(address);

        }


        String profileImageUri = mEnseignantList.get(position).getProfile().getImage_url();
        Picasso.with(context)
                .load(profileImageUri)
                .noFade().
                into(holder.profileImageView);


    }

    @Override
    public int getItemCount() {
        if (mEnseignantList != null) {
            return mEnseignantList.size();
        } else {
            return 0;
        }

    }

    public class EnseignantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_profile_image_card)
        CircleImageView profileImageView;
        @BindView(R.id.tv_enseignant_name_card)
        TextView nameTextView;
        @BindView(R.id.tv_enseignant_specialty_card)
        TextView specialtyTextView;
        @BindView(R.id.tv_distance_card)
        TextView distanceTextView;
        @BindView(R.id.tv_enseignant_address_card)
        TextView addressTextView;

        EnseignantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void clearData() {
        mEnseignantList.clear();
        notifyDataSetChanged();
    }
}
