package de.marcely.bedwars.multibeds.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.tools.location.XYZD;
import de.marcely.bedwars.tools.location.XYZD.Direction;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class ArenaDataSerializer {

  private static final Gson GSON = new Gson();
  private static final String STORAGE_KEY = "multibeds:data";

  public static JsonObject serialize(ArenaData data) {
    final JsonObject root = new JsonObject();

    root.addProperty("beds-count", data.getBedsCount());

    {
      final JsonObject teamsObj = new JsonObject();

      for (Map.Entry<Team, Map<Integer, XYZD>> teamEntry : data.getBedPlacements().entrySet()) {
        final JsonObject teamObj = new JsonObject();

        {
          final JsonObject bedsObj = new JsonObject();

          for (Map.Entry<Integer, XYZD> bedEntry : teamEntry.getValue().entrySet()) {
            final JsonObject bedObj = new JsonObject();
            final XYZD pos = bedEntry.getValue();

            bedObj.addProperty("x", pos.getX());
            bedObj.addProperty("y", pos.getY());
            bedObj.addProperty("z", pos.getZ());
            bedObj.addProperty("direction", pos.getDirection().name());

            bedsObj.add(String.valueOf(bedEntry.getKey()), bedObj);
          }

          teamObj.add("beds", bedsObj);
        }

        teamsObj.add(teamEntry.getKey().name(), teamObj);
      }

      root.add("teams", teamsObj);
    }

    return root;
  }

  public static ArenaData deserialize(Arena arena, JsonObject root) throws Exception {
    final ArenaData data = new ArenaData(arena);

    data.setBedsCount(root.get("beds-count").getAsInt());

    {
      final JsonObject teamsObj = root.getAsJsonObject("teams");

      for (Map.Entry<String, JsonElement> teamEntry : teamsObj.entrySet()) {
        final Team team = Team.valueOf(teamEntry.getKey());
        final JsonObject teamObj = teamEntry.getValue().getAsJsonObject();

        {
          final JsonObject bedsObj = teamObj.getAsJsonObject("beds");

          for (Map.Entry<String, JsonElement> bedEntry : bedsObj.entrySet()) {
            final int index = Integer.parseInt(bedEntry.getKey());
            final JsonObject posObj = bedEntry.getValue().getAsJsonObject();
            final XYZD pos = new XYZD(
                posObj.get("x").getAsDouble(),
                posObj.get("y").getAsDouble(),
                posObj.get("z").getAsDouble(),
                Direction.valueOf(posObj.get("direction").getAsString())
            );

            data.setBedPlacement(team, index, pos);
          }
        }
      }
    }

    return data;
  }

  public static void save(ArenaData data) {
    try {
      data.getArena().getPersistentStorage().set(
          STORAGE_KEY,
          GSON.toJson(serialize(data))
      );
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  @Nullable
  public static ArenaData load(Arena arena) {
    try {
      final Optional<String> value = arena.getPersistentStorage().get(STORAGE_KEY);

      if (!value.isPresent())
        return null;

      return deserialize(arena, GSON.fromJson(value.get(), JsonObject.class));
    } catch (Throwable t) {
      t.printStackTrace();
    }

    return null;
  }
}
