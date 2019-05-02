package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.gridstone.trainingkotlin.PokemonListState.Content
import au.com.gridstone.trainingkotlin.PokemonListState.Loading
import au.com.gridstone.trainingkotlin.PokemonListState.Error
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.IllegalArgumentException

sealed class PokemonListState {
  object Loading : PokemonListState()
  data class Content(val list: List<PokemonSummary>) : PokemonListState()
  data class Error(val message: String? = "Unknown Error") : PokemonListState()
}

class ListController : Controller() {

  private lateinit var swipeRefreshLayout: SwipeRefreshLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    val view = inflater.inflate(R.layout.view_list, container, false)
    router.activity?.applicationContext?.let { context ->
      val viewAdapter = MyRecyclerViewAdapter()
      viewAdapter.set(ArrayList())
      viewAdapter.tapHandler = { id ->
        val transaction: RouterTransaction = RouterTransaction.with(DetailsController(id))
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())
        router.pushController(transaction)
      }

      val viewManager = LinearLayoutManager(context)

      view.findViewById<RecyclerView>(R.id.my_recycler_view)
          ?.let { recyclerView ->
            print(recyclerView.layoutManager)
            print(recyclerView.adapter)
            recyclerView.apply {
              setHasFixedSize(true)
              layoutManager = viewManager
              adapter = viewAdapter
            }
          }
    }

    swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    swipeRefreshLayout.setOnRefreshListener {
      loadData()
    }

    return view
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    loadData()
  }

  override fun onDetach(view: View) {
    super.onDetach(view)
  }

  private fun loadData() {

    APIManager.listObservable.subscribe(object : Observer<PokemonListState> {
      override fun onSubscribe(d: Disposable) {}

      override fun onNext(t: PokemonListState) {
        (view as ListView).display(t)
      }

      override fun onError(e: Throwable) {
        (view as ListView).display(PokemonListState.Error(e.message))
      }

      override fun onComplete() {}
    })
  }
}