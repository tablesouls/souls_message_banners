package net.tablesouls.souls_message_banners.data;

public enum TriggerType {
    ENTITY_DIED("entity_died"),
    ENTITY_SPAWNED("entity_spawned"),
    PLAYED_DIED("player_died"),
    PLAYER_SPAWNED("player_spawned"),
    PLAYER_SPAWN_SET("player_spawn_set"),
    BLOCK_STATE_CHANGED_ON_INTERACT("block_state_changed_on_interact"),
    RAID_STATUS("raid_status"),
    BONFIRE_LIT("bonfires:bonfire_lit"),
    WAYSTONE_ACTIVATION("waystones:waystone_activated"),
    XP_CATALYST_RETRIEVED("irons_rpg_tweaks:xp_catalyst_retrieved");

    private final String id;

    TriggerType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static TriggerType fromId(String id) {
        for (TriggerType type : values()) {
            if (type.id.equals(id)) return type;
        }
        throw new IllegalArgumentException("Unknown trigger type \"" + id + "\"");
    }

    public boolean supportsTags() {
        return this == ENTITY_DIED || this == ENTITY_SPAWNED || this == BLOCK_STATE_CHANGED_ON_INTERACT || this == WAYSTONE_ACTIVATION;
    }
}