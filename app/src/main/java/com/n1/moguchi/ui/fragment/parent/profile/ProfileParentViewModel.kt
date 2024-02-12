package com.n1.moguchi.ui.fragment.parent.profile

import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.Parent
import com.n1.moguchi.interactors.FetchParentDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileParentViewModel @Inject constructor(
    private val fetchParentDataUseCase: FetchParentDataUseCase
) : ViewModel() {

    fun fetchParentData(parentId: String): Flow<Parent> {
        return fetchParentDataUseCase.invoke(parentId).map {
            it!!
        }
    }
}
