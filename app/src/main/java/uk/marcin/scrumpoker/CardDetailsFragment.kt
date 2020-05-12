package uk.marcin.scrumpoker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.*
import kotlinx.android.synthetic.main.fragment_card_details.*
import kotlinx.android.synthetic.main.fragment_card_details.view.*

data class CardDetailsState(val isShowingCard: Boolean = false, val selectedCard: String = "") : MvRxState {
    constructor(arg: String): this(selectedCard = arg)
}

class CardDetailsViewModel(initialState: CardDetailsState) : MvRxViewModel<CardDetailsState>(initialState){
    fun toggleVote(){
        setState {
            copy(isShowingCard = !isShowingCard)
        }
    }
}

class CardDetailsFragment : BaseMvRxFragment() {
    private val viewModel: CardDetailsViewModel by fragmentViewModel()
    private val selectedCard: String by args()

    override fun invalidate() = withState(viewModel) {
//        textView_selectedCard.isVisible = it.isShowingCard
        textView_selectedCard.text = if (it.isShowingCard) it.selectedCard else "\uD83C\uDCCF"
        button_showVote.text = if (it.isShowingCard) "Hide vote" else "Show vote"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.button_showVote.setOnClickListener {
            viewModel.toggleVote()
        }

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

//        editText_roomName.addTextChangedListener {
//            viewModel.updateRoomName(it.toString())
//        };
    }

    companion object {
        fun arg(selectedCard: String): Bundle{
            val args = Bundle()
            args.putString(MvRx.KEY_ARG, selectedCard)
            return args
        }
    }
}