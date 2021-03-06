package com.walmart.jikanimage.modules

import com.squareup.moshi.Moshi
import com.walmart.jikanimage.helper.BASE_URL
import com.walmart.jikanimage.repository.JikanAPI
import com.walmart.jikanimage.repository.JikanRepository
import com.walmart.jikanimage.viewmodels.ImagesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/* Define module variables */
val networkModule = module {
    single { provideRetrofit(get(), get()) }
    single { provideOkHttpClient() }
    single { provideMoshi() }
}

val repositoryModule = module {
    single { JikanRepository(get()) }
    single { provideRetrofit(get()) }
}

val viewModelModule = module {
    viewModel { ImagesViewModel(get()) }
}


/**
 * @return [Retrofit] instance
 */
private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .client(okHttpClient)
    .build()

/**
 * @return [OkHttpClient] instance
 */
private fun provideOkHttpClient() = OkHttpClient.Builder()
    .readTimeout(10L, TimeUnit.SECONDS)
    .readTimeout(15L, TimeUnit.SECONDS)
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    )
    .build()

/**
 * @return [Moshi] instance
 */
private fun provideMoshi(): Moshi = Moshi.Builder().build()

/**
 * @return [JikanAPI] instance
 */
private fun provideRetrofit(retrofit: Retrofit): JikanAPI = retrofit.create(JikanAPI::class.java)