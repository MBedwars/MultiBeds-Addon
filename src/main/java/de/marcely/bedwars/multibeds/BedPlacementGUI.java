package de.marcely.bedwars.multibeds;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.multibeds.data.ArenaData;
import de.marcely.bedwars.multibeds.util.ItemStackUtil;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.gui.AddItemCondition;
import de.marcely.bedwars.tools.gui.CenterFormat;
import de.marcely.bedwars.tools.gui.GUIItem;
import de.marcely.bedwars.tools.gui.type.ChestGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BedPlacementGUI {

  private static final ItemStack ITEM_EMPTY_GLASS_PANE =
      Helper.get().parseItemStack("GRAY_STAINED_GLASS_PANE {DisplayName:\" \"}");
  private static final ItemStack ITEM_ADD_BED =
      Helper.get().parseItemStack("skull:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19"
          + " {DisplayName:\"&a&l+1\"}");
  private static final ItemStack ITEM_REMOVE_BED =
      Helper.get().parseItemStack("skull:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0="
          + " {DisplayName:\"&c&l-1\"}");

  private final MultiBedsPlugin plugin;
  private final ArenaData data;
  private final Player player;
  private final Team team;
  private Consumer<Optional<Integer>> listener;

  public void open() {
    final ChestGUI gui = new ChestGUI(
        Message.buildByKey("MultiBeds_BedPlacement_Title")
            .placeholder("arena", data.getArena().getName())
            .done(this.player)
    );

    gui.addCloseListener(p -> callListener(Optional.empty()));

    render(gui, true);

    gui.open(this.player);
  }

  private void render(ChestGUI gui, boolean newGUI) {
    final Arena arena = this.data.getArena();
    final List<Team> enabledTeams = arena.getEnabledTeams()
        .stream()
        .sorted()
        .collect(Collectors.toList());

    gui.setHeight(3 + Math.min(3, (int) Math.ceil(this.data.getBedsCount()/9D)));

    // render general stuff
    if (newGUI) {
      // bars
      for (int x=0; x<9; x++) {
        gui.setItem(ITEM_EMPTY_GLASS_PANE, x, 0);
        gui.setItem(ITEM_EMPTY_GLASS_PANE, x, 2);
      }

      // misc
      gui.setItem(arena.getIcon(), 8, 0);
    }

    // bed amount modifiers
    {
      if (this.data.getBedsCount() > 1) {
        gui.setItem(
            ITEM_REMOVE_BED,
            3, 1,
            (g0, g1, g2) -> {
              if (this.data.getBedsCount() > 1)
                this.data.setBedsCount(this.data.getBedsCount()-1);

              render(gui, false);
            }
        );
      }

      if (this.data.getBedsCount() < 3 * 9) {
        gui.setItem(
            ITEM_REMOVE_BED,
            5, 1,
            (g0, g1, g2) -> {
              if (this.data.getBedsCount() < 3 * 9)
                this.data.setBedsCount(this.data.getBedsCount()+1);

              render(gui, false);
            }
        );
      }
    }

    // beds
    {
      if (!newGUI) {
        for (int x = 0; x < 9; x++) {
          for (int y = 3; y < gui.getHeight(); y++) {
            gui.setItem((GUIItem) null, x, y);
          }
        }
      }

      for (int i = 0; i < this.data.getBedsCount(); i++) {
        final int index = i;
        final List<String> lines = new ArrayList<>();

        lines.add(Message.buildByKey("MultiBeds_BedPlacement_Place")
            .placeholder("teamcolor", this.team.getBungeeChatColor())
            .placeholder("team", this.team.getDisplayName(this.player))
            .done(this.player));

        for (Team team : enabledTeams) {
          final boolean placed = this.data.getBedPlacement(team, i) != null;
          final String placedString = placed ? ChatColor.GREEN + "✔" : ChatColor.RED + "❌";

          lines.add(team.getDisplayName(this.player) + ChatColor.GRAY + ": " + placedString);
        }

        gui.addItem(
            ItemStackUtil.setLines(this.plugin.getBedItem(this.team), lines),
            (g0, g1, g2) -> {
              if (index >= this.data.getBedsCount()) {
                render(gui, false);
                return;
              }

              callListener(Optional.of(index));
              gui.closeAll();
            }, AddItemCondition.withinY(3, 5));
      }

      for (int y = 3; y < gui.getHeight(); y++)
        gui.formatRow(y, CenterFormat.CENTRALIZED_EVEN);
    }
  }

  private void callListener(Optional<Integer> data) {
    if (this.listener == null)
      return;

    this.listener = null;
    this.listener.accept(data);
  }
}