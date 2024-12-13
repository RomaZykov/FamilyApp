package com.example.familyapp.di.modules

import com.example.familyapp.ui.LoadImage
import com.example.familyapp.ui.ProfileImage
import dagger.Module
import dagger.Provides

@Module
class ImageModule {

    @Provides
    fun provideLoadImage(profileImage: ProfileImage): LoadImage {
        return profileImage
    }
}