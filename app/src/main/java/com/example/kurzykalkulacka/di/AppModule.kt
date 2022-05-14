package com.example.kurzykalkulacka.di

import android.content.Context
import androidx.room.Room
import com.example.kurzykalkulacka.KurzyAPI
import com.example.kurzykalkulacka.data.dao.KurzDao
import com.example.kurzykalkulacka.data.dao.MenaDao
import com.example.kurzykalkulacka.data.DefaultDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): DefaultDatabase {
        return Room.databaseBuilder(
            appContext,
            DefaultDatabase::class.java,
            "KurzDB"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        return Retrofit.Builder()
            .baseUrl("https://www.ecb.europa.eu/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    fun provideKurzAPI(retrofit: Retrofit): KurzyAPI {
        return retrofit.create(KurzyAPI::class.java)
    }

    @Provides
    fun provideKurzDao(db: DefaultDatabase): KurzDao {
        return db.kurzDao
    }

    @Provides
    fun provideMenaDao(db: DefaultDatabase): MenaDao {
        return db.menaDao
    }
}