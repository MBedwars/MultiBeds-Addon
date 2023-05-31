package de.marcely.bedwars.multibeds;

import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.multibeds.data.ArenaDataCache;
import de.marcely.bedwars.tools.Helper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiBedsPlugin extends JavaPlugin {

  private static final byte MBEDWARS_API_NUM = 21;
  private static final String MBEDWARS_API_NAME = "5.2.5";

  @Getter
  private MutliBedsAddon addon;

  @Getter
  private ArenaDataCache arenaData;

  @Override
  public void onEnable() {
    if (!validateMBedwars())
      return;
    if (!registerAddon())
      return;

    this.arenaData = new ArenaDataCache();
  }

  private boolean validateMBedwars() {
    try {
      final Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
      final int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

      if (apiVersion < MBEDWARS_API_NUM)
        throw new IllegalStateException();
    } catch (Exception e) {
      getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v" + MBEDWARS_API_NAME);
      Bukkit.getPluginManager().disablePlugin(this);

      return false;
    }

    return true;
  }

  private boolean registerAddon() {
    this.addon = new MutliBedsAddon(this);

    if (!this.addon.register()) {
      getLogger().warning("It seems like the addon is already running. Please delete the duplicate and try again");
      Bukkit.getPluginManager().disablePlugin(this);

      return false;
    }

    return true;
  }

  public ItemStack getBedItem(Team team) {
    return Helper.get().parseItemStack(team.name() + "_BED");
  }
}