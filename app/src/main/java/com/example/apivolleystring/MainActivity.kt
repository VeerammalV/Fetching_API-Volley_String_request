package com.example.apivolleystring

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apivolleystring.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private var recyclerViewAdapter: DataAdapter? = null
    private lateinit var setText: TextView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recycler_view)
        setText = findViewById(R.id.set_text)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Fetch data from the new URL
        val newUrl = "https://jsonplaceholder.typicode.com/posts"
        val queue = Volley.newRequestQueue(this)
        val newStringRequest = StringRequest(
            Request.Method.GET, newUrl,
            { response ->
                val dataList = parseData(response)
                setupRecyclerView(dataList)
            },
            { setText.text = "Failed to fetch data" })

        queue.add(newStringRequest)
    }

    private fun parseData(response: String?): List<String> {
        val jsonArray = JSONArray(response)
        val dataList = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getString(i)
            dataList.add(item)
        }
        return dataList
    }

    private fun setupRecyclerView(dataList: List<String>) {
        recyclerViewAdapter = DataAdapter(dataList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recyclerViewAdapter
        }
    }
}