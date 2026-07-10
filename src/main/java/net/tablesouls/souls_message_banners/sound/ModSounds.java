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

    public static final RegistryObject<SoundEvent> HOST_VANQUISHED = SOUND_EVENTS.register("host_vanquished",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SoulsMessageBanners.MODID, "host_vanquished")));

    public static final RegistryObject<SoundEvent> INVADER_VANQUISHED = SOUND_EVENTS.register("invader_vanquished",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SoulsMessageBanners.MODID, "invader_vanquished")));
}
