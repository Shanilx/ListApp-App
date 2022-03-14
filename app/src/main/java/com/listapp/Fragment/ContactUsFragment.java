package com.listapp.Fragment;

/**
 * Created by syscraft on 8/2/2017.
 */

import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.listapp.R;

public class ContactUsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout call, email;
    private LinearLayout whatsapp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_us, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        call = view.findViewById(R.id.call);
        email = view.findViewById(R.id.email);
        whatsapp = view.findViewById(R.id.whatsapp);
        call.setOnClickListener(this);
        email.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.call:

                try {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919977773388")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.email:

                try {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", "support@listapp.in", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.whatsapp:

                try {

                    Uri uri = Uri.parse("smsto:" + "+919977773388");
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(i, ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
