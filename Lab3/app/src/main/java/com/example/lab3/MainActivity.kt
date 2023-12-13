package com.example.lab3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NewsAdapter.Listener {

    private val currentNewsList: MutableList<Article> = mutableListOf()
    private val newsAdapter = NewsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("working", "start")

        val retrofit = RetrofitClient.getInstance()
        val newsAPI = retrofit.create(NewsAPI::class.java)

        val call = newsAPI.getAllNews("pub_346062ac4e5d9e57120fd5f7519f6f65efab8")

        call.enqueue(object : Callback<ResponseArticle> {
            override fun onResponse(
                call: Call<ResponseArticle>,
                response: Response<ResponseArticle>
            ) {
                if (response.isSuccessful) {
                    val newsList = response.body()
                    newsList?.let {
                        for (i in 0..9) {
                            val news = it.results[i]
                            currentNewsList.add(Article(news.title, news.link))
                        }
                    }
                    outputNewsList()
                } else {
                    Log.d("working", "response code failure ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseArticle>, t: Throwable) {
                Log.d("working", "onFailure call")
                throw t
            }
        })
    }

    private fun outputNewsList() {
        val newsRV = findViewById<RecyclerView>(R.id.list_of_news)
        newsRV.adapter = newsAdapter
        newsRV.layoutManager = LinearLayoutManager(this)

        Log.d("working", currentNewsList.size.toString())
        for (article in currentNewsList) {
            newsAdapter.addArticle(article)
        }
    }

    object RetrofitClient {
        fun getInstance(): Retrofit {
            val mHttpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val mOkHttpClient =
                OkHttpClient.Builder().addInterceptor(mHttpLoggingInterceptor).build()

            return Retrofit.Builder()
                .baseUrl("https://newsdata.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build()
        }
    }

    override fun onClick(article: Article) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(article: Article) {
        TODO("Not yet implemented")
    }
}