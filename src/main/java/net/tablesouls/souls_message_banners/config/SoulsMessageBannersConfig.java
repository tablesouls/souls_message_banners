package net.tablesouls.souls_message_banners.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.minecraft.core.registries.BuiltInRegistries;

public class SoulsMessageBannersConfig {
    public static final ModConfigSpec COMMON_SPEC;
    public static final Triggers TRIGGERS;

    public static final ModConfigSpec CLIENT_SPEC;
    public static final Appearance APPEARANCE;
    public static final ModConfigSpec.BooleanValue HIDE_CROSSHAIR_WHEN_BANNER;

    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();

        TRIGGERS = new Triggers(commonBuilder);
        COMMON_SPEC = commonBuilder.build();

        clientBuilder
                .comment("Customizations in configs are overwritten by banner styles",
                        "May require reloading",
                        "Change existing banner styles via resourcepacks",
                        "Choose custom mob death message via datapacks");

        APPEARANCE = new Appearance(clientBuilder);

        HIDE_CROSSHAIR_WHEN_BANNER = clientBuilder
                .comment("Should player's crosshair temporarily hide when a message banner is shown")
                .define("hide_crosshair_when_banner", true);

        CLIENT_SPEC = clientBuilder.build();
    }

    public static class Triggers {
        public final ModConfigSpec.BooleanValue CAMPFIRE_LIT;
        public final ModConfigSpec.BooleanValue BONFIRE_LIT;
        public final ModConfigSpec.BooleanValue WAYSTONE_ACTIVATION;
        public final ModConfigSpec.BooleanValue ENTITY_FELLLED;
        public final ModConfigSpec.BooleanValue RAID_STATUS;

        Triggers(ModConfigSpec.Builder builder) {
            builder.comment("Triggers").push("triggers");

            CAMPFIRE_LIT = builder
                    .comment("Banner for lighting up campfires")
                    .define("campfire_lit", true);

            ENTITY_FELLLED = builder
                    .comment("Banner for fallen entities (entities must be chosen through datapacks).",
                            "By default, its all bosses tagged with forge:bosses.",
                            "Special banners are given to the Wither and Ender Dragon.")
                    .define("entity_felled", true);

            RAID_STATUS = builder
                    .comment("Banner for either raid victory or loss")
                    .define("raid_status", true);

            builder.comment("Compatibility").push("compatibility");
            BONFIRE_LIT = builder
                    .comment("Banner for activating bonfires [Bonfires Mod]")
                    .define("bonfire_lit", true);

            WAYSTONE_ACTIVATION = builder
                    .comment("Banner for activating waystones [Waystones Mod]")
                    .define("waystones_activate", true);

            builder.pop();
            builder.pop();
        }
    }

    public static class Appearance {
        public final ModConfigSpec.ConfigValue<Integer> OFFSET_Y;
        public final ModConfigSpec.BooleanValue TEXT_AUTOSCALE;
        public final ModConfigSpec.ConfigValue<Double> DEFAULT_TEXT_SCALE;
        public final ModConfigSpec.ConfigValue<String> DEFAULT_SOUND;
        public final ModConfigSpec.ConfigValue<String> DEFAULT_FONT;

        public SoundEvent getSound() {
            ResourceLocation id = ResourceLocation.parse(DEFAULT_SOUND.get());
            return BuiltInRegistries.SOUND_EVENT.get(id);
        }

        public ResourceLocation getFont() {
            return ResourceLocation.parse(DEFAULT_FONT.get());
        }

        Appearance(ModConfigSpec.Builder builder) {
            builder.comment("Appearance").push("appearance");

            OFFSET_Y = builder
                    .comment("Set Y offset of the entire message banner")
                    .define("offset_y", 0);

            TEXT_AUTOSCALE = builder
                    .comment("Should text fit into the screen")
                    .define("text_autoscale", true);

            DEFAULT_TEXT_SCALE = builder
                    .comment("Default default text scale")
                    .define("default_text_scale", 3.5);

            DEFAULT_SOUND = builder
                    .comment("Default sound")
                    .define("default_sound", "souls_message_banners:generic", obj -> {
                        if (!(obj instanceof String string)) return false;
                        return ResourceLocation.tryParse(string) != null;
                    });

            DEFAULT_FONT = builder
                    .comment("Default font")
                    .define("default_font", "souls_message_banners:optimus_principus", obj -> {
                        if (!(obj instanceof String string)) return false;
                        return ResourceLocation.tryParse(string) != null;
                    });

            builder.pop();
        }
    }
}