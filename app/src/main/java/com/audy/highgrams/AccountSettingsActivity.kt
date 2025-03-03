package com.audy.highgrams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.audy.highgrams.databinding.ActivityAccountSettingsBinding

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Access the CircleImageView using View Binding
        val profileImageView = binding.profileImageViewProfileFrag

        // Set a new image (example)
        profileImageView.setImageResource(R.drawable.profile)

        // Or load an image from a URL (using a library like Glide or Picasso)
        // Glide.with(this).load("https://example.com/profile.jpg").into(profileImageView)
    }
}
