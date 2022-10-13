package com.ugurinci.yemeksepetifinal.ui.fooddetail

import androidx.lifecycle.*
import com.ugurinci.yemeksepetifinal.data.remote.FoodItem
import com.ugurinci.yemeksepetifinal.data.repository.FoodDetailRepository
import com.ugurinci.yemeksepetifinal.utils.Cart
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class FoodDetailViewModel @AssistedInject constructor(
    @Assisted val foodId: String,
    private val foodDetailRepository: FoodDetailRepository
) : ViewModel() {

    private var _food = MutableLiveData<FoodItem>()

    val food: LiveData<FoodItem> = _food

    init {
        fetchFood()
    }

    private fun fetchFood() = viewModelScope.launch {
        foodDetailRepository.fetchFoodById(foodId).collect {
            if (it != null) {
                _food.value = it.body()
            }
        }
    }

    fun addCart() {
        Cart.foodList.add(food.value!!)
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(foodId: String): FoodDetailViewModel
    }

    companion object {
        fun provideFactory(
            viewModelFactory: ViewModelFactory,
            foodId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create(foodId) as T
            }
        }
    }
}