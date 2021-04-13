package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AddJobFragment extends Fragment {
    EditText desigation, description, salary, companyname, city, website, vacancy, pod;
    Spinner jobtype, jobQualification;

    Button add_job;
    DatabaseReference db_add_job;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    public AddJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_job, container, false);

        initViews(view);
        initPreferences();
        addListeners(view);


        db_add_job = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");


        return view;
    }

    private void initViews(View view) {
        jobtype = view.findViewById(R.id.jobtitle);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.job_title,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobtype.setPrompt("Select Job");
        jobtype.setAdapter(adapter);

        jobQualification = view.findViewById(R.id.jobQualification);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.job_qualification,
                android.R.layout.simple_spinner_item);
        jobQualification.setPrompt("Select Qualification");
        jobQualification.setAdapter(adapter1);

        desigation = view.findViewById(R.id.edit_desig);
        description = view.findViewById(R.id.edit_desc);
        salary = view.findViewById(R.id.edit_salary);
        companyname = view.findViewById(R.id.edit_company_name);
        city = view.findViewById(R.id.edit_city);
        website = view.findViewById(R.id.edit_website);
        pod = view.findViewById(R.id.edit_post_date);
        Date date = new Date();
        CharSequence s = DateFormat.format("MMMM d,yyyy", date.getTime());
        pod.setText(s);
        pod.setEnabled(false);

        vacancy = view.findViewById(R.id.edit_vacancy);

        add_job = view.findViewById(R.id.btn_addjob);

    }

    private void addListeners(View view) {
        add_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addjob();
                clear();
            }
        });
    }

    private void addjob() {
        String title = jobtype.getSelectedItem().toString().trim();
        String desi = desigation.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String sala = salary.getText().toString().trim();
        String company = companyname.getText().toString().trim();
        String cit = city.getText().toString().trim();
        String web = website.getText().toString().trim();
        String quali = jobQualification.getSelectedItem().toString().trim();
        String post_date = pod.getText().toString().trim();
        String vacan = vacancy.getText().toString().trim();

        if (title.equals("")) {
            jobtype.requestFocus();
            Toast.makeText(getContext(), "Title is required !", Toast.LENGTH_SHORT).show();
        } else if (quali.equals("")) {
            jobQualification.requestFocus();
            Toast.makeText(getContext(), "Qualification is required !", Toast.LENGTH_SHORT).show();
        } else if (desi.isEmpty()) {
            desigation.requestFocus();
            Toast.makeText(getContext(), "Designation is required !", Toast.LENGTH_SHORT).show();
        } else if (sala.isEmpty()) {
            salary.requestFocus();
            Toast.makeText(getContext(), "Salary is required !", Toast.LENGTH_SHORT).show();
        } else if (company.isEmpty()) {
            companyname.requestFocus();
            Toast.makeText(getContext(), "Company name is required !", Toast.LENGTH_SHORT).show();
        } else if (cit.isEmpty()) {
            city.requestFocus();
            Toast.makeText(getContext(), "City is required !", Toast.LENGTH_SHORT).show();
        } else if (web.isEmpty()) {
            website.requestFocus();
            Toast.makeText(getContext(), "Website is required !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.WEB_URL.matcher(web).matches()) {
            website.requestFocus();
            Toast.makeText(getContext(), "Please enter valid Website !", Toast.LENGTH_SHORT).show();
        } else if (vacan.isEmpty()) {
            vacancy.requestFocus();
            Toast.makeText(getContext(), "No. of Vacancy is required !", Toast.LENGTH_SHORT).show();
        } else {
            String id = db_add_job.push().getKey();

            Job job = new Job(id, title, desi, desc, sala, company, cit, web, vacan, post_date, quali);
            db_add_job.child(id).setValue(job).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(getContext(), "Job added", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void clear() {
        jobtype.setSelection(0);
        jobQualification.setSelection(0);
        desigation.setText("");
        description.setText("");
        salary.setText("");
        companyname.setText("");
        city.setText("");
        website.setText("");
        vacancy.setText("");
        pod.setText("");

    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}