package simpledm;

import java.io.File;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Simple Deathmatch is a plugin that provides death messages ("XXX was killed by YYY") and shows how many frags each player has.
 * The hasmap 'playerLastDamagedBy' uses a player's entity id as a key and stores a reference to an entity or DamageCause as the value against each key.
 * When a player is damaged by an entity that entity is added to the HashMap using the player's id as the key.
 * This enables use to determine who killed a player when the player dies (the last entity/DamageCause to damage the player must be the one that killed him, right?)
 * 
 * 'Scores' Simply stores Players and an integer value against each player to represent their score. If a player is found to have killed another player their score is incremented by 1.
 * @author Matthew Jennings
 */
public class SimpleDeathmatch extends JavaPlugin {

    // Listeners
    private final SimpleDeathmatchPlayerListener playerListener = new SimpleDeathmatchPlayerListener(this);
    private final SimpleDeathmatchEntityListener entityListener = new SimpleDeathmatchEntityListener(this);
    // The first hash map stores a reference to the last entity that damaged a player. The second stores frags.
    public HashMap playerLastDamagedBy = new HashMap();
    public HashMap scores = new HashMap();


    public SimpleDeathmatch(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }

    public void onDisable() {
        System.out.println("Simple Deathmatch disabled");
    }

    public void onEnable() {
        
        PluginManager pm = getServer().getPluginManager();

        // register events
        pm.registerEvent(Type.ENTITY_DEATH, entityListener, Priority.Normal, this);
        pm.registerEvent(Type.ENTITY_DAMAGEDBY_ENTITY, entityListener, Priority.Normal, this);
        pm.registerEvent(Type.ENTITY_DAMAGEDBY_BLOCK, entityListener, Priority.Normal, this);
        pm.registerEvent(Type.ENTITY_DAMAGED, entityListener, Priority.Normal, this);
        pm.registerEvent(Type.ENTITY_DAMAGEDBY_PROJECTILE, entityListener, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);

        // print plugin info in server console at startup
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }

}
