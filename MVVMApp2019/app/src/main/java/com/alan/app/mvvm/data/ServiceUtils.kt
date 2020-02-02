package com.pcb.fintech.data

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pcb.fintech.BuildConfig
import com.pcb.fintech.data.remote.ApiParams
import com.pcb.fintech.data.remote.ParamsName
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object ServiceUtils {

    private fun getConnectSpec(): ConnectionSpec {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .supportsTlsExtensions(true)
                .build()
    }

    private fun getTrustManagerPair(): Pair<SSLSocketFactory, X509TrustManager> {
        val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                        .apply { init(null as KeyStore?) }
        val trustManagers = trustManagerFactory.trustManagers
        check(trustManagers?.getOrNull(0) is X509TrustManager) {
            "Unexpected default trust managers:" + Arrays.toString(trustManagers)
        }
        val trustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("SSL")
                .apply { init(null, arrayOf<TrustManager>(trustManager), null) }
        return Pair(sslContext.socketFactory, trustManager)
    }

    fun createOkHttpClient(apiParams: ApiParams?, authenticator: Authenticator? = null): OkHttpClient {
        val trustManager = getTrustManagerPair()
        val clientBuilder = OkHttpClient.Builder()
        val paramsInterceptor = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            apiParams?.run {
                if (getAuthorization().isNotEmpty()) {
                    builder.addHeader(ParamsName.AUTHORIZATION, getAuthorization())
                }
                if (getContentType().isNotEmpty()) {
                    builder.addHeader(ParamsName.CONTENT_TYPE, getContentType())
                }
                if (getCacheControl().isNotEmpty()) {
                    builder.addHeader(ParamsName.CACHE_CONTROL, getCacheControl())
                }
                if (getAPIKey().isNotEmpty()) {
                    builder.addHeader(ParamsName.API_KEY, getAPIKey())
                }
                builder.method(original.method(), original.body())
            }
            chain.proceed(builder.build())
        }

        if (authenticator != null) {
            clientBuilder.authenticator(authenticator)
        }

        /*
        Use debugging for develop.
        */
        if (BuildConfig.BUILD_TYPE == "debug") {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
        }
        clientBuilder.addNetworkInterceptor(StethoInterceptor())

        return clientBuilder.connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                /*
                .connectionSpecs(Collections.singletonList(getConnectSpec())) /* Ignore SSL verification */
                .sslSocketFactory(trustManager.first, trustManager.second)
                 */
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(paramsInterceptor)
                .build()
    }

    inline fun <reified T> createWebService(okHttpClient: OkHttpClient, domain: String): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(domain)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                /*.addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/ /* Use it for RxJava. No need if use Roroutine */
                .build()
        return retrofit.create(T::class.java)
    }
}
