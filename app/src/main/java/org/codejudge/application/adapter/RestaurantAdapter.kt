package org.codejudge.application.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_restaurant.view.*
import org.codejudge.application.R
import org.codejudge.application.model.Restaurants

class RestaurantAdapter(
    internal var context: Context,
    internal var restaurantList: List<Restaurants>
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant: Restaurants = restaurantList[position]
        holder.bindPhoto(restaurant)
    }

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var restaurant: Restaurants? = null

        init {
            itemView.ivIcon.setOnClickListener(this)
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun bindPhoto(restaurant: Restaurants) {
            this.restaurant = restaurant

            var isOpen = restaurant.opening_hours?.open_now ?: false

            itemView.tvName.text = restaurant.name.toString()
            itemView.tvLocation.text = restaurant.vicinity.toString()
            itemView.tvRating.text = restaurant.rating.toString()

            if (restaurant.rating >= 3) {
                itemView.tvRating.setBackground(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_rating_background_green
                    )
                )
            } else if (restaurant.rating >= 2) {
                itemView.tvRating.setBackground(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_rating_background_yellow
                    )
                )
            } else {
                itemView.tvRating.setBackground(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_rating_background_red
                    )
                )
            }

            if (isOpen) {
                itemView.tvOpeningHour.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.green
                    )
                )
                itemView.tvOpeningHour.text = itemView.context.getString(R.string.open)
            } else {
                itemView.tvOpeningHour.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )
                itemView.tvOpeningHour.text = itemView.context.getString(R.string.closed)
            }

            Glide.with(itemView.context)
                .load(restaurant.icon.toString())
                .placeholder(R.drawable.ic_restaurant_red) //placeholder
                .error(R.drawable.ic_rating_background_red) //error
                .into(itemView.ivIcon);
        }

        override fun onClick(v: View?) {
            Toast.makeText(
                itemView.context,
                (restaurant?.name ?: "Item") + " Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}