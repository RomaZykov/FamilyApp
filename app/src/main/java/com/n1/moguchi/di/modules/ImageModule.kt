package com.n1.moguchi.di.modules

import com.n1.moguchi.ui.LoadImage
import com.n1.moguchi.ui.ProfileImage
import dagger.Module
import dagger.Provides

@Module
class ImageModule {

    @Provides
    fun provideLoadImage(profileImage: ProfileImage): LoadImage {
        return profileImage
    }
}