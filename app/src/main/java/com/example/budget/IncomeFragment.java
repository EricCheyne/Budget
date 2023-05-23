package com.example.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budget.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/*
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {

    //Firebase DB
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //RecyclerView
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    // Text view

    private TextView incomeTotalSum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totlatvalue = 0;

                for (DataSnapshot mysanapshot:dataSnapshot.getChildren()) {

                    Data data=mysanapshot.getValue(Data.class);

                    totlatvalue+=data.getAmount();

                    String stTotalvale=String.valueOf(totlatvalue);

                    incomeTotalSum.setText(stTotalvale);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDatabase, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {

            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false));
            }

            protected void onBindViewHolder(MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
            }
        };
        recyclerView.setAdapter(adapter);
    }
}


class MyViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    void setType(String type) {
        TextView mType = mView.findViewById(R.id.type_txt_income);
        mType.setText(type);
    }

    void setNote(String note) {

        TextView mNote = mView.findViewById(R.id.note_txt_income);
        mNote.setText(note);
    }

    void setDate(String date) {
        TextView mDate = mView.findViewById(R.id.date_txt_income);
        mDate.setText(date);
    }

    void setAmmount(int ammount) {
        TextView mAmmount = mView.findViewById(R.id.ammount_txt_income);
        String stammount = String.valueOf(ammount);
        mAmmount.setText(stammount);
    }

}