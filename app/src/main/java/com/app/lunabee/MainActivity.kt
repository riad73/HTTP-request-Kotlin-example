package com.app.lunabee

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lunabee.Models.User
import com.app.lunabee.Utils.Adapters.MainRecyclerAdapter
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() , MainRecyclerAdapter.OnLoadMoreItemsListener{


    private lateinit var mUserRecycler : RecyclerView
    private var mLayoutManager: RecyclerView.LayoutManager ?= null
    private var mAdapter: MainRecyclerAdapter?= null
    private val client = OkHttpClient()
    private var users : MutableList<User>?= null
    private var currentPage = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: started.")
        mUserRecycler = findViewById(R.id.main_recycler)

        searchUsers()

    }

    /**
     * Recherche les 10 premiers utilisateurs
     */
    private fun searchUsers(){
        currentPage = 1
        val request = Request.Builder()
            .url("http://server.lunabee.studio:11111/techtest/users?page=1&pageSize=10")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: warning")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                users = Gson().fromJson(body, Array<User>::class.java).toMutableList()
                this@MainActivity.runOnUiThread {
                    setAdapter()

                }

            }
        })
    }

    override fun openFragment(user: User){
        val fragment = ViewUserFragment()
        val args = Bundle()
        args.putParcelable("EXTRA_USER", user)
        fragment.arguments = args
        this.supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentHolder, fragment, "LUNABEE")
            .commit()
    }

    /**
     * Initialisation du RecyclerView
     */
    private fun setAdapter(){
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mUserRecycler.layoutManager = mLayoutManager
        if (mAdapter == null){
            mAdapter = MainRecyclerAdapter(this, users!!, this)
            mUserRecycler.adapter = mAdapter
        } else{
            mAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * Pagination 10 items par 10 items
     */

    override fun onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: add new items from page $currentPage")
        //Nouvelle page
        currentPage++
        val urlCurrentPage =
            "http://server.lunabee.studio:11111/techtest/users?page=$currentPage&pageSize=10"
        val request = Request.Builder()
            .url(urlCurrentPage)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: warning")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val newUsers = Gson().fromJson(body, Array<User>::class.java).toMutableList()
                users?.plusAssign(newUsers)
                Log.d(TAG, "onResponse: users: ${users?.size}")
                this@MainActivity.runOnUiThread {
                    mUserRecycler.post(Runnable {
                        kotlin.run {
                            mAdapter!!.notifyDataSetChanged()
                        }
                    })
                }

            }
        })
    }


    companion object{
        private const val TAG = "MainActivity"
    }
}