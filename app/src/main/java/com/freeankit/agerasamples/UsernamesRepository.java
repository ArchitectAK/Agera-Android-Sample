package com.freeankit.agerasamples;

import android.support.annotation.NonNull;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com) on 12/11/17 (MM/DD/YYYY )
 **/

public class UsernamesRepository extends BaseObservable
        implements Supplier<String[]>, Updatable, UsernamesFetcher.UsernamesCallback {

    /**
     * The usernames list. This list is the most up to date known usernames.
     */
    private String[] usernames;

    /**
     * Whether the last update resulted in an error to retrieve a new list of usernames.
     */
    private boolean lastRefreshError;

    /**
     * This is responsible for getting the list of usernames. It simulates a server call, and uses
     * a {@link UsernamesFetcher.UsernamesCallback}.
     */
    private final UsernamesFetcher usernamesFetcher;

    public UsernamesRepository(UsernamesFetcher usernamesFetcher) {
        super();
        this.usernamesFetcher = usernamesFetcher;
    }

    /**
     * @return the most up to date known list of usernames
     */
    @NonNull
    @Override
    public String[] get() {
        return usernames;
    }

    /**
     * @return true if the last update resulted in an error in retrieving a new list of usernames
     */
    public boolean isError() {
        return lastRefreshError;
    }

    /**
     * As this {@link UsernamesRepository} is set up to observe the {@link OnRefreshObservable},
     * this is triggered whenever a request has been requested.
     */
    @Override
    public void update() {
        usernamesFetcher.getUsernames(this);
    }

    /**
     * The {@link #usernamesFetcher} couldn't fetch a new list of usernames.
     */
    @Override
    public void setError() {
        lastRefreshError = true;
        dispatchUpdate();
    }

    /**
     * @param usernames The new list of usernames fetched by the {@link #usernamesFetcher}
     */
    @Override
    public void setUsernames(String[] usernames) {
        this.usernames = usernames;
        lastRefreshError = false;
        dispatchUpdate();
    }

    @Override
    protected void observableActivated() {
        // Now that this is activated, we trigger an update to ensure the repository contains up to
        // date data.
        update();
    }
}
