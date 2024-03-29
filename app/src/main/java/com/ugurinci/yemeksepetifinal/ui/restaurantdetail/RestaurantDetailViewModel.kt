package com.ugurinci.yemeksepetifinal.ui.restaurantdetail

import androidx.lifecycle.*
import com.ugurinci.yemeksepetifinal.data.remote.FoodItem
import com.ugurinci.yemeksepetifinal.data.remote.RestaurantItem
import com.ugurinci.yemeksepetifinal.data.repository.RestaurantDetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class RestaurantDetailViewModel @AssistedInject constructor(
    @Assisted val restaurantId: String,
    private val restaurantDetailRepository: RestaurantDetailRepository
) : ViewModel() {

    private var _restaurant = MutableLiveData<RestaurantItem>()

    val restaurant: LiveData<RestaurantItem> = _restaurant

    private var _foods = MutableLiveData<List<FoodItem>>()

    val foods: LiveData<List<FoodItem>> = _foods

    init {
        fetchRestaurant()
        fetchFoods()
    }

    private fun fetchRestaurant() = viewModelScope.launch {
        restaurantDetailRepository.fetchRestaurantsById(restaurantId).collect {
            if (it != null) {
                _restaurant.value = it.body()
            }
        }
    }

    private fun fetchFoods() = viewModelScope.launch {
        restaurantDetailRepository.fetchFoods().collect {
            if (it != null) {
                _foods.value = it.body()!!.filter { foodItem ->
                    foodItem.restaurantId == restaurantId
                }
            }
        }
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(restaurantId: String): RestaurantDetailViewModel
    }

    companion object {
        fun provideFactory(
            viewModelFactory: ViewModelFactory,
            restaurantId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create(restaurantId) as T
            }
        }
    }
}