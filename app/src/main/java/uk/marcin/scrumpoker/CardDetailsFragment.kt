package uk.marcin.scrumpoker

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
    private var isInitalised: Boolean = false

    override fun invalidate() = withState(viewModel) { state ->
        if (!isInitalised){
            isInitalised = true
            updateCard(state)
            return@withState
        }

        cardView_cardContainer.animate().withLayer()
            .rotationY(90f)
            .rotationX(-20F)
            .setDuration(150)
            .setInterpolator(AccelerateInterpolator(2f))
            .withEndAction {
                val v = cardView_cardContainer

                val scale: Float =
                    this.requireActivity().getApplicationContext()
                        .getResources().getDisplayMetrics().density
                val distance: Float =
                    v.getCameraDistance() * (scale + scale / 20)
                if (distance.isFinite() && distance > 0)
                    v.cameraDistance = distance

                updateCard(state)

                v.rotationY = -90f;
                v.rotationX = -20f;
                v.animate().withLayer()
                    .rotationY(0f)
                    .rotationX(0F)
                    .setDuration(250)
                    .setInterpolator(DecelerateInterpolator(2f))
                    .withEndAction {
                    }
                    .start();
            }.start();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    fun updateCard(state: CardDetailsState){
        val colorId = if (state.isShowingCard) R.color.cardFrontBackground else R.color.cardBackBackground
        val color = ContextCompat.getColor(this.requireContext(), colorId)
        cardView_cardContainer.setCardBackgroundColor(color)

        textView_cardSelected.isVisible = state.isShowingCard == false
        textView_selectedCard.text = if (state.isShowingCard) state.selectedCard else ""
        textView_selectedCardInverted.text = if (state.isShowingCard) state.selectedCard else ""
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