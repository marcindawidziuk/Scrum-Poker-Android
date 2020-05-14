package uk.marcin.scrumpoker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import kotlinx.android.synthetic.main.fragment_landing.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseMvRxFragment() {
    private val viewModel: MyViewModel by activityViewModel()

    override fun invalidate(): Unit = withState(viewModel) { state ->
//        loadingView.isVisible = state.listing is Loadingc
        editText_roomName.setTextIfDifferent(state.roomName)
        editText_userName.setTextIfDifferent(state.userName)
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        editText_roomName.addTextChangedListener {
            viewModel.updateRoomName(it.toString())
//            editText_roomName.setTextIfDifferent(it)
        };
    }
}

//class MyViewModel(initialState: MyState) : BaseMvRxViewModel(initialState, debugMode = true)
abstract class MvRxViewModel<S : MvRxState>(val initialState: S) : BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG)

data class MyState(val userName: String = "Marcin", val roomName: String = "") : MvRxState
//
class MyViewModel(initialState: MyState) : MvRxViewModel<MyState>(initialState) {
    fun updateRoomName(toString: String) {
        setState {
            initialState.copy(roomName = toString)
        }
    }
    // TODO: Load last used room on init
}

