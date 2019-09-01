package com.game.quillyjumper.configuration

import com.badlogic.gdx.math.Vector2
import com.game.quillyjumper.ecs.component.EntityType
import com.game.quillyjumper.ecs.component.ModelType
import ktx.log.logger
import java.util.*

private val LOG = logger<CharacterCfgCache>()

enum class Character {
    PLAYER,
    BLUE_SLIME
}

class CharacterCfg(val entityType: EntityType, val modelType: ModelType) {
    var speed = 0f
    var size = Vector2(1f, 1f)
    var collBodyOffset = Vector2(0f, 0f)
}

class CharacterCfgCache : EnumMap<Character, CharacterCfg>(Character::class.java) {
    private val defaultCfg = CharacterCfg(EntityType.OTHER, ModelType.UNKNOWN)

    fun characterCfg(
        id: Character,
        entityType: EntityType,
        modelType: ModelType,
        init: CharacterCfg.() -> Unit = { Unit }
    ) {
        if (this.containsKey(id)) {
            LOG.error { "Character configuration for id $id is already existing!" }
            return
        }
        this[id] = CharacterCfg(entityType, modelType).apply(init)
    }

    override operator fun get(key: Character): CharacterCfg {
        val cfg = super.get(key)
        if (cfg == null) {
            LOG.error { "Trying to access character cfg $key which is not configured yet!" }
            return defaultCfg
        }
        return cfg
    }
}

inline fun characterCfgCache(init: CharacterCfgCache.() -> Unit) = CharacterCfgCache().apply(init)