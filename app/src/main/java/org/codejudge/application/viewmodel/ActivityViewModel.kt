package org.codejudge.application.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.codejudge.application.model.MyPlaces
import org.codejudge.application.model.Restaurants
import org.codejudge.application.remote.RestApi
import org.codejudge.application.remote.RetrofitClient

class ActivityViewModel(application: Application) : BaseViewModel(application) {

    val restaurantsList = MutableLiveData<List<Restaurants>>()
    val restaurantsListLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val jsonApi: RestApi = RetrofitClient.instance
    private val myCompositeDisposable = CompositeDisposable()

    fun fetchData(url: String) {
        loading.value = true
        myCompositeDisposable?.add(
            jsonApi.getNearbyPlaces(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myPlaces -> displayData(myPlaces) },
                    { t: Throwable ->
                        Log.e("RxError : ", "RxError : " + t.message)
                        restaurantsListLoadError.value = true
                        loading.value = false
                    })
        )
    }

    private fun displayData(myPlaces: MyPlaces) {

        if (myPlaces.status.equals("OK")) {
            val restaurants: Array<Restaurants>? = myPlaces.results
            restaurantsList.value = restaurants!!.toList()
            restaurantsListLoadError.value = false
            loading.value = false

        } else {
            restaurantsListLoadError.value = true
            loading.value = false
        }
    }


    override fun onCleared() {
        super.onCleared()
        if (myCompositeDisposable != null) {
            myCompositeDisposable.clear()
        }
    }

}