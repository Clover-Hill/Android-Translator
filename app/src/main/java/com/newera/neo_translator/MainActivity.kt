package com.newera.neo_translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.newera.neo_translator.data.WordItem
import com.newera.neo_translator.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private var wordList : MutableList<WordItem> = ArrayList()
    private lateinit var wordListAdapter: WordListAdapter
    private val TAG = "Maxime"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordListAdapter = WordListAdapter()
        binding.recyclerview.apply {
            adapter = wordListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.searchview.apply {

            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String): Boolean
                {
                    lifecycleScope.launchWhenCreated {
                        binding.progressbar.isVisible = true

                        val response = try {
                            val salt = System.currentTimeMillis().toString()
                            val curtime = (System.currentTimeMillis()/1000).toString()
                            val signStr = Constants.appKey + truncate(query) + salt + curtime + Constants.appPassword

                            RetrofitInstance.api.getTranslate(query,salt,sha256(signStr),curtime)
                        } catch(e: IOException) {

                            Toast.makeText(context,"IOException",Toast.LENGTH_SHORT).show()
                            return@launchWhenCreated

                        } catch(e: HttpException) {

                            Toast.makeText(context,"HttpException",Toast.LENGTH_SHORT).show()
                            return@launchWhenCreated
                        }

                        if(response.isSuccessful && response.body() != null)
                        {
                            val word = response.body()!!
                            
                            Log.i(TAG,word.query)
                            
                            val newList = arrayListOf(word)
                            wordList.forEach {newList.add(it)}
                            wordListAdapter.submitList(newList)
                            wordList.add(word)
                        }
                        else
                        {
                            Toast.makeText(context,"Response not successful",Toast.LENGTH_SHORT).show()
                        }

                        binding.progressbar.isVisible = false
                    }
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean
                {
                    return false
                }
            })
        }
    }

    private fun sha256(Str : String): String
    {
        val bytes = Str.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun truncate(Str : String): String
    {
        val len = Str.length
        return if(len<=20) {
            Str
        } else {
            Str.substring(0,10)+len.toString()+Str.substring(len-10,len)
        }
    }
}