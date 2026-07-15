package net.tablesouls.souls_message_banners.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.minecraft.core.registries.BuiltInRegistries;

public class SoulsMessageBannersConfig {
    public static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec COMMON_SPEC;

    public static final ModConfigSpec.BooleanValue CAMPFIRE_LIT;
    public static final ModConfigSpec.BooleanValue BONFIRE_LIT;
    public static final ModConfigSpec.BooleanValue WAYSTONE_ACTIVATION;
    public static final ModConfigSpec.BooleanValue ENTITY_FELLLED;
    public static final ModConfigSpec.BooleanValue RAID_STATUS;

    public static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec CLIENT_SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> Y_OFFSET;
    public static final ModConfigSpec.BooleanValue TEXT_AUTOSCALE;
    public static final ModConfigSpec.ConfigValue<Double> DEFAULT_TEXT_SCALE;
    public static final ModConfigSpec.ConfigValue<String> DEFAULT_SOUND;
    public static final ModConfigSpec.ConfigValue<String> DEFAULT_FONT;

    static {
        COMMON_BUILDER.push("Common Config");
        COMMON_BUILDER.comment("Triggers").push("triggers");

        CAMPFIRE_LIT = COMMON_BUILDER
                .comment("Banner for lighting up campfires")
                .define("campfire_lit", true);

        ENTITY_FELLLED = COMMON_BUILDER
                .comment("Banner for fallen entities (entities must be chosen through datapacks).",
                        "By default, its all bosses tagged with forge:bosses.",
                        "Special banners are given to the Wither and Ender Dragon.")
                .define("entity_felled", true);

        RAID_STATUS = COMMON_BUILDER
                .comment("Banner for either raid victory or loss")
                .define("raid_status", true);

        COMMON_BUILDER.comment("Compatibility").push("compatibility");
        BONFIRE_LIT = COMMON_BUILDER
                .comment("Banner for activating bonfires [Bonfires Mod]")
                .define("bonfire_lit", true);

        WAYSTONE_ACTIVATION = COMMON_BUILDER
                .comment("Banner for activating waystones [Waystones Mod]")
                .define("waystones_activate", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();
        COMMON_SPEC = COMMON_BUILDER.build();

        CLIENT_BUILDER
                .comment("Customizations in configs are overwritten by banner styles",
                        "May require reloading",
                        "Change existing banner styles via resourcepacks",
                        "Choose custom mob death message via datapacks")
                .push("Client Config");

        CLIENT_BUILDER.comment("Appearance").push("appearance");

        Y_OFFSET = CLIENT_BUILDER
                .comment("Set Y offset of the entire message banner")
                .define("y_offset", 0);

        TEXT_AUTOSCALE = CLIENT_BUILDER
                .comment("Should text fit into the screen")
                .define("text_autoscale", true);

        DEFAULT_TEXT_SCALE = CLIENT_BUILDER
                .comment("Default default text scale")
                .define("default_text_scale", 3.5);

        DEFAULT_SOUND = CLIENT_BUILDER
                .comment("Default sound")
                .define("default_sound", "souls_message_banners:generic", obj -> {
                    if (!(obj instanceof String string)) return false;
                    return ResourceLocation.tryParse(string) != null;
                });

        DEFAULT_FONT = CLIENT_BUILDER
                .comment("Default font")
                .define("default_font", "souls_message_banners:optimus_principus", obj -> {
                    if (!(obj instanceof String string)) return false;
                    return ResourceLocation.tryParse(string) != null;
                });
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.pop();
        CLIENT_SPEC = CLIENT_BUILDER.build();
    }

    public static SoundEvent getSound() {
        ResourceLocation id = ResourceLocation.parse(DEFAULT_SOUND.get());
        return BuiltInRegistries.SOUND_EVENT.get(id);
    }

    public static ResourceLocation getFont() {
        return ResourceLocation.parse(DEFAULT_FONT.get());
    }
}