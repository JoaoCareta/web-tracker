package com.joao.otavio.core.util
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsNetworkAvailableUseCaseImplTest {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCapabilities: NetworkCapabilities
    private lateinit var activeNetwork: Network
    private lateinit var useCase: IsNetworkAvailableUseCaseImpl

    @Before
    fun setup() {
        connectivityManager = mockk()
        networkCapabilities = mockk()
        activeNetwork = mockk()
        useCase = IsNetworkAvailableUseCaseImpl(connectivityManager)
    }

    @Test
    fun `given wifi available, when checking network availability, then should return true`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given cellular available, when checking network availability, then should return true`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given both wifi and cellular available, when checking network availability, then should return true`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `given no network available, when checking network availability, then should return false`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns null
        every { connectivityManager.getNetworkCapabilities(null) } returns null

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given active network but no capabilities, when checking network availability, then should return false`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns null

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given neither wifi nor cellular available, when checking network availability, then should return false`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `given other transport types available, when checking network availability, then should return false`() {
        // Mockk
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns true

        // Run Test
        val result = useCase.invoke()

        // Assert
        assertFalse(result)
    }
}
