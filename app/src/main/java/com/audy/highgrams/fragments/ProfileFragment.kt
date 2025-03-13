package com.audy.highgrams.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audy.highgrams.AccountSettingsActivity
import com.audy.highgrams.Model.User
import com.audy.highgrams.R
import com.audy.highgrams.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        // Get profileId from SharedPreferences
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        profileId = pref?.getString("profileId", firebaseUser.uid) ?: firebaseUser.uid

        // Set button text based on profile being viewed
        if (profileId == firebaseUser.uid) {
            binding.editAccountSettingsBtn.text = "Edit Profile"
        } else {
            checkFollowAndFollowingButtonStatus()
        }

        // Set up button click listener
        binding.editAccountSettingsBtn.setOnClickListener {
            handleButtonClick()
        }

        getFollowers()
        getFollowings()
        userInfo()

        return binding.root
    }

    private fun handleButtonClick() {
        val buttonText = binding.editAccountSettingsBtn.text.toString()

        when {
            buttonText == "Edit Profile" -> {
                startActivity(Intent(context, AccountSettingsActivity::class.java))
            }
            buttonText == "Follow" -> {
                followUser()
            }
            buttonText == "Following" -> {
                unfollowUser()
            }
        }
    }

    private fun followUser() {
        firebaseUser.uid.let { currentUserId ->
            // Follow the user
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(currentUserId)
                .child("Following").child(profileId)
                .setValue(true)

            // Add the user to the follower's list
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Followers").child(currentUserId)
                .setValue(true)

            // Optionally update the UI to reflect the change
            binding.editAccountSettingsBtn.text = "Following"
        }
    }

    private fun unfollowUser() {
        firebaseUser.uid.let { currentUserId ->
            // Unfollow the user
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(currentUserId)
                .child("Following").child(profileId)
                .removeValue()

            // Remove from the follower's list
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(profileId)
                .child("Followers").child(currentUserId)
                .removeValue()

            // Optionally update the UI to reflect the change
            binding.editAccountSettingsBtn.text = "Follow"
        }
    }

    private fun checkFollowAndFollowingButtonStatus() {
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(firebaseUser.uid)
            .child("Following")

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(profileId).exists()) {
                    binding.editAccountSettingsBtn.text = "Following"
                } else {
                    binding.editAccountSettingsBtn.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileFragment", "Error checking follow status: ${error.message}")
            }
        })
    }

    private fun getFollowers() {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId).child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.totalFollowers.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileFragment", "Error fetching followers: ${error.message}")
            }
        })
    }

    private fun getFollowings() {
        val followingsRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId).child("Following")

        followingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.totalFollowing.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileFragment", "Error fetching followings: ${error.message}")
            }
        })
    }

    private fun userInfo() {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users").child(profileId)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user?.getImage()).placeholder(R.drawable.profile).into(binding.proImageProfileFrag)
                    binding.profileFragmentUsername.text = user?.getUsername()
                    binding.fullNameProfileFrag.text = user?.getFullname()
                    binding.bioProfileFrag.text = user?.getBio()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileFragment", "Error fetching user info: ${error.message}")
            }
        })
    }

    private fun saveProfileId() {
        context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()?.apply {
            putString("profileId", profileId)
            apply()
        }
    }

    override fun onStop() {
        super.onStop()
        saveProfileId()
    }

    override fun onPause() {
        super.onPause()
        saveProfileId()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveProfileId()
    }
}
