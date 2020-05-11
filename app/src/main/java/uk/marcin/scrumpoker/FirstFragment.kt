package uk.marcin.scrumpoker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseMvRxFragment() {
    private val viewModel: MyViewModel by fragmentViewModel()

    override fun invalidate() = withState(viewModel) { state ->
//        loadingView.isVisible = state.listing is Loading
        editText_roomName.setText(state.roomName);
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}

//class MyViewModel(initialState: MyState) : BaseMvRxViewModel(initialState, debugMode = true)
abstract class MvRxViewModel<S : MvRxState>(initialState: S) : BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG)

data class MyState(val roomName: String) : MvRxState
//
class MyViewModel(val initialState: MyState) : MvRxViewModel<MyState>(initialState) {
    // TODO: Load last used room on init
}

