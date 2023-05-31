package de.marcely.bedwars.multibeds.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {

  public static ItemStack setName(ItemStack is, String name) {
    final ItemMeta im = is.getItemMeta();

    im.setDisplayName(name);

    is.setItemMeta(im);

    return is;
  }
}
