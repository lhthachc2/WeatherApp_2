package com.example.myapplication

import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.CityModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity()  {

    lateinit var wordViewModel: WordViewModel
    lateinit var recyclerView1 : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView1 = findViewById(R.id.recycleviewhistory)
        val adapter1 = AdapterHistory()
        recyclerView1.adapter = adapter1
        recyclerView1.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView1.setLayoutManager(LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false))

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        wordViewModel.allWords.observe(this, Observer<List<CityModel>> {
            val list : MutableList<CityModel> = mutableListOf()
            for (i in 0..it.size-1)
            {
                list.add(0,it.get(i))
            }
            adapter1.setWords(list)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.option_menu,menu)
        val searchView : SearchView = menu!!.findItem(R.id.mnuSearch).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
               ReadJson().execute("http://api.worldweatheronline.com/premium/v1/search.ashx?format=json&key=6fe0e24c40354453beb152141202702&query="+query+"")
               return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private inner class ReadJson : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg strings: String): String {
            val content = StringBuilder()
            try {
                val url = URL(strings[0])
                val inputStreamReader = InputStreamReader(url.openConnection().getInputStream())
                val bufferedReader = BufferedReader(inputStreamReader)
                var line: String? = ""
                while ({ line = bufferedReader.readLine(); line }() != null) {
                    content.append(line)
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return content.toString()
        }

        override fun onPostExecute(s: String) {

            super.onPostExecute(s)
            var arraylist = ArrayList<CityModel>()
            try {
                val json = JSONObject(s)
                val jsonObject = json.getJSONObject("search_api")
                val jsonArray = jsonObject.getJSONArray("result")
                for (i in 0..jsonArray.length()-1) {
                    val JSONObject = jsonArray.getJSONObject(i)
                    val JSONArrayarea = JSONObject.getJSONArray("areaName")
                    for (j in 0..JSONArrayarea.length()-1 )
                    {
                        val JSONObjectvalue = JSONArrayarea.getJSONObject(j)
                        var value : String = JSONObjectvalue.getString("value")
                        arraylist.add(CityModel(value))
                    }
                }
                recyclerView1 = findViewById(R.id.recycleviewhistory)
                val adapter = Adapter(arraylist)
                recyclerView1.adapter = adapter
                recyclerView1.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView1.setLayoutManager(LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false))

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onRestart() {
        recyclerView1 = findViewById(R.id.recycleviewhistory)
        val adapter1 = AdapterHistory()
        recyclerView1.adapter = adapter1
        recyclerView1.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView1.setLayoutManager(LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false))

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        wordViewModel.allWords.observe(this, Observer<List<CityModel>> {
            val list : MutableList<CityModel> = mutableListOf()
            for (i in 0..it.size-1)
            {
                list.add(0,it.get(i))
            }
            adapter1.setWords(list)
        })
        super.onRestart()
    }
}
