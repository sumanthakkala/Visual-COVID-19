package com.sumanthakkala.visualcovid_19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class CountryCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Context ctx;
    private static TextView countryName;
    private static TextView totalCases;
    private static TextView activeCases;
    private static TextView recoveredCases;
    private static TextView fatalCases;
    private static ImageButton star;
    private CardView countryCardView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CountryName = "Country Name";
    private static final String TotalCasesCount = "99999";
    private static final String ActiveCasesCount = "8888";
    private static final String RecoveredCasesCount = "7777";
    private static final String FatalCasesCount = "6666";

    // TODO: Rename and change types of parameters
    private String mCountryName;
    private String mTotalCasesCount;
    private String mActiveCasesCount;
    private String mRecoveredCasesCount;
    private String mFatalCasesCount;


    public CountryCardFragment(Context ctx) {
        // Required empty public constructor
        this.ctx = ctx;
    }

    // TODO: Rename and change types and number of parameters
    public static CountryCardFragment newInstance(String name, String totalCount, String activeCount, String recoveredCount, String fatalCount, Context ctx) {
        CountryCardFragment fragment = new CountryCardFragment(ctx);
        Bundle args = new Bundle();
        args.putString(CountryName, name);
        args.putString(TotalCasesCount, totalCount);
        args.putString(ActiveCasesCount, activeCount);
        args.putString(RecoveredCasesCount, recoveredCount);
        args.putString(FatalCasesCount, fatalCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCountryName = getArguments().getString(CountryName);
            mTotalCasesCount = getArguments().getString(TotalCasesCount);
            mActiveCasesCount = getArguments().getString(ActiveCasesCount);
            mRecoveredCasesCount = getArguments().getString(RecoveredCasesCount);
            mFatalCasesCount = getArguments().getString(FatalCasesCount);
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
        totalCases = view.findViewById(R.id.totalCasesSummaryCard);
        activeCases = view.findViewById(R.id.invisibleActiveCasesSummaryCard);
        recoveredCases = view.findViewById(R.id.invisibleRecoveredCasesSummaryCard);
        fatalCases = view.findViewById(R.id.invisibleFatalCasesSummaryCard);
        star = view.findViewById(R.id.btnOFF);
        countryCardView = view.findViewById(R.id.summaryCard);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo Star Functionality
            }
        });
        countryName.setText(mCountryName);
        totalCases.setText(mTotalCasesCount);
        activeCases.setText(mActiveCasesCount);
        recoveredCases.setText(mRecoveredCasesCount);
        fatalCases.setText(mFatalCasesCount);

        countryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(ctx, CountryDetailView.class);
                intent.putExtra("TotalCases", ((TextView)v.findViewById(R.id.totalCasesSummaryCard)).getText().toString());
                intent.putExtra("ActiveCases", ((TextView)v.findViewById(R.id.invisibleActiveCasesSummaryCard)).getText().toString());
                intent.putExtra("RecoveredCases", ((TextView)v.findViewById(R.id.invisibleRecoveredCasesSummaryCard)).getText().toString());
                intent.putExtra("FatalCases", ((TextView)v.findViewById(R.id.invisibleFatalCasesSummaryCard)).getText().toString());
                startActivity(intent);
            }
        });

    }


}
