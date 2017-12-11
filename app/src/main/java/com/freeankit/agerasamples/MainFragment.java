package com.freeankit.agerasamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.google.android.agera.Updatable;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com) on 12/11/17 (MM/DD/YYYY )
 **/

public class MainFragment extends Fragment implements Updatable {
    private OnRefreshObservable refreshObservable;
    private UsernamesRepository usernamesRepository;

    private ListAdapter listAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.list);

        // Set pull to refresh as an observable and attach it to the view
        refreshObservable = new OnRefreshObservable();
        swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(refreshObservable);

        // Initialise the repository
        usernamesRepository = new UsernamesRepository(new UsernamesFetcher());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // We make sure the repository observes the refresh listener
        refreshObservable.addUpdatable(usernamesRepository);

        /**
         * We make sure the main fragment observes the repository. This will also trigger the
         * repository to update itself, via
         * {@link UsernamesRepository#firstUpdatableAdded(UpdateDispatcher)}.
         */
        usernamesRepository.addUpdatable(this);

        /**
         * We update the UI to show the data is being updated. We need to wait for the
         * {@link swipeRefreshLayout} to be ready before asking it to show itself as refreshing.
         */
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void update() {

    }
}
