package fr.lewon.dofus.bot.core.model.maps


data class DofusMap(
    val subArea: DofusSubArea,
    val worldMap: DofusWorldMap?,
    val id: Double,
    val posX: Int,
    val posY: Int,
    val name: String,
    val outdoor: Boolean,
    val isTransition: Boolean,
    val hasPriorityOnWorldMap: Boolean,
    val capabilities: Int
) {

    fun getCoordinates(): DofusCoordinates {
        return DofusCoordinates(posX, posY)
    }

    fun canReachHavenBag(): Boolean = capabilities and 8 != 0 && capabilities and 4 != 0

}