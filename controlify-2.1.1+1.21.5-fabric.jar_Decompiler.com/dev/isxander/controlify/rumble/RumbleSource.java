package dev.isxander.controlify.rumble;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import dev.isxander.controlify.utils.CUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.class_2960;

public record RumbleSource(class_2960 id) {
   public static final Codec<RumbleSource> CODEC;
   private static final Map<class_2960, RumbleSource> SOURCES;
   public static final RumbleSource MASTER;
   public static final RumbleSource PLAYER;
   public static final RumbleSource WORLD;
   public static final RumbleSource INTERACTION;
   public static final RumbleSource GUI;

   public RumbleSource(class_2960 id) {
      this.id = id;
   }

   public static RumbleSource get(class_2960 id) {
      RumbleSource source = (RumbleSource)SOURCES.get(id);
      if (source == null) {
         CUtil.LOGGER.warn("Unknown rumble source: {}. Using master.", id);
         return MASTER;
      } else {
         return source;
      }
   }

   public static Collection<RumbleSource> values() {
      return SOURCES.values();
   }

   public static JsonObject getDefaultJson() {
      JsonObject object = new JsonObject();
      Iterator var1 = SOURCES.values().iterator();

      while(var1.hasNext()) {
         RumbleSource source = (RumbleSource)var1.next();
         object.addProperty(source.id().toString(), 1.0F);
      }

      return object;
   }

   public static Map<class_2960, Float> getDefaultMap() {
      Map<class_2960, Float> map = new HashMap();
      Iterator var1 = SOURCES.values().iterator();

      while(var1.hasNext()) {
         RumbleSource source = (RumbleSource)var1.next();
         map.put(source.id(), 1.0F);
      }

      return map;
   }

   public static RumbleSource register(class_2960 id) {
      RumbleSource source = new RumbleSource(id);
      SOURCES.put(id, source);
      return source;
   }

   public static RumbleSource register(String identifier, String path) {
      return register(class_2960.method_60655(identifier, path));
   }

   private static RumbleSource register(String path) {
      return register("controlify", path);
   }

   public class_2960 id() {
      return this.id;
   }

   static {
      CODEC = class_2960.field_25139.xmap(RumbleSource::get, RumbleSource::id);
      SOURCES = new Object2ObjectLinkedOpenHashMap();
      MASTER = register("master");
      PLAYER = register("player");
      WORLD = register("world");
      INTERACTION = register("interaction");
      GUI = register("gui");
   }
}
