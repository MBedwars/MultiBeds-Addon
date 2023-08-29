package de.marcely.bedwars.multibeds.data;

import de.marcely.bedwars.api.arena.Arena;
import java.util.IdentityHashMap;
import java.util.Map;

public class ArenaDataCache {

  private final Map<Arena, ArenaData> cache = new IdentityHashMap<>();

  public ArenaData get(Arena arena) {
    // from cache
    ArenaData data = this.cache.get(arena);

    if (data != null)
      return data;

    // load it
    data = ArenaDataSerializer.load(arena);

    if (data == null)
      data = new ArenaData(arena);

    // cache it
    if (arena.exists())
      this.cache.put(arena, data);

    return data;
  }

  public void unload(Arena arena) {
    this.cache.remove(arena);
  }

  public void unloadAll() {
    this.cache.clear();
  }
}
