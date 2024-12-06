package net.supiserver.play.magicStone.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Util {
    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void generateParticles(Location location, Particle particle, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        Objects.requireNonNull(location.getWorld()).spawnParticle(particle,
                location.getX(),
                location.getY(),
                location.getZ(),
                count,
                offsetX,
                offsetY,
                offsetZ,
                extra);
    }
}
