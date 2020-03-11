package com.example.myapplication

import androidx.lifecycle.LiveData
import com.example.myapplication.Database.CityModel
import com.example.myapplication.Database.WordDao

class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<CityModel>> = wordDao.getAlphabetizedWords()
    val getone : CityModel = wordDao.getone()

    suspend fun insert(model: CityModel) {
        wordDao.insert(model)
    }
    fun delete(model: CityModel){
        wordDao.delete(model)
    }
    fun countRow() : Int
    {
        var count: Int = wordDao.countRow()
        return count
    }
    fun getCityModel(cityModel : String) : Boolean
    {
       val cityModel = wordDao.getcityModel(cityModel)
        if (cityModel == null) // Không có
            return true
        else // Có
            return false
    }
}