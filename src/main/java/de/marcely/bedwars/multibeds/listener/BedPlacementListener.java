package de.marcely.bedwars.multibeds.listener;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerAddBedEvent;
import de.marcely.bedwars.multibeds.BedPlacementGUI;
import de.marcely.bedwars.multibeds.MultiBedsPlugin;
import de.marcely.bedwars.multibeds.data.ArenaData;
import de.marcely.bedwars.tools.location.XYZD;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BedPlacementListener implements Listener {

  private final MultiBedsPlugin plugin;

  public BedPlacementListener(MultiBedsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerAddBedEvent(PlayerAddBedEvent event) {
    event.setCancelled(true);

    final Arena arena = event.getArena();
    final ArenaData arenaData = this.plugin.getArenaData().get(arena);
    final BedPlacementGUI gui = new BedPlacementGUI(
        this.plugin,
        arenaData,
        event.getPlayer(),
        event.getTeam(),
        bedIndex -> {
          if (!bedIndex.isPresent())
            return;

          arenaData.setBedPlacement(
              event.getTeam(),
              bedIndex.get(),
              new XYZD(event.getLocation())
          );
        }
    );
  }
}
