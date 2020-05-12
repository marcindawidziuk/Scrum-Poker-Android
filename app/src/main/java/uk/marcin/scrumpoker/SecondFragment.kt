package uk.marcin.scrumpoker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_second.view.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Destina
//        findNavController().

//        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        val onClickListener = View.OnClickListener { v ->
            val card = v as Button
            val directions = SecondFragmentDirections.actionSecondFragmentToCardDetailsFragment(card.text.toString())
            findNavController().navigate(directions);
        }
        view.button2.setOnClickListener(onClickListener)
        view.button3.setOnClickListener(onClickListener)
        view.button4.setOnClickListener(onClickListener)
        view.button5.setOnClickListener(onClickListener)
        view.button6.setOnClickListener(onClickListener)
        view.button7.setOnClickListener(onClickListener)
        view.button8.setOnClickListener(onClickListener)
        view.button9.setOnClickListener(onClickListener)
//        view.button2.setOnClickListener {
//            val directions = SecondFragmentDirections.actionSecondFragmentToCardDetailsFragment("0")
//
//            findNavController().navigate(directions);
//        }
    }
}
