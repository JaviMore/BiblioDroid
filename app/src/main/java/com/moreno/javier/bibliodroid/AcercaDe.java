package com.moreno.javier.bibliodroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Javas on 19/01/2016.
 */

public class AcercaDe extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_acercade);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView attribution = (TextView) findViewById(R.id.icon_attribution);

        attribution.setText(Html.fromHtml("Iconos creados por Freepik de <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a> licencia " +
               "Creative Commons BY 3.0"));
        attribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.flaticon.com"));
                startActivity(browserIntent);
            }
        });
    }
}
