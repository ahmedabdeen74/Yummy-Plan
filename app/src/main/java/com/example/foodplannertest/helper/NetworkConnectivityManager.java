package com.example.foodplannertest.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkConnectivityManager {
    private final MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>();
    private final ConnectivityManager connectivityManager;
    private final ConnectivityManager.NetworkCallback networkCallback;

    public NetworkConnectivityManager(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                isNetworkAvailable.postValue(true);
            }

            @Override
            public void onLost(Network network) {
                isNetworkAvailable.postValue(false);
            }
        };
    }

    public void startNetworkCallback() {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public void stopNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    public LiveData<Boolean> getNetworkAvailability() {
        return isNetworkAvailable;
    }
}
