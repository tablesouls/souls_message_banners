package net.tablesouls.souls_message_banners.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateHelper {
    public static void onBlockStateChanged(ServerLevel level, BlockPos pos, BlockState stateBefore, Runnable onChanged) {
        level.getServer().tell(new TickTask(level.getServer().getTickCount(), () -> {
            BlockState stateAfter = level.getBlockState(pos);
            if (!stateAfter.equals(stateBefore)) {
                onChanged.run();
            }
        }));
    }
}