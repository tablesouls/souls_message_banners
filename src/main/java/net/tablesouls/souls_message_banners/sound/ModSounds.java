package net.tablesouls.souls_message_banners.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, SoulsMessageBanners.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GENERIC = SOUND_EVENTS.register("generic",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "generic")));

    public static final DeferredHolder<SoundEvent, SoundEvent> ENEMY_FELLED = SOUND_EVENTS.register("enemy_felled",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "enemy_felled")));

    public static final DeferredHolder<SoundEvent, SoundEvent> HOST_VANQUISHED = SOUND_EVENTS.register("host_vanquished",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "host_vanquished")));

    public static final DeferredHolder<SoundEvent, SoundEvent> INVADER_VANQUISHED = SOUND_EVENTS.register("invader_vanquished",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "invader_vanquished")));
}