package com.example.visualcovid_19;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;


public class CountryCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static TextView countryName;
    private static TextView totalCases;
    private static ImageButton star;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CountryName = "Country Name";
    private static final String CasesCount = "99999";

    // TODO: Rename and change types of parameters
    private String mCountryName;
    private String mCasesCount;


    public CountryCardFragment(String name, String count) {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CountryCardFragment newInstance(String name, String count) {
        CountryCardFragment fragment = new CountryCardFragment(name, count);
        Bundle args = new Bundle();
        args.putString(CountryName, name);
        args.putString(CasesCount, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCountryName = getArguments().getString(CountryName);
            mCasesCount = getArguments().getString(CasesCount);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.country_card_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countryName = view.findViewById(R.id.countryName);
        totalCases = view.findViewById(R.id.totalCases);
        star = view.findViewById(R.id.btnOFF);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo Star Functionality
            }
        });
        countryName.setText(mCountryName);
        totalCases.setText(mCasesCount);

    }
}
