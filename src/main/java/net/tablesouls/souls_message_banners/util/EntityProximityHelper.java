package net.tablesouls.souls_message_banners.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityProximityHelper {
    public static boolean hasNearbyMonsters(Level level, Player player, double radius) {
        AABB area = player.getBoundingBox().inflate(radius);
        List<Monster> monsters = level.getEntitiesOfClass(
                Monster.class, area, m -> m.isAlive() && !m.isRemoved());
        return !monsters.isEmpty();
    }
    public static List<Player> getPlayersNearby(LivingEntity entity, double radius) {
        Level level = entity.level();
        AABB area = entity.getBoundingBox().inflate(radius);
        List<Player> players = level.getEntitiesOfClass(
                Player.class, area);
        return players;
    }
}