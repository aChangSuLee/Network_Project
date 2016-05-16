package com.example.leejs4937.networkproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class TabActivity extends android.app.TabActivity {
    TabHost mTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTab = getTabHost();
        TabHost.TabSpec spec;
        LayoutInflater.from(this).inflate(R.layout.activity_tab, mTab.getTabContentView(), true);
        spec = mTab.newTabSpec("tab1").setIndicator("구매").setContent(R.id.tab1);
        mTab.addTab(spec);
        spec = mTab.newTabSpec("tab2").setIndicator("판매").setContent(R.id.tab2);
        mTab.addTab(spec);
    }
}
