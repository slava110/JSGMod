package tauri.dev.jsg.worldgen.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class JSGWorldTopBlock {
    public int y;
    public Block topBlock;
    public IBlockState topBlockState;
    public JSGWorldTopBlock(int y, Block topBlock, IBlockState state){
        this.y = y;
        this.topBlock = topBlock;
        this.topBlockState = state;
    }

    @Nullable
    public static JSGWorldTopBlock getTopBlock(World world, int x, int z, int airCountUp, int dimensionId) {
        int y = (dimensionId == -1 ? 0 : world.getHeight());
        while (((dimensionId != -1 && y > 0) || (dimensionId == -1 && y < world.getHeight()))) {
            if(y < 240) {
                BlockPos pos = new BlockPos(x, y, z);
                Block block = world.getBlockState(pos).getBlock();
                boolean isAirUp = true;
                for (int i = 1; i <= airCountUp; i++) {
                    Block airBlock = world.getBlockState(new BlockPos(x, (y + i), z)).getBlock();
                    if (!block.isReplaceable(world, pos) && canBeTopBlock(airBlock)) {
                        isAirUp = false;
                        break;
                    }
                }
                if (!block.isReplaceable(world, pos) && isAirUp && canBeTopBlock(block))
                    return new JSGWorldTopBlock(y, block, world.getBlockState(pos));
            }
            y += (dimensionId == -1 ? 1 : -1);
        }
        return null;
    }

    private static boolean canBeTopBlock(Block block){
        if(block == Blocks.SNOW_LAYER) return false;
        if(block == Blocks.AIR) return false;
        if(block == Blocks.LEAVES) return false;
        if(block == Blocks.LEAVES2) return false;
        if(block == Blocks.LOG) return false;
        if(block == Blocks.LOG2) return false;
        return block != Blocks.BEDROCK;
    }
}