package com.example.user_profile_registration.View

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user_profile_registration.R
import com.example.user_profile_registration.adapter.ProfileAdapter
import com.example.user_profile_registration.model.UserProfile
import com.example.user_profile_registration.viewModel.UserProfileViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileListActivity : AppCompatActivity() {

    private lateinit var profileViewModel: UserProfileViewModel
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_list)

        profileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.profileRecyclerView)
        profileAdapter = ProfileAdapter()

        recyclerView.adapter = profileAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        profileViewModel.getUserProfiles().observe(this, Observer { profiles ->
            if (profiles.isEmpty()) {

                Toast.makeText(this, "No profiles available", Toast.LENGTH_SHORT).show()
            } else {
                profileAdapter.submitList(profiles)
            }
        })


        profileAdapter.setOnItemClickListener { userProfile ->
            val intent = Intent(this@ProfileListActivity, SingleProfileActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            startActivity(intent)
        }


        profileAdapter.setOnDeleteClickListener { userProfile ->
            showDeleteConfirmationDialog(userProfile)
        }


        profileAdapter.setOnUpdateClickListener { userProfile ->
            val intent = Intent(this@ProfileListActivity, UpdateProfileActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            startActivity(intent)
        }


        findViewById<FloatingActionButton>(R.id.addProfileBtn).setOnClickListener {
            val intent = Intent(this@ProfileListActivity, AddProfileActivity::class.java)
            startActivity(intent)
        }
    }


    private fun showDeleteConfirmationDialog(userProfile: UserProfile) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Profile")
        builder.setMessage("Are you sure you want to delete this profile?")

        builder.setPositiveButton("Yes") { dialog, which ->
            profileViewModel.deleteUserProfile(userProfile)
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}

