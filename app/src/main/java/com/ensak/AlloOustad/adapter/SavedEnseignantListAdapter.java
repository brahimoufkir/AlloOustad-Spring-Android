package com.ensak.AlloOustad.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ensak.AlloOustad.R;
import com.ensak.AlloOustad.database.EnseignantEntry;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SavedEnseignantListAdapter extends RecyclerView.Adapter<SavedEnseignantListAdapter.EnseignantViewHolder> {

    private List<EnseignantEntry> mEnseignantList;

    private final Context context;

    private final SavedEnseignantAdapterOnClickHandler mClickHandler;

    public interface SavedEnseignantAdapterOnClickHandler {
        void onClick(int position);
    }


    public SavedEnseignantListAdapter(Context context, SavedEnseignantAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    public void setEnseignants(List<EnseignantEntry> doctorEntries) {
        mEnseignantList = doctorEntries;
        notifyDataSetChanged();
    }

    @Override
    public EnseignantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.doctor_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new EnseignantViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final EnseignantViewHolder holder, int position) {

        String firstName = mEnseignantList.get(position).getFirstName();
        String lastName = mEnseignantList.get(position).getLastName();
        String name = firstName + " " + lastName;
        holder.nameTextView.setText(name);

        if (mEnseignantList.get(position).getSpecialtyName() != null ||
                !mEnseignantList.get(position).getSpecialtyName().equals("")) {
            String specialty = mEnseignantList.get(position).getSpecialtyName();
            holder.specialtyTextView.setText(specialty);
        }

        String address = mEnseignantList.get(position).getState() + ", " +
                mEnseignantList.get(position).getCity() + ", " +
                mEnseignantList.get(position).getStreet();
        if (!address.equals(", , ")) {
            holder.addressTextView.setText(address);
        }

        String profileImageUri = mEnseignantList.get(position).getProfileImage();
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


}
