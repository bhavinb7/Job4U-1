package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.CategoryAdapter;
import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Category;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends Fragment {
    EditText editText;

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    LinearLayoutManager linearLayoutManager;

    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    JobAdapter jobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    RelativeLayout lay1, lay2, lay3;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initPreferences();
        lay1 = view.findViewById(R.id.lay1);
        lay2 = view.findViewById(R.id.lay2);
        lay3 = view.findViewById(R.id.lay3);

        recyclerView = view.findViewById(R.id.category_filtered);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryArrayList = new ArrayList<>();
        categoryArrayList = getJobCategory();
        categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList,preferences,preferenceEditor,R.id.action_searchFragment_to_jobListFragment);
        recyclerView.setAdapter(categoryAdapter);

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job_filtered);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);

        jobArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    jobAdapter = new JobAdapter(getContext(), jobArrayList,preferences,preferenceEditor, R.id.action_searchFragment_to_jobPreviewFragment2);
                    jobRecyclerView.setAdapter(jobAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editText = view.findViewById(R.id.search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return view;
    }
    private ArrayList<Category> getJobCategory() {
        ArrayList<Category> jobCategories = new ArrayList<>();
        Category jc = new Category(R.drawable.ic_frontend_developer, "Web Developer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_mobile_app_developer, "Android Developer");

        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_web_designer, "Web Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_ux_interface, "UI/UX Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_graphic_designer, "Graphics Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_backend, "Backend Developer");

        jobCategories.add(jc);
        return jobCategories;
    }

    private void filter(String text) {
        ArrayList<Category> filteredList = new ArrayList<>();
        ArrayList<Job> filteredList2 = new ArrayList<>();

        for (Category item : categoryArrayList) {
            if (item.getC_Job_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        for (Job item : jobArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList2.add(item);
            }
        }

        categoryAdapter.filterList(filteredList);
        jobAdapter.filterList(filteredList2);

        if (filteredList.isEmpty()){
            lay1.setVisibility(View.GONE);
        } else {
            lay1.setVisibility(View.VISIBLE);
            lay3.setVisibility(View.GONE);
        }
        if (filteredList2.isEmpty()){
            lay2.setVisibility(View.GONE);
        } else {
            lay2.setVisibility(View.VISIBLE);
            lay3.setVisibility(View.GONE);
        }
        if (filteredList.isEmpty()&&filteredList2.isEmpty()){
            lay1.setVisibility(View.GONE);
            lay2.setVisibility(View.GONE);
            lay3.setVisibility(View.VISIBLE);

        }
    }
    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}