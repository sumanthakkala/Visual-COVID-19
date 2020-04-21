package com.sumanthakkala.visualcovid_19;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private AdView cardAdView;

    public AdFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AdFragment newInstance() {
        AdFragment fragment = new AdFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardAdView = view.findViewById(R.id.countryCardAdUnit);
        AdRequest adRequest = new AdRequest.Builder().build();
        cardAdView.loadAd(adRequest);
    }
}
