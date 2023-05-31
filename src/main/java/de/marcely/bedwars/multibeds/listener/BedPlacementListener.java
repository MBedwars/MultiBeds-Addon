package de.marcely.bedwars.multibeds.listener;

import de.marcely.bedwars.api.event.player.PlayerAddBedEvent;
import de.marcely.bedwars.multibeds.MultiBedsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BedPlacementListener implements Listener {

  private final MultiBedsPlugin plugin;

  public BedPlacementListener(MultiBedsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerAddBedEvent(PlayerAddBedEvent event) {

  }
}
