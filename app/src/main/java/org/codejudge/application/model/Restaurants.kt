package org.codejudge.application.model

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Restaurants : Serializable {

    @NonNull
    var r_id: Int = 0

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("icon")
    var icon: String? = ""

    var geometry: Geometry? = null

    val photos: Array<Photos>? = null

    @SerializedName("id")
    var id: String? = ""

    @SerializedName("place_id")
    var place_id: String? = ""

    @SerializedName("price_level")
    var price_level: Int = 0

    @SerializedName("rating")
    var rating: Double = 0.0

    @SerializedName("reference")
    var reference: String? = ""

    @SerializedName("scope")
    var scope: String? = ""

    @SerializedName("vicinity")
    var vicinity: String? = ""

    var opening_hours: OpeningHours? = null

    @SerializedName("types")
    var types: Array<String>? = null

}
