package com.app.lunabee

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.lunabee.Models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ViewUserFragment : Fragment() {

    private lateinit var mEmail : TextView
    private lateinit var mFirstName : TextView
    private lateinit var mLastName : TextView
    private lateinit var mDisplayName : TextView
    private lateinit var mProfilePhoto : ShapeableImageView

    private lateinit var btnBack : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: started")
        val view = inflater.inflate(R.layout.fragment_view_user, container, false)
        mEmail = view.findViewById(R.id.email)
        mFirstName = view.findViewById(R.id.firstName)
        mLastName = view.findViewById(R.id.lastName)
        mDisplayName = view.findViewById(R.id.display_name)
        mProfilePhoto = view.findViewById(R.id.profile_photo)
        btnBack = view.findViewById(R.id.btn_back)
        val bundle = this.arguments
        var user = User()
        if (bundle != null) {
            user = bundle.getParcelable("EXTRA_USER")!!
        }
        setupWidgets(user)

        return view

    }


    @SuppressLint("SetTextI18n")
    private fun setupWidgets(user: User){
        mEmail.text = user.email
        mFirstName.text = user.first_name
        mLastName.text = user.last_name
        mDisplayName.text = user.first_name + " "+ user.last_name
        Glide
            .with(this)
            .load(user.avatar)
            .centerCrop()
            .into(mProfilePhoto)

        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
        }
    }
    companion object{
        private const val TAG = "ViewUserFragment"
    }
}