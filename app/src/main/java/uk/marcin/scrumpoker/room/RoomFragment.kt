package uk.marcin.scrumpoker.room

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ch.kuon.phoenix.Presence
import ch.kuon.phoenix.Socket
import com.airbnb.epoxy.*
import com.airbnb.mvrx.*
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.elem_room_user.*
import kotlinx.android.synthetic.main.fragment_room.view.*
import uk.marcin.scrumpoker.MvRxViewModel
import uk.marcin.scrumpoker.R
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


data class User(val name: String)

@Parcelize
data class RoomArgs(val roomName: String, val userName: String) : Parcelable

data class RoomState(val roomName: String,
                     val userName: String,
                     val users: Async<List<UserViewModel>> = Uninitialized) : MvRxState{
    constructor(args: RoomArgs) : this(args.roomName, args.userName)
}

class RoomViewModel(initialState: RoomState) : MvRxViewModel<RoomState>(initialState){
    private var socket : Socket
    init {
        val url = "wss://poker.marcin.uk/socket/?user_id=${initialState.userName}&vsn=2.0.0"
        val sd = Socket(url)
        socket = sd

        sd.connect()

        val channel = sd.channel("room:${initialState.roomName}:lobby")
        val presence = Presence(channel)

        presence.onSync {
            val presences = presence.list()
            val metas = presences.map { it.getMetas().getJSONObject(0)}

            val users = metas.map {UserViewModel(it.getString("userName"), "")}.toList()
            setState {
//                var users = presence.list()
//                presence.map()
//                Presence
                copy(users = Success(users))
            }
        }

        channel
            .join()
            .receive("ok") { msg ->
                // channel is connected
                System.out.println("Join success, got messages: " + msg.toString())
            }
            .receive("error") { msg ->
                System.out.println("Join ERROR, got messages: " + msg.toString())
            }
            .receive("timeout") { msg ->
                System.out.println("Join TIMEOUT, got messages: " + msg.toString())
            }

        channel
            .push("hello")
            .receive("ok") { msg ->
                // sent hello and got msg back
            }
    }

    override fun onCleared() {
        super.onCleared()
        socket.disconnect()
    }
}


class RoomFragment : BaseMvRxFragment(){
    private val viewModel: RoomViewModel by fragmentViewModel()
    protected lateinit var recyclerView: EpoxyRecyclerView
    protected val epoxyController by lazy { epoxyController() }
    override fun invalidate() = withState(viewModel){ state ->
        recyclerView.requestModelBuild()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room, container, false).apply {
            recyclerView = recyclerView_votes as EpoxyRecyclerView
            recyclerView.setController(epoxyController)
        }
    }

    private fun epoxyController(): MvRxEpoxyController {
        return simpleController(viewModel) { state ->
            when (state.users){
                is Success -> {
                    state.users.invoke().forEach { user ->
                        basicRow {
                            id(user.name)
                            userName(user.name)

                        }
                    }
                }
            }
        }
    }

    companion object {
        fun arg(roomArgs: RoomArgs): Bundle {
            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, roomArgs)
            return args
        }
    }
}


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class BasicRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val userNameView: TextView
    private val voteView: TextView

    init {
        inflate(context, R.layout.elem_room_user, this)
        userNameView = findViewById(R.id.textView_userName)
        voteView = findViewById(R.id.textView_vote)
        orientation = VERTICAL
    }

    @TextProp
    fun setUserName(title: CharSequence) {
        userNameView.text = title
    }

//    @TextProp
//    fun setTitle(title: CharSequence) {
//        userNameView.text = title
//    }

//    @TextProp
//    fun setSubtitle(subtitle: CharSequence?) {
//        voteView.visibility = if (subtitle.isNullOrBlank()) View.GONE else View.VISIBLE
//        voteView.text = subtitle
//    }

//    @CallbackProp
//    fun setClickListener(clickListener: OnClickListener?) {
//        setOnClickListener(clickListener)
//    }
}

fun <S : MvRxState, A : MvRxViewModel<S>> simpleController(
    viewModel: A,
    buildModels: EpoxyController.(state: S) -> Unit
) = MvRxEpoxyController {
    withState(viewModel) { state ->
        buildModels(state)
    }
}

class UserState(val name: String, val vote: String?) : MvRxState
class UserViewModel(val name: String, val vote: String?) : MvRxState

