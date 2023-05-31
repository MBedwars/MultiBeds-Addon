package de.marcely.bedwars.multibeds.data;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.tools.location.ImmutableXYZD;
import de.marcely.bedwars.tools.location.XYZD;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class ArenaData {

  @Getter
  private final Arena arena;

  @Getter
  private int bedsCount = 1;
  @Getter
  private Map<Team, Map<Integer, XYZD>> bedPlacements = new EnumMap<>(Team.class);

  public ArenaData(Arena arena) {
    this.arena = arena;
  }

  public void setBedsCount(int count) {
    if (count < 0)
      throw new IllegalArgumentException("count is out of bounds (got " + count + ")");

    this.bedsCount = count;
  }

  @Nullable
  public XYZD getBedPlacement(Team team, int index) {
    final Map<Integer, XYZD> teamPlacements = this.bedPlacements.get(team);

    if (teamPlacements == null)
      return null;

    return teamPlacements.get(index);
  }

  public void setBedPlacement(Team team, int index, @Nullable XYZD pos) {
    if (index < 0 || index >= this.bedsCount)
      throw new IllegalArgumentException("index out of bounds (must be between 0-" + (this.bedsCount-1) + "), got " + index);

    // fetch team placemenets
    Map<Integer, XYZD> teamPlacements = this.bedPlacements.get(team);

    if (teamPlacements == null) {
      if (pos == null)
        return;

      this.bedPlacements.put(team, teamPlacements = new HashMap<>());
    }

    // update bed
    if (pos != null)
      teamPlacements.put(index, new ImmutableXYZD(pos));
    else {
      teamPlacements.remove(index);

      if (teamPlacements.isEmpty())
        this.bedPlacements.remove(team);
    }
  }

  public void clearUnused() {
    this.bedPlacements.keySet().removeIf(team -> !this.arena.isTeamEnabled(team));

    for (Map<Integer, XYZD> map : this.bedPlacements.values()) {
      map.keySet().removeIf(i -> i < 0 || i >= this.bedsCount);
    }
  }

  public void save() {
    ArenaDataSerializer.save(this);
  }
}