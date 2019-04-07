package com.example.fishingtest.Controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fishingtest.Adapter.CompAdapter;
import com.example.fishingtest.Model.Competition;
import com.example.fishingtest.Model.User;
import com.example.fishingtest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CompetitionsFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    ArrayList<Competition> comps_registered;

    private FirebaseUser user;
    private DatabaseReference databaseUsers;

    ArrayList<Competition> comps;



    public CompetitionsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_competitions, container, false);

        // Get a reference to recyclerView
        recyclerView =  view.findViewById(R.id.recyclerView_comps);
        // Set layoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //Floating button
        fab = (FloatingActionButton) view.findViewById(R.id.floating_button_comp);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(getActivity(),RegisterCompActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        // TODO: Get Competitions enrolled by the user from Firebase

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");


        // TODO: decide what image sample to be used to decide "comps"
        comps = new ArrayList<>();
        // Required empty public constructor
        for(int i = 1; i < 12; i++){
            Competition temp = new Competition(Integer.toString(i));
            comps.add(temp);
        }


        String userID = user.getUid();
        DatabaseReference databaseThisUser = databaseUsers.child(userID);

        databaseThisUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                User temp = dataSnapshot.getValue(User.class);
//                String name = temp.displayName;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        // Create an adapter
        CompAdapter cAdapter = new CompAdapter(comps);
        // Set adaptor
        recyclerView.setAdapter(cAdapter);


        return view;
    }

}