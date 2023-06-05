package de.marcely.bedwars.multibeds.util;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {

  public static ItemStack setName(ItemStack is, String name) {
    final ItemMeta im = is.getItemMeta();

    im.setDisplayName(name);

    is.setItemMeta(im);

    return is;
  }

  public static ItemStack setLines(ItemStack is, List<String> list) {
    if (list.isEmpty())
      return is;

    final ItemMeta im = is.getItemMeta();

    im.setDisplayName(list.get(0));

    if (list.size() >= 2)
      im.setLore(list.stream().skip(1).collect(Collectors.toList()));

    is.setItemMeta(im);

    return is;
  }
}
