package de.weightlifting.app.buli;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import de.weightlifting.app.MainActivity;
import de.weightlifting.app.R;
import de.weightlifting.app.helper.API;

public class CompetitionsFragment extends ListViewFragment {

    protected void getBuliElements() {
    }

    @Override
    protected void setEmptyListItem() {
        TextView emptyText = (TextView) fragment.findViewById(R.id.emptyCompetitions);
        emptyText.setVisibility(View.VISIBLE);
        listViewBuli.setEmptyView(emptyText);
    }

    public void setCompetitionsListAdaptherWithProtocolFragment(final ArrayList<PastCompetition> competitionItems, Activity activity) {
        CompetitionsListAdapter adapter = new CompetitionsListAdapter(competitionItems, activity);
        listViewBuli.setAdapter(adapter);
        listViewBuli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Show the protocol which belongs to the competition
                Fragment protocol = new ProtocolFragment();
                Bundle bundle = new Bundle();
                PastCompetition competition = competitionItems.get(position);
                bundle.putString(API.PROTOCOL_URL, competition.getProtocolUrl());
                bundle.putString(API.COMPETITION_PARTIES, competition.getHome() + " vs. " + competition.getGuest() + ": " + competition.getScore());
                protocol.setArguments(bundle);
                ((MainActivity) getActivity()).addFragment(protocol, getString(R.string.nav_buli), true);
            }
        });
    }

}
