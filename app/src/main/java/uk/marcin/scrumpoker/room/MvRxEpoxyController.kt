package uk.marcin.scrumpoker.room

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController

open class MvRxEpoxyController(
    val buildModelsCallback: EpoxyController.() -> Unit = {}
) : AsyncEpoxyController() {

    override fun buildModels() {
        buildModelsCallback()
    }
}