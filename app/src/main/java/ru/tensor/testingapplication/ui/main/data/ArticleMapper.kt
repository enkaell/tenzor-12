package ru.tensor.testingapplication.ui.main.data

import android.content.Context
import ru.tensor.testingapplication.domain.Article

class ArticleMapper(private val appContext: Context) {

    fun apply(article: Article): ScreenState = ScreenState(
        article.title,
        article.subtitle,
        article.descriptions,
        article.author,
        article.views.toString(),
        article.likes.toString()
    )
}