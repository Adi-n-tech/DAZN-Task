package com.app.dazn.di

import com.app.dazn.viewmodel.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    fun provideVideoRepository(): VideoRepository {
        return VideoRepository()
    }
}