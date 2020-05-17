package uk.marcin.scrumpoker

import ch.kuon.phoenix.Presence
import ch.kuon.phoenix.Socket
import com.airbnb.mvrx.*


data class User(val name: String)

data class RoomState(val users: Async<List<Presence.Entry>> = Uninitialized) : MvRxState

class RoomViewModel(initialState: RoomState) : MvRxViewModel<RoomState>(initialState){
    private var socket : Socket
    init {
        val url = "ws://test.marcin.uk/socket/?user_id=Test&vsn=2.0.0"
        val sd = Socket(url)
        socket = sd

        sd.connect()

        val channel = sd.channel("room:test:lobby")
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

    override fun onDestroyView() {
        super.onDestroyView()

    }
}