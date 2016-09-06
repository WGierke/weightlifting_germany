package de.weightlifting.app.buli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;

import de.weightlifting.app.R;
import de.weightlifting.app.WeightliftingApp;
import de.weightlifting.app.helper.API;
import de.weightlifting.app.helper.NetworkHelper;
import de.weightlifting.app.helper.UiHelper;

public class ProtocolFragment extends Fragment {

    private String protocolUrl;
    private String competitionParties;
    private WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.buli_competition_protocol, container, false);
        FloatingActionButton fab = (FloatingActionButton) fragment.findViewById(R.id.fab);
        FloatingActionButton print = (FloatingActionButton) fragment.findViewById(R.id.print_icon);

        webview = (WebView) fragment.findViewById(R.id.buli_competition_protocol);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // Get protocol url from bundle
        try {
            Bundle bundle = this.getArguments();
            protocolUrl = bundle.getString(API.PROTOCOL_URL);
            competitionParties = bundle.getString(API.COMPETITION_PARTIES);
            webview.loadUrl(protocolUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (protocolUrl.length() > 0) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WeightliftingApp app = (WeightliftingApp) getActivity().getApplication();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, competitionParties);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, competitionParties + ". " + getString(R.string.buli_protocol) + ": " + protocolUrl);
                    shareIntent.setType("text/plain");

                    Intent chooserIntent = Intent.createChooser(shareIntent, getString(R.string.share_this));
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    app.startActivity(chooserIntent);

                    Answers.getInstance().logShare(new ShareEvent()
                            .putMethod("Protocol Share")
                            .putContentName(competitionParties));

                    NetworkHelper.sendProtocolShare(competitionParties);
                }
            });
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                        PrintDocumentAdapter printAdapter = webview.createPrintDocumentAdapter();
                        String jobName = competitionParties;
                        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
                    } else {
                        UiHelper.showToast(getString(R.string.buli_print_too_old), getActivity());
                    }
                }
            });
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
        return fragment;
    }
}
