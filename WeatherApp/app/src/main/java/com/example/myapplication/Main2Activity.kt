package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Database.CityModel
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

class Main2Activity() : AppCompatActivity(), Parcelable {

    lateinit var temp_C : TextView
    lateinit var temp_F : TextView
    lateinit var hudimy : TextView
    lateinit var weatherDesc : TextView
    lateinit var image : ImageView
    lateinit var url : TextView

    lateinit var cityModel: CityModel
    lateinit var cityModelHistory: CityModel

    private lateinit var wordViewModel: WordViewModel

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val Intent = getIntent()

        temp_C = findViewById(R.id.temp_C)
        temp_F = findViewById(R.id.temp_F)
        hudimy = findViewById(R.id.humidity)
        weatherDesc = findViewById(R.id.weatherDesc)
        url = findViewById(R.id.url)
        image = findViewById(R.id.image)

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        try
        {
            cityModel  = Intent.getParcelableExtra<CityModel>("weather")
            var count : Int = wordViewModel.countRow()
            var query : String = cityModel.City.toString()
            if (count<3)
            {
                wordViewModel.insert(cityModel)
            }
            else
            {
                val isValid = wordViewModel.getCityModel(cityModel.City.toString())
                if (isValid==false)
                {
                    wordViewModel.delete(cityModel)
                    wordViewModel.insert(cityModel)

                }
                else
                {
                    wordViewModel.delete(wordViewModel.getone)
                    wordViewModel.insert(cityModel)

                }
            }
            ReadJson().execute("http://api.worldweatheronline.com/premium/v1/weather.ashx?format=json&key=6fe0e24c40354453beb152141202702&query="+query+"")
        }
        catch (e : Exception)
        {
            cityModelHistory = Intent.getParcelableExtra<CityModel>("weather1")
            var queryHistory : String = cityModelHistory.City.toString()
            wordViewModel.delete(cityModelHistory)
            wordViewModel.insert(cityModelHistory)
            ReadJson().execute("http://api.worldweatheronline.com/premium/v1/weather.ashx?format=json&key=6fe0e24c40354453beb152141202702&query="+queryHistory+"")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Main2Activity> {
        override fun createFromParcel(parcel: Parcel): Main2Activity {
            return Main2Activity(parcel)
        }

        override fun newArray(size: Int): Array<Main2Activity?> {
            return arrayOfNulls(size)
        }
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
            try {
                val json = JSONObject(s)
                val jsonObject = json.getJSONObject("data")
                val jsonArray = jsonObject.getJSONArray("current_condition")
                for (i in 0..jsonArray.length()-1) {
                    val JSONObject = jsonArray.getJSONObject(i)
                    temp_C.setText(JSONObject.getString("temp_C").toString())
                    temp_F.setText(JSONObject.getString("temp_F").toString())
                    val JSONArrayweatherDes = JSONObject.getJSONArray("weatherDesc")
                    for (j in 0.. JSONArrayweatherDes.length()-1)
                    {
                        val JSONObjectDes = JSONArrayweatherDes.getJSONObject(j)
                        weatherDesc.setText(JSONObjectDes.getString("value"))
                    }
                    val JSONArrayIconUrl = JSONObject.getJSONArray("weatherIconUrl")
                    for (k in 0.. JSONArrayIconUrl.length()-1)
                    {
                        val JSONObjectvalue = JSONArrayIconUrl.getJSONObject(k)
                        var value : String = JSONObjectvalue.getString("value")
                        url.setText(value)
                        Picasso.get().load(value).into(image)
                    }
                    hudimy.setText(JSONObject.getString("humidity"))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
