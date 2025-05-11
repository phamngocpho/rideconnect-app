package com.rideconnect.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.rideconnect.BuildConfig
import com.rideconnect.data.remote.api.*
import com.rideconnect.data.remote.interceptor.AuthInterceptor
import com.rideconnect.data.remote.interceptor.NetworkInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val properties by lazy {
        Properties().apply {
            try {
                val localPropertiesFile = File(System.getProperty("user.dir"), "../local.properties")
                if (localPropertiesFile.exists()) {
                    load(FileInputStream(localPropertiesFile))
                } else {
                    val altFile = File("local.properties")
                    if (altFile.exists()) {
                        load(FileInputStream(altFile))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return try {
            val buildConfigClass = Class.forName("com.rideconnect.BuildConfig")
            val field = buildConfigClass.getField("API_BASE_URL")
            field.get(null) as String
        } catch (e: Exception) {
            properties.getProperty("api.base.url", "http://10.0.2.2:8080/api/")
        }
    }

    @Provides
    @Singleton
    @Named("goongApiBaseUrl")
    fun provideGoongApiBaseUrl(): String {
        return try {
            val buildConfigClass = Class.forName("com.rideconnect.BuildConfig")
            val field = buildConfigClass.getField("GOONG_API_BASE_URL")
            field.get(null) as String
        } catch (e: Exception) {
            properties.getProperty("goong.api.base.url", "https://rsapi.goong.io/")
        }
    }

    @Provides
    @Singleton
    @Named("goongApiKey")
    fun provideGoongApiKey(): String {
        val apiKey = try {
            val buildConfigClass = Class.forName("com.rideconnect.BuildConfig")
            val field = buildConfigClass.getField("GOONG_API_KEY")
            field.get(null) as String
        } catch (e: Exception) {
            properties.getProperty("goong.api.key", "")
        }

        // Log một phần của API key để kiểm tra
        if (apiKey.isBlank()) {
            Log.e("GoongAPI", "WARNING: API key is blank or missing!")
        } else {
            val maskedKey = if (apiKey.length > 4) {
                apiKey.substring(0, 4) + "..." + apiKey.takeLast(2)
            } else {
                "***"
            }
            Log.d("GoongAPI", "Using API key: $maskedKey")
        }

        return apiKey
    }

    @Provides
    @Singleton
    @Named("goongMaptilesKey")
    fun provideGoongMaptilesKey(): String {
        return try {
            val buildConfigClass = Class.forName("com.rideconnect.BuildConfig")
            val field = buildConfigClass.getField("GOONG_MAPTILES_KEY")
            field.get(null) as String
        } catch (e: Exception) {
            properties.getProperty("goong.maptiles.key", "")
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            // Thay đổi từ đây: Thêm Log.d để xem logs trong Logcat
            Log.d("API_LOG", message)
        }.apply {
            val isDebug = try {
                val buildConfigClass = Class.forName("com.rideconnect.BuildConfig")
                val debugField = buildConfigClass.getField("DEBUG")
                debugField.getBoolean(null)
            } catch (e: Exception) {
                true // Mặc định là true nếu không thể lấy được giá trị DEBUG
            }

            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(networkInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    @Named("goongOkHttpClient")
    fun provideGoongOkHttpClient(
        @Named("goongApiKey") goongApiKey: String
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("GoongAPI", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url

                // Kiểm tra API key trước khi thêm vào request
                if (goongApiKey.isBlank()) {
                    Log.e("GoongAPI", "ERROR: API key is blank, request will likely fail!")
                }

                // Thử cả hai cách thêm API key
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", goongApiKey)
                    // Nếu cách trên không hoạt động, thử cách này
                    // .addQueryParameter("key", goongApiKey)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                Log.d("GoongAPI", "Request URL: ${request.url}")
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        // Cấu hình Gson với setLenient(true)
        val gson = GsonBuilder()
            .setLenient() // Cho phép xử lý JSON không chuẩn
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Sử dụng gson đã cấu hình
            .build()
    }

    @Provides
    @Singleton
    @Named("goongRetrofit")
    fun provideGoongRetrofit(
        @Named("goongApiBaseUrl") baseUrl: String,
        @Named("goongOkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDriverApi(retrofit: Retrofit): DriverApi {
        return retrofit.create(DriverApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationApi(retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d("API_LOG", message)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideGoongMapApi(@Named("goongRetrofit") retrofit: Retrofit): GoongMapApi {
        return retrofit.create(GoongMapApi::class.java)
    }
}
