package com.n1.moguchi.presentation.fragment.parent.profile

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.remote.model.Parent
import com.n1.moguchi.domain.usecases.FetchParentDataUseCase
import com.n1.moguchi.presentation.ProfileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileParentViewModel @Inject constructor(
    private val fetchParentDataUseCase: FetchParentDataUseCase,
    private val profileImage: ProfileImage
) : ViewModel() {

    fun fetchParentData(parentId: String): Flow<Parent> {
        return fetchParentDataUseCase.invoke(parentId).map {
            it!!
        }
    }

    fun load(url: String, imageView: ImageView) = profileImage.load(url, imageView)
}
