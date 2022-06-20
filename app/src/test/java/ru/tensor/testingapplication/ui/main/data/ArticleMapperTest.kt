package ru.tensor.testingapplication.ui.main.data

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.tensor.testingapplication.domain.Article

class ArticleMapperTest {


    private val mapper = ArticleMapper(mock())

    @Test
    fun `Correct title transformation`() {
        val result = mapper.apply(Article("TestTitle"))
        assertEquals("TestTitle", result.title)
    }

    @Test
    fun `Correct subtitle transformation`() {
        val result = mapper.apply(Article(subtitle = "TestSubTitle"))
        assertEquals("TestSubTitle", result.subtitle)
    }

    @Test
    fun `Correct likes and views transformation`() {
        val result = mapper.apply(Article(likes = 505, views = 1001))
        assertEquals("505", result.likes)
        assertEquals("1001", result.views)
    }
}