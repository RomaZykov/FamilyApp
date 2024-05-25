package com.n1.moguchi.ui.fragment.parent.profile.related_bottom_sheet.edit_profile

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.remote.model.Parent
import com.n1.moguchi.interactors.FetchParentDataUseCase
import com.n1.moguchi.ui.ProfileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditParentViewModel @Inject constructor(
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
