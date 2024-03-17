package com.oppo.moeslimbuddy.di

import android.os.Build
import android.preference.PreferenceManager
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.oppo.moeslimbuddy.BuildConfig
import com.oppo.moeslimbuddy.util.CommonUtil
import com.securepreferences.SecurePreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkingModule = module {
    single { provideGson() }
    single { provideGsonConverterFactory(get()) }

    single(named("api")) { provideRetrofit(get()) }
}


val sharedPreferencesModule = module {
    single {
        if (BuildConfig.DEBUG) {
            PreferenceManager.getDefaultSharedPreferences(androidContext())
        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val spec = KeyGenParameterSpec.Builder(
                        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                        .build()
                    val masterKey = MasterKey.Builder(get())
                        .setKeyGenParameterSpec(spec)
                        .build()

                    EncryptedSharedPreferences.create(
                        androidContext(),
                        "tf_secret_shared_prefs",
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                } else {
                    SecurePreferences(
                        androidContext(),
                        BuildConfig.DB_PASS,
                        "tf_secret_shared_prefs"
                    )
                }
            } catch (e: Exception) {
                PreferenceManager.getDefaultSharedPreferences(androidContext())
            } catch (e: NoClassDefFoundError) {
                PreferenceManager.getDefaultSharedPreferences(androidContext())
            }
        }
    }
}

private fun provideGson(): Gson = Gson()

private fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

private fun provideRetrofit(
    converterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(CommonUtil.hexToAscii(BuildConfig.BASE_API_URL))
        .addConverterFactory(converterFactory)
        .build()

}