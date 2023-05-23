package com.example.budget;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class ExpenseFragment extends Fragment {

    //Firebase Database
    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    //RecyclerView
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    // Text view

    private TextView expenseTotalSum;

    //Update edit text

    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;

    //button for update and delete

    private Button btnUpdate;
    private Button btnDelete;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        expenseTotalSum=myview.findViewById(R.id.expense_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totlatvalue = 0;

                for (DataSnapshot mysanapshot:dataSnapshot.getChildren()) {

                    Data data=mysanapshot.getValue(Data.class);

                    totlatvalue+=data.getAmount();

                    String stTotalvale=String.valueOf(totlatvalue);

                    expenseTotalSum.setText(stTotalvale);


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
                .setQuery(mExpenseDatabase, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {

            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false));
            }

            protected void onBindViewHolder(MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        void setNote(String note) {

            TextView mNote = mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        void setAmmount(int ammount) {
            TextView mAmmount = mView.findViewById(R.id.ammount_txt_expense);
            String stammount = String.valueOf(ammount);
            mAmmount.setText(stammount);
        }

    }

    private void updateDataItem(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmmount=myview.findViewById(R.id.ammount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);

        btnUpdate=myview.findViewById(R.id.btnUpdate);
        btnDelete=myview.findViewById(R.id.btnuPD_Delete);

        AlertDialog dialog= mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

}


