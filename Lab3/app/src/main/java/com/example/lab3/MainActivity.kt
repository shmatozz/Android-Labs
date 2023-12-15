package com.example.lab3

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private val myApiKey = "pub_346062ac4e5d9e57120fd5f7519f6f65efab8"
    private val retrofit = RetrofitClient.getInstance()
    private val newsAPI = retrofit.create(NewsAPI::class.java)
    private var keyword = ""
    private var ru = false
    private var en = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("working", "start")

        if (currentNewsList.isEmpty()) {
            getNews()
        } else {
            outputNewsList()
        }

        val newsSearch = findViewById<FloatingActionButton>(R.id.find_news)

        newsSearch.setOnClickListener {
            openNewsFilterDialog()
        }
    }

    private fun openNewsFilterDialog() {
        val newsFilterDialog = Dialog(this)
        newsFilterDialog.setContentView(R.layout.dialog_filter_news)
        newsFilterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        newsFilterDialog.setCancelable(true)

        val searchButton: Button = newsFilterDialog.findViewById(R.id.search_button)
        val ruLang: CheckBox = newsFilterDialog.findViewById(R.id.ru_checkbox)
        val enLang: CheckBox = newsFilterDialog.findViewById(R.id.en_checkbox)
        val keyWordInput: EditText = newsFilterDialog.findViewById(R.id.key_word_input)

        searchButton.setOnClickListener {
            ru = ruLang.isChecked
            en = enLang.isChecked
            keyword = keyWordInput.text.toString()

            getNews()
            newsFilterDialog.dismiss()
            newsAdapter.clearNews()
            currentNewsList.clear()
        }

        newsFilterDialog.show()
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

    private fun getNews() {
        val call: Call<ResponseArticle> =
            if (keyword.isEmpty() && !en && !ru) {
                newsAPI.getAllNews(myApiKey)
            } else if (keyword.isNotEmpty() && !en && !ru) {
                newsAPI.getKeyNews(myApiKey, keyword)
            } else if (en && !ru) {
                if (keyword.isEmpty()) newsAPI.getAllLangNews(myApiKey,"en")
                else newsAPI.getKeyLangNews(myApiKey, keyword, "en")
            } else if (!en && ru) {
                if (keyword.isEmpty()) newsAPI.getAllLangNews(myApiKey, "ru")
                else newsAPI.getKeyLangNews(myApiKey, keyword, "ru")
            } else {
                if (keyword.isEmpty()) newsAPI.getAllLangNews(myApiKey, "ru,en")
                else newsAPI.getKeyLangNews(myApiKey, keyword, "ru,en")
            }

        call.enqueue(object : Callback<ResponseArticle> {
            override fun onResponse(
                call: Call<ResponseArticle>,
                response: Response<ResponseArticle>
            ) {
                if (response.isSuccessful) {
                    val newsList = response.body()
                    newsList?.let {
                        for (i in 0 until minOf(10, it.results.size)) {
                            val news = it.results[i]
                            currentNewsList.add(Article(news.title,
                                                        news.link,
                                                        news.description,
                                                        news.pubDate))
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
        val intent = Intent(this, ArticleActivity::class.java).apply {
            putExtra("title", article.title)
            putExtra("link", article.link)
            putExtra("description", article.description)
            putExtra("pubDate", article.pubDate)
        }
        startActivity(intent)
    }

    override fun onLongClick(article: Article) {
        val sourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
        startActivity(sourceIntent)
    }
}