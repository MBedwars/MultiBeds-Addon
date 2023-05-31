package de.marcely.bedwars.multibeds.listener;

import de.marcely.bedwars.api.event.ConfigsLoadEvent;
import de.marcely.bedwars.api.event.arena.ArenaDeleteEvent;
import de.marcely.bedwars.multibeds.MultiBedsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ArenaUnloadListener implements Listener {

  private final MultiBedsPlugin plugin;

  public ArenaUnloadListener(MultiBedsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onConfigsLoadEvent(ConfigsLoadEvent event) {
    this.plugin.getArenaData().unloadAll();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onArenaDeleteEvent(ArenaDeleteEvent event) {
    this.plugin.getArenaData().unload(event.getArena());
  }
}
