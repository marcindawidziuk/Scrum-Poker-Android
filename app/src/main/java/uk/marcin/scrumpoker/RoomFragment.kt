package uk.marcin.scrumpoker

import android.os.Bundle
import android.os.Parcelable
import ch.kuon.phoenix.Presence
import ch.kuon.phoenix.Socket
import com.airbnb.mvrx.*
import kotlinx.android.parcel.Parcelize


data class User(val name: String)

@Parcelize
data class RoomArgs(val roomName: String, val userName: String) : Parcelable

data class RoomState(val roomName: String,
                     val userName: String,
                     val users: Async<List<Presence.Entry>> = Uninitialized) : MvRxState{
    constructor(args: RoomArgs) : this(args.roomName, args.userName)
}

class RoomViewModel(initialState: RoomState) : MvRxViewModel<RoomState>(initialState){
    private var socket : Socket
    init {
        val url = "ws://test.marcin.uk/socket/?user_id=${initialState.userName}&vsn=2.0.0"
        val sd = Socket(url)
        socket = sd

        sd.connect()

        val channel = sd.channel("room:${initialState.roomName}:lobby")
        val presence = Presence(channel)

        presence.onSync {
            setState {
                copy(users = Success(presence.list()))
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
    override fun invalidate() = withState(viewModel){

    }

    companion object {
        fun arg(roomArgs: RoomArgs): Bundle {
            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, roomArgs)
            return args
        }
    }
}