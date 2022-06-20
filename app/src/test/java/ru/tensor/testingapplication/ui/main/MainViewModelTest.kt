package ru.tensor.testingapplication.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.tensor.testingapplication.domain.Article
import ru.tensor.testingapplication.domain.ArticleRepository
import ru.tensor.testingapplication.ui.main.data.ArticleMapper
import ru.tensor.testingapplication.ui.main.data.ScreenState
import utils.TrampolineSchedulerRule

/**
 * Класс теста VM
 * Его экземпляр будет создаваться для каждого @Test метода (т.е нет смысла пытаться хранить промежуточное состояние)
 */
class MainViewModelTest {

    @get:Rule
    val rxRule = TrampolineSchedulerRule()


    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private val testArticle = Article(
        title = "title",
        subtitle = "subtitle",
        descriptions = "description",
        author = "author",
        views = 50,
        likes = 200
    )

    private val mockRepository: ArticleRepository = mock {
        on { getArticle() } doReturn Single.just(testArticle)
    }
    private val mockMapper: ArticleMapper = spy(ArticleMapper(mock()))
    private val mockStateObserver: Observer<ScreenState> = mock()
    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        /*
         * Spy. Использует реальный объект vm, тоже можно вызывать [verify]
         */
        vm = spy(MainViewModel(mockRepository, mockMapper)).apply {
            state.observeForever(mockStateObserver)
        }
    }

    @After
    fun tearDown() = Unit

    @Test
    fun `On vm init request data from repository`() {
        assertFalse(vm.state.value!!.isEmpty)
        assertEquals("title", vm.state.value!!.title)
        assertEquals("subtitle", vm.state.value!!.subtitle)
        assertEquals("description", vm.state.value!!.descriptions)
        assertEquals("author", vm.state.value!!.author)

        assertEquals("50", vm.state.value!!.views)
        assertEquals("200", vm.state.value!!.likes)
        verify(mockRepository, times(1)).getArticle()
    }

    @Test
    fun `On vm refresh, increase counter`(){
        assertEquals(0, vm.refreshCounts)
        vm.refresh()
        assertEquals(1, vm.refreshCounts)
    }

    @Test
    fun `On vm refresh, reset screen state`(){
        vm.refresh()
        verify(mockStateObserver).onChanged(ScreenState.getEmpty())
    }

    @Test
    fun `On vm refresh after reset, request data update`(){
        vm.refresh()
        verify(mockRepository, times(2)).getArticle()
    }

    @Test
    fun `On vm init, author of article is saved`(){
        assertFalse(vm.state.value!!.isEmpty)
        verify(mockRepository, times(1)).saveArticleAuthor(testArticle)
    }
}
