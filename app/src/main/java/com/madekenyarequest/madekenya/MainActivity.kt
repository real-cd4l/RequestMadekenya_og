package com.madekenyarequest.madekenya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.madekenyarequest.madekenya.adapter.RequestSummaryAdapter
import com.madekenyarequest.madekenya.pages.auth.ChangePasswordActivity
import com.madekenyarequest.madekenya.pages.auth.LoginActivity
import com.madekenyarequest.madekenya.pojos.Request
import com.madekenyarequest.madekenya.pojos.Subscriber
import com.madekenyarequest.madekenya.utilities.LocalDb
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var subscriber: Subscriber? = null
    private val TAG = "MainActivity"
    lateinit var adapter: RequestSummaryAdapter
    private var requests: ArrayList<Request> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar)
        setContentView(R.layout.activity_main)
        subscriber = LocalDb.getSavedCustomer(applicationContext)
        if (subscriber != null) {
            toolBar.title = subscriber?.username
            adapter = RequestSummaryAdapter(this@MainActivity, requests)
            toolBar.menu.findItem(R.id.verifiedPrompt).isVisible = subscriber!!.isVerified

            checkIfUserIsVerified()
            recyclerView.adapter = adapter
            if (subscriber != null && subscriber?.id != null) {
                fetchAllRequests(subscriber?.id!!)
            } else {
                Log.d(TAG, "onCreate: subScriber ==> " + subscriber.toString())
                Toast.makeText(this@MainActivity, "null founded...!!", Toast.LENGTH_LONG).show()
            }

        } else {
            val intent: Intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        setSupportActionBar(toolBar)

        mbRequest.setOnClickListener {
            val intent: Intent = Intent(this@MainActivity, ViewRequestActivity::class.java)
            intent.putExtra("subscriber", subscriber)
            startActivity(intent)

        }
    }

    private fun checkIfUserIsVerified() {
        FirebaseDatabase.getInstance().getReference("madekenyarequest").child("subscriber").child(subscriber!!.id).child("verified").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val verified: Boolean? = snapshot.value as Boolean?
                    if (verified != null) {
                        toolBar.menu.findItem(R.id.verifiedPrompt).isVisible = verified
                        subscriber!!.isVerified = verified
                        LocalDb.saveCustomerLocal(applicationContext, subscriber)
                    }

                }else{
                    toolBar.menu.findItem(R.id.verifiedPrompt).isVisible = false
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun fetchAllRequests(id: String) {
        requests.clear()
        adapter.notifyDataSetChanged()
        progressBar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("madekenyarequest").child("requests").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    tvNoRequest.visibility = View.GONE
                    requests.clear()
                    adapter.notifyDataSetChanged()

                    for (child in snapshot.children) {
                        val request: Request? = child.getValue(Request::class.java)
                        if (request != null) {
                            requests.add(0, request)
                            adapter.notifyItemInserted(0)
                        }
                    }
                } else {
                    tvNoRequest.visibility = View.VISIBLE
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.verifiedPrompt) {
            showVerifiedMessage()
        } else if (item.itemId == R.id.logout) {
            logout()
        } else if (item.itemId == R.id.changePassword) {
            changePassword()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changePassword() {
        if (subscriber != null) {
            val intent: Intent = Intent(this@MainActivity, ChangePasswordActivity::class.java)
            intent.putExtra("subscriber", subscriber!!)
            startActivity(intent)
        }


    }

    private fun logout() {
        LocalDb.saveCustomerLocal(applicationContext, null)
        val intent: Intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun showVerifiedMessage() {
        MaterialAlertDialogBuilder(this@MainActivity, R.style.AlertDialogTheme).setMessage(subscriber!!.username + " !, wewe ni mteja wetu na tunakufahamu, tuko pamoja. tuko kazini.").setPositiveButton("Sawa",null).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}