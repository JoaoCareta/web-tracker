package com.joao.otavio.core.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class IsNetworkAvailableUseCaseImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
): IsNetworkAvailableUseCase {

    override fun invoke(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )
        return networkCapabilities != null &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}
