package simpledm;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Handles player events
 * @author Matthew Jennings
 */
public class SimpleDeathmatchPlayerListener extends PlayerListener {

    private final SimpleDeathmatch plugin;
    
    public SimpleDeathmatchPlayerListener(SimpleDeathmatch instance) {
        plugin = instance;
    }

    /**
     * Handles commands from players. "/scores" will show how many frags each player has
     * @param evt
     */
    @Override
    public void onPlayerCommand(PlayerChatEvent evt) {
        //Split the command (seperates any arguments from the command)
        String msg[] = evt.getMessage().split(" ");

        // Scores
        if (msg[0].equalsIgnoreCase("/scores")) {
            evt.getPlayer().sendMessage("Simple Deathmatch scores:");
            // Iterate through all players in the score list and send their score to the player that issued the scores command
            for (Object o : plugin.scores.keySet()) {
                String pn = (String) o;
                evt.getPlayer().sendMessage(pn + " : " + plugin.scores.get(pn));
            }
        }

    }

    /**
     * When a player joins the server they are added to the scores list and the lastDamagedBy list
     * @param evt
     */
    @Override
    public void onPlayerJoin(PlayerEvent evt) {
        plugin.playerLastDamagedBy.put(evt.getPlayer().getEntityId(), null);
        plugin.scores.put(evt.getPlayer().getDisplayName(), new Integer(0));
        evt.getPlayer().sendMessage("This server is running SimpleDeathmatch v0.1");
        evt.getPlayer().sendMessage("Type /scores to see how many frags you have!");

    }

    /**
     * When a player leaves the server they are removed from any list containing them
     * @param evt
     */
    @Override
    public void onPlayerQuit(PlayerEvent evt) {

        plugin.playerLastDamagedBy.remove(evt.getPlayer().getEntityId());
        plugin.scores.remove(evt.getPlayer().getDisplayName());

    }

    /**
     * lastDamagedBy is reset for the player on respawn
     * @param evt
     */
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent evt) {
         plugin.playerLastDamagedBy.put(evt.getPlayer().getEntityId(), null);
    }

}
