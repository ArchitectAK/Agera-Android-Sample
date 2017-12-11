package com.freeankit.agerasamples;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com) on 12/11/17 (MM/DD/YYYY )
 **/

public class UsernamesFetcher {

    /**
     * Config constant that determines the number of users to return.
     * {@link #getUsernames(UsernamesCallback)} fires an error if this is negative.
     */
    public static int NUMBER_OF_USERS = 4;

    /**
     * This method fakes getting a list of usernames from a server. It fires
     * {@link UsernamesCallback#setError} if {@link #NUMBER_OF_USERS} is negative. It simulates
     * server latency to return usernames.
     */
    public void getUsernames(final UsernamesCallback callback) {
        if (NUMBER_OF_USERS < 0) {
            callback.setError();
            return;
        }

        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // Create a fake list of usernames
                String name1 = "Joe";
                String name2 = "Amanda";
                final List<String> usernames = new ArrayList<String>();
                Random random = new Random();
                for (int i = 0; i < NUMBER_OF_USERS; i++) {
                    int number = random.nextInt(50);
                    if (System.currentTimeMillis() % 2 == 0) {
                        usernames.add(name1 + number);
                    } else {
                        usernames.add(name2 + number);
                    }
                }
                callback.setUsernames(usernames.toArray(new String[usernames.size()]));
            }
        };

        // Simulate network latency
        h.postDelayed(r, 2000);
    }

    public interface UsernamesCallback {
        void setError();

        void setUsernames(String[] usernames);
    }
}
