package org.codejudge.application

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import org.codejudge.application.adapter.RestaurantAdapter
import org.codejudge.application.util.Constants.Companion.MY_PERMISSION_CODE
import org.codejudge.application.util.Util
import org.codejudge.application.util.Util.Companion.getUrl
import org.codejudge.application.util.Util.Companion.getUrlForSearch
import org.codejudge.application.viewmodel.ActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ActivityViewModel
    private lateinit var mLastLocation: Location

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)

        //===========View======================
        listError.visibility = View.GONE
        recycler_retaurants.setHasFixedSize(true)
        recycler_retaurants.layoutManager = LinearLayoutManager(this)
        //=====================================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                accessLocation()
            }
        } else {
            accessLocation()
        }

        observeViewModel()

    }

    private fun accessLocation() {
        buildLocationRequest()
        buildLocationCallback()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

    }


    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0!!.locations.size - 1) //Get last location


                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude


                val url = getUrl(latitude, longitude)

                if (Util.isOnline(this@MainActivity)) {
                    viewModel.fetchData(url)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.you_are_offline),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 500000
        locationRequest.fastestInterval = 300000
        locationRequest.smallestDisplacement = 10f
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermission()) {

                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )

                        }
                } else {
                    Toast.makeText(
                        this,
                        this.getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onStop() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onStop()
    }

    fun observeViewModel() {
        viewModel.restaurantsList.observe(this, Observer { restaurantsList ->
            restaurantsList?.let {
                recycler_retaurants.visibility = View.VISIBLE

                val adapter = RestaurantAdapter(this, restaurantsList)
                recycler_retaurants.adapter = adapter


            }

        })

        viewModel.restaurantsListLoadError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    recycler_retaurants.visibility = View.GONE
                }
            }
        })

        edit_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val url = getUrlForSearch(latitude, longitude, s.toString())

                if (Util.isOnline(this@MainActivity)) {
                    viewModel.fetchData(url)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.you_are_offline),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        })

    }


}
