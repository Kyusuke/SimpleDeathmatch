package simpledm;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

/**
 * Handles entity events. Most of this class is used to find out who killed who
 * @author Matthew jennings
 */
public class SimpleDeathmatchEntityListener extends EntityListener {

    private final SimpleDeathmatch plugin;

    public SimpleDeathmatchEntityListener(SimpleDeathmatch instance) {
        this.plugin = instance;
    }

    /**
     * When an entity dies, we check if it was a player. if it was a player then we print a message saying [PlayerName] was killed by [Last entity to damage the player]
     * The last entity that damaged the player should be stored in the playerLastDamagedBy hashmap
     * @param evt
     */
    @Override
    public void onEntityDeath(EntityDeathEvent evt) {
        if (evt.getEntity() instanceof Player) {
            Player p = (Player) evt.getEntity();

            // Print different messages depending on the kind of monster that killed the player
            if (plugin.playerLastDamagedBy.get(p.getEntityId()) instanceof Zombie) {

                plugin.getServer().broadcastMessage(p.getDisplayName() + " was eaten by a zombie");

            } else if (plugin.playerLastDamagedBy.get(p.getEntityId()) instanceof Spider) {

                plugin.getServer().broadcastMessage(p.getDisplayName() + " was jumped on by a spider");

            } else if (plugin.playerLastDamagedBy.get(p.getEntityId()) instanceof Creeper) {

                plugin.getServer().broadcastMessage(p.getDisplayName() + " was blown up by a creeper");

            } else if (plugin.playerLastDamagedBy.get(p.getEntityId()) instanceof Skeleton) {

                plugin.getServer().broadcastMessage(p.getDisplayName() + " was perforated by a skeleton");

            // if another player killed this player then print a message and increment the score of the player that fragged this one
            } else if (plugin.playerLastDamagedBy.get(p.getEntityId()) instanceof Player) {
                // get a reference to the other player and print a message
                Player p2 = (Player) plugin.playerLastDamagedBy.get(p.getEntityId());
                plugin.getServer().broadcastMessage(p.getDisplayName() + " was fragged by " + p2.getDisplayName());
                // increment score of other player
                Integer score = (Integer) plugin.scores.get(p2.getDisplayName());
                plugin.scores.put(p2.getDisplayName(), score+1);

            // if the player killed themselves then print a message and decrement their score
            } else if (plugin.playerLastDamagedBy.get(p.getEntityId()) instanceof DamageCause) {
                DamageCause dc = (DamageCause) plugin.playerLastDamagedBy.get(p.getEntityId());

                if (dc == DamageCause.FALL) {
                    plugin.getServer().broadcastMessage(p.getDisplayName() + " threw himself over a cliff. WHY?!");
                    Integer score = (Integer) plugin.scores.get(p.getDisplayName());
                    plugin.scores.put(p.getDisplayName(), score-1);
                } else if (dc == DamageCause.DROWNING ) {
                    plugin.getServer().broadcastMessage(p.getDisplayName() + " drowned himself");
                    Integer score = (Integer) plugin.scores.get(p.getDisplayName());
                    plugin.scores.put(p.getDisplayName(), score-1);
                } else {
                    // I cant be bothered to add messages for all damage types right now :<
                    plugin.getServer().broadcastMessage(p.getDisplayName() + " died mysteriously");
                    Integer score = (Integer) plugin.scores.get(p.getDisplayName());
                    plugin.scores.put(p.getDisplayName(), score-1);
                }
            }
        }
    }

    /**
     * When an entity is damaged by another entity, check if the damaged entity is a player. If it is then add the entity that damaged the player to playerLastDamagedBy for this player
     * @param evt
     */
    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
        if (evt.getEntity() instanceof Player) {
            plugin.playerLastDamagedBy.put(evt.getEntity().getEntityId(), evt.getDamager());
        }
    }

    /**
     * As above but for blocks (only tnt i think?)
     * @param evt
     */
    @Override
    public void onEntityDamageByBlock(EntityDamageByBlockEvent evt) {
        if (evt.getEntity() instanceof Player) {
            plugin.playerLastDamagedBy.put(evt.getEntity().getEntityId(), evt.getDamager());
        }
    }

    /**
     * As above but for enviromental damage
     * @param evt
     */
    @Override
    public void onEntityDamage(EntityDamageEvent evt) {
        if (evt.getEntity() instanceof Player) {
            if ((evt.getCause() == DamageCause.FALL) || (evt.getCause() == DamageCause.DROWNING) || (evt.getCause() == DamageCause.FIRE) || (evt.getCause() == DamageCause.LAVA) || (evt.getCause() == DamageCause.SUFFOCATION) || (evt.getCause() == DamageCause.BLOCK_EXPLOSION) || (evt.getCause() == DamageCause.FIRE_TICK)) {
                plugin.playerLastDamagedBy.put(evt.getEntity().getEntityId(), evt.getCause());
            }
        }
    }

    /**
     * As above but for projectiles. Get the entity that fired the projectile and add it to playerLastDamagedBy
     * @param evt
     */
    @Override
    public void onEntityDamageByProjectile(EntityDamageByProjectileEvent evt) {
        if (evt.getEntity() instanceof Player) {
            plugin.playerLastDamagedBy.put(evt.getEntity().getEntityId(), evt.getDamager());
        }
    }

}
