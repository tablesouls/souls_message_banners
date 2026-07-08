package net.tablesouls.souls_message_banners.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, SoulsMessageBanners.MODID);

    public static final RegistryObject<SoundEvent> GENERIC = SOUND_EVENTS.register("generic",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SoulsMessageBanners.MODID, "generic")));

    public static final RegistryObject<SoundEvent> ENEMY_FELLED = SOUND_EVENTS.register("enemy_felled",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SoulsMessageBanners.MODID, "enemy_felled")));
}
