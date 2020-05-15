package uk.marcin.scrumpoker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import kotlinx.android.synthetic.main.fragment_card_details.*

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
        textView_selectedCard.text = if (it.isShowingCard) it.selectedCard else "X"
        textView_selectedCardInverted.text = if (it.isShowingCard) it.selectedCard else "X"
        textView_hint.text = if (it.isShowingCard) "Tap to hide your card" else "Tap to show your card"
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
        constraintLayout.setOnClickListener {
            viewModel.toggleVote()
        }
    }

    companion object {
        fun arg(selectedCard: String): Bundle{
            val args = Bundle()
            args.putString(MvRx.KEY_ARG, selectedCard)
            return args
        }
    }
}