package com.listapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.listapp.R;

/**
 * Created by syscraft on 8/21/2017.
 */

public class UpdateAppActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                    ("market://details?id=com.listapp.in")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
