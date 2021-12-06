package com.app.lunabee.Utils.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.lunabee.Models.User
import com.app.lunabee.R
import com.app.lunabee.ViewUserFragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView


class MainRecyclerAdapter(
    private val context: Context,
    var userList: MutableList<User>,
    val onLoadMoreItems: OnLoadMoreItemsListener
): RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {


    companion object{
        private const val TAG = "MainRecyclerAdapter"
    }
    interface OnLoadMoreItemsListener {
        fun onLoadMoreItems()
        fun openFragment(user: User)
    }

    private fun reachedEndOfList(position: Int): Boolean {
        return position == itemCount - 1
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onCreateViewHolder(view: ViewGroup, position: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout_item_user, view, false)
        )

    }


    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (reachedEndOfList(holder.adapterPosition)) {
            Log.d(TAG, "onViewAttachedToWindow: position: ${holder.adapterPosition}")
            onLoadMoreItems.onLoadMoreItems()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.user = userList[position]
        Glide
            .with(context)
            .load(holder.user?.avatar)
            .centerCrop()
            .into(holder.profilePhoto)

        holder.displayName.text = "${holder.user?.first_name} ${holder.user?.last_name}"

    }




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var user: User?= null
        val displayName = itemView.findViewById<TextView>(R.id.display_name)
        val profilePhoto = itemView.findViewById<ShapeableImageView>(R.id.profile_photo)
        val userCard = itemView.findViewById<CardView>(R.id.user_card)

        init {
            userCard.setOnClickListener {
                onLoadMoreItems.openFragment(user!!)


            }
        }


    }
}