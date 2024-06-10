package com.n1.moguchi.di.modules

import com.n1.moguchi.presentation.LoadImage
import com.n1.moguchi.presentation.ProfileImage
import dagger.Module
import dagger.Provides

@Module
class ImageModule {

    @Provides
    fun provideLoadImage(profileImage: ProfileImage): LoadImage {
        return profileImage
    }
}