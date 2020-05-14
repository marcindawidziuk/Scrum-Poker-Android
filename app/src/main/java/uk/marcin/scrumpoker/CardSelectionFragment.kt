package uk.marcin.scrumpoker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_card_selection.view.*

class CardSelectionFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClickListener = View.OnClickListener { v ->
            val button = v as Button
            val directions = CardSelectionFragmentDirections.actionSecondFragmentToCardDetailsFragment(button.text.toString())
            findNavController().navigate(directions)
        }
        view.button2.setOnClickListener(onClickListener)
        view.button3.setOnClickListener(onClickListener)
        view.button4.setOnClickListener(onClickListener)
        view.button5.setOnClickListener(onClickListener)
        view.button6.setOnClickListener(onClickListener)
        view.button7.setOnClickListener(onClickListener)
        view.button8.setOnClickListener(onClickListener)
        view.button9.setOnClickListener(onClickListener)
        view.button10.setOnClickListener(onClickListener)
        view.button11.setOnClickListener(onClickListener)
        view.button12.setOnClickListener(onClickListener)
        view.button13.setOnClickListener(onClickListener)
    }
}
