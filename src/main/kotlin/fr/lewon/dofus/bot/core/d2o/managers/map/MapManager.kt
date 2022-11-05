package fr.lewon.dofus.bot.core.d2o.managers.map

import fr.lewon.dofus.bot.core.VldbManager
import fr.lewon.dofus.bot.core.d2o.D2OUtil
import fr.lewon.dofus.bot.core.i18n.I18NUtil
import fr.lewon.dofus.bot.core.model.maps.DofusMap
import fr.lewon.dofus.bot.core.model.maps.DofusSubArea

object MapManager : VldbManager {

    private lateinit var mapById: Map<Double, DofusMap>

    override fun initManager() {
        val objects = D2OUtil.getObjects("MapPositions")
        mapById = objects.associate {
            val id = it["id"].toString().toDouble()
            val posX = it["posX"].toString().toInt()
            val posY = it["posY"].toString().toInt()
            val nameId = it["nameId"].toString().toInt()
            val name = I18NUtil.getLabel(nameId) ?: "[UNKNOWN_NAME]"
            val subAreaId = it["subAreaId"].toString().toDouble()
            val subArea = SubAreaManager.getSubArea(subAreaId)
            val worldMapId = it["worldMap"].toString().toInt()
            val worldMap = WorldMapManager.getWorldMap(worldMapId)
            val isOutdoor = it["outdoor"].toString().toBoolean()
            val isTransition = it["isTransition"].toString().toBoolean()
            val hasPriorityOnWorldMap = it["hasPriorityOnWorldmap"].toString().toBoolean()
            val capabilities = it["capabilities"].toString().toInt()
            id to DofusMap(
                subArea, worldMap, id, posX, posY, name, isOutdoor, isTransition, hasPriorityOnWorldMap, capabilities
            )
        }
    }

    override fun getNeededManagers(): List<VldbManager> {
        return listOf(SubAreaManager, WorldMapManager)
    }

    fun getDofusMap(id: Double): DofusMap {
        return mapById[id] ?: error("No map found for id [$id]")
    }

    fun getDofusMaps(x: Int, y: Int): List<DofusMap> {
        return mapById.values.filter { it.posX == x && it.posY == y }
    }

    fun getDofusMaps(subArea: DofusSubArea): List<DofusMap> {
        return mapById.values.filter { it.subArea === subArea }
    }

}