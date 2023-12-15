package com.example.lab3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab3.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val pubDate = intent.getStringExtra("pubDate")
        val sourceLink = intent.getStringExtra("link")

        if (title != null) binding.articleTitleLabel.text = title
        else binding.articleTitleLabel.text = getString(R.string.no_title)

        if (description != null) binding.articleDescription.text = description
        else binding.articleDescription.text = getString(R.string.no_description)

        if (pubDate != null) binding.articleDate.text = pubDate
        else binding.articleDate.text = getString(R.string.no_date)

        if (sourceLink == null) {
            binding.openSourceButton.isEnabled = false
        }

        binding.openSourceButton.setOnClickListener {
            val sourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceLink))
            startActivity(sourceIntent)
        }
    }
}