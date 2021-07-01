package net.tiap.testappinterview.apis

import net.tiap.testappinterview.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Conf {
    val BASE_URL: String = "https://fakestoreapi.com"
}

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClien(): Retrofit {
        if (retrofit == null) {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
//                    .addQueryParameter("token", "")
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()
            val specs = listOf(spec)

            var okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .connectionSpecs(specs)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
            if (BuildConfig.DEBUG) {
                okHttpClient = OkHttpClient.Builder()
                    .connectionSpecs(specs)
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(logging)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()
            }

            retrofit = Retrofit.Builder()
                .baseUrl(Conf.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}