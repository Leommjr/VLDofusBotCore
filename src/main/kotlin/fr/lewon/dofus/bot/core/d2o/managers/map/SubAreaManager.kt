package fr.lewon.dofus.bot.core.d2o.managers.map

import fr.lewon.dofus.bot.core.VldbManager
import fr.lewon.dofus.bot.core.d2o.D2OUtil
import fr.lewon.dofus.bot.core.d2o.managers.entity.MonsterManager
import fr.lewon.dofus.bot.core.i18n.I18NUtil
import fr.lewon.dofus.bot.core.model.maps.DofusMap
import fr.lewon.dofus.bot.core.model.maps.DofusSubArea

object SubAreaManager : VldbManager {

    private lateinit var subAreaById: Map<Double, DofusSubArea>

    override fun initManager() {
        subAreaById = D2OUtil.getObjects("SubAreas").associate {
            val id = it["id"].toString().toDouble()
            val worldMapId = it["worldmapId"].toString().toInt()
            val worldMap = WorldMapManager.getWorldMap(worldMapId)
            val packId = it["packId"].toString().toInt()
            val isConquestVillage = it["isConquestVillage"].toString().toBoolean()
            val associatedZaapMapId = it["associatedZaapMapId"].toString().toDouble()
            val nameId = it["nameId"].toString().toInt()
            val name = I18NUtil.getLabel(nameId) ?: "UNKNOWN_SUB_AREA_NAME"
            val areaId = it["areaId"].toString().toDouble()
            val area = AreaManager.getArea(areaId)
            val monsterIds = it["monsters"] as List<Double>
            val mapIds = it["mapIds"] as List<Double>
            val monsters = monsterIds.map { monsterId -> MonsterManager.getMonster(monsterId) }
            val psiAllowed = it["psiAllowed"].toString().toBoolean()
            val displayOnWorldMap = it["displayOnWorldMap"].toString().toBoolean()
            id to DofusSubArea(
                id, worldMap, monsters, mapIds, packId, isConquestVillage,
                associatedZaapMapId, name, area, psiAllowed, displayOnWorldMap
            )
        }
    }

    override fun getNeededManagers(): List<VldbManager> {
        return listOf(AreaManager, WorldMapManager, MonsterManager)
    }

    fun getAllSubAreas(): List<DofusSubArea> {
        return ArrayList(subAreaById.values)
    }

    fun getAllZaapMaps(): List<DofusMap> {
        return subAreaById.values.filter { it.associatedZaapMapId > 0 }
            .map { MapManager.getDofusMap(it.associatedZaapMapId) }
    }

    fun getSubArea(subAreaId: Double): DofusSubArea {
        return subAreaById[subAreaId] ?: error("No sub area for id : $subAreaId")
    }

}