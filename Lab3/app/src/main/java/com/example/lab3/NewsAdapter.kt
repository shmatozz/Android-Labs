package com.example.lab3

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.CardArticleBinding

class NewsAdapter(private val listener: Listener): RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    private val news = ArrayList<Article>()

    class NewsHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = CardArticleBinding.bind(item)
        fun bind(article: Article, listener: Listener) = with(binding) {
            article.title?.let { Log.d("working", it) }
            articleTitle.text = article.title

            itemView.setOnClickListener {
                listener.onClick(article)
            }

            itemView.setOnLongClickListener {
                listener.onLongClick(article)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_article, parent, false)

        return NewsHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(news[position], listener)
    }

    fun addArticle(article: Article) {
        news.add(article)
        notifyDataSetChanged()
    }

    fun clearNews() {
        news.clear()
        notifyDataSetChanged()
    }


    interface Listener {
        fun onClick(article: Article)
        fun onLongClick(article: Article)
    }
}