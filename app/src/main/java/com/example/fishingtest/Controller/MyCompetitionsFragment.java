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
import android.widget.Toast;

import com.example.fishingtest.Adapter.MyCompAdapter;
import com.example.fishingtest.Model.Common;
import com.example.fishingtest.Model.Competition;
import com.example.fishingtest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Completed by Kan Bu on 8/06/2019.
 *
 * The fragment residing in "HomePageActivity"
 * to display the user's registered competitions for the user
 */

public class MyCompetitionsFragment extends Fragment {

    // Local variables
    List<Map<Integer, Competition>> comps_registered;

    // UI views
    RecyclerView recyclerView;
    FloatingActionButton fab;

    private DatabaseReference databaseComps;
    private String userID;
    private MyCompAdapter cAdapter;

    // Constructor
    public MyCompetitionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the user from Firebase
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_competitions, container, false);

        // Get a reference to recyclerView
        recyclerView =  view.findViewById(R.id.recyclerView_comps);
        // Set layoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        // Initiate the adapter
        initAdapter();
        cAdapter = new MyCompAdapter(comps_registered, getContext());
        // Set adaptor
        recyclerView.setAdapter(cAdapter);

        //floating action button
        fab = (FloatingActionButton) view.findViewById(R.id.floating_button_comp);

        // Set up the OnClick listener of the floating action button
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(cAdapter.row_index >= 0){
                    Intent myIntent = new Intent(getActivity(), ViewCompDetailsActivity.class);
                    getActivity().startActivity(myIntent);
                }
                else{
                    Toast.makeText(getContext(), "Please select a competition for full detail review", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    // Initialize the Recycler View adapter
    private void initAdapter() {
        comps_registered = new ArrayList<>();
        // Find the full details of the registered competition from Firebase database
        databaseComps = FirebaseDatabase.getInstance().getReference("Competitions");
        databaseComps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cAdapter.clearCompList();
                List<Competition> comp_inPrg = new ArrayList<Competition>();
                List<Competition> comp_24Hr = new ArrayList<Competition>();
                List<Competition> comp_normal = new ArrayList<Competition>();

                for(DataSnapshot compSnapshot: dataSnapshot.getChildren()){
                    Competition comp = compSnapshot.getValue(Competition.class);

                    // Initialize the empty ArrayList from the Firebase as they are otherwise null
                    comp.checkArrayList();

                    if(comp.getAttendants().contains(userID)){ // Check if the competition is registered by the current user

                        // Check if the unregistered competition has not yet started
                        String compStartAt = comp.getDate().trim() + " " + comp.getStartTime().trim() + " GMT+10:00"; // Competition Time is based on AEST by default
                        long resultStart = Common.timeToCompStart(compStartAt);
                        long hourStart = resultStart / (60 * 60 * 1000);

                        String compStopAt = comp.getDate().trim() + " " + comp.getStopTime().trim() + " GMT+10:00"; // Competition Time is based on AEST by default
                        long resultStop = Common.timeToCompStart(compStopAt);

                        // Categorise the competition display type
                        if(resultStart <= 0 && resultStop >= 0){
                            comp_inPrg.add(comp);
                        }else if(resultStart > 0 && hourStart <= 24){
                            comp_24Hr.add(comp);
                        }else if(hourStart > 24){
                            comp_normal.add(comp);
                        }
                    }
                }

                // sort by date time
                comp_inPrg.sort(Comparator.comparing(Competition::calCompDateTime));
                comp_24Hr.sort(Comparator.comparing(Competition::calCompDateTime));
                comp_normal.sort(Comparator.comparing(Competition::calCompDateTime));

                // A map combining the competition display type and the Competition object
                Map<Integer, Competition> map;

                // Add three Tittle items for displaying three different types of competitions
                if(comp_inPrg.size()>0){
                    map = new HashMap<Integer, Competition>();
                    map.put(MyCompAdapter.VIEW_TYPE_TITLE, new Competition("Competition in Progress")); // user Competition ID as title text
                    cAdapter.addCompMap(map);

                    for(int i = 0; i < comp_inPrg.size();i++){
                        map = new HashMap<Integer, Competition>();
                        map.put(MyCompAdapter.VIEW_TYPE_INPRG, comp_inPrg.get(i));
                        cAdapter.addCompMap(map);
                    }
                }

                if(comp_24Hr.size() > 0){
                    map = new HashMap<Integer, Competition>();
                    map.put(MyCompAdapter.VIEW_TYPE_TITLE, new Competition("Competition Coming within 24 Hours"));
                    cAdapter.addCompMap(map);

                    for(int i = 0; i < comp_24Hr.size();i++){
                        map = new HashMap<Integer, Competition>();
                        map.put(MyCompAdapter.VIEW_TYPE_24HR, comp_24Hr.get(i));
                        cAdapter.addCompMap(map);
                    }
                }

                if(comp_normal.size()>0){
                    map = new HashMap<Integer, Competition>();
                    map.put(MyCompAdapter.VIEW_TYPE_TITLE, new Competition("Registered Competition"));
                    cAdapter.addCompMap(map);
                    for(int i = 0; i < comp_normal.size();i++){
                        map = new HashMap<Integer, Competition>();
                        map.put(MyCompAdapter.VIEW_TYPE_ITEM, comp_normal.get(i));
                        cAdapter.addCompMap(map);
                    }
                }

                // Notify the recylcer view adapter of the changes in the list of Comp Map
                cAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
