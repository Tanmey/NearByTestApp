package org.codejudge.application.remote

import io.reactivex.Observable
import org.codejudge.application.model.MyPlaces
import retrofit2.http.GET
import retrofit2.http.Url


interface RestApi {
    @GET
    fun getNearbyPlaces(@Url url: String): Observable<MyPlaces>
}