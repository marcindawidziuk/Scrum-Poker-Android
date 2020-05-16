package uk.marcin.scrumpoker

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRxState


data class User(val name: String)

data class RoomState(val users: Async<List<User>>) : MvRxState

class RoomViewModel(initialState: RoomState) : MvRxViewModel<RoomState>(initialState){
    init {
        val url = "ws://localhost:4444/socket"
        val sd = Socket(url)

        sd.connect()

        val chan = sd.channel("auth:login")

        chan
            .join()
            .receive("ok") { msg ->
                // channel is connected
            }
            .receive("error") { msg ->
                // channel did not connected
            }
            .receive("timeout") { msg ->
                // connection timeout
            }

        chan
            .push("hello")
            .receive("ok") { msg ->
                // sent hello and got msg back
            }
    }
}


class RoomFragment : BaseMvRxFragment(){
    override fun invalidate()
    }

}