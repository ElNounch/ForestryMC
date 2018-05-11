package forestry.climatology.tiles;

import java.util.Arrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import forestry.api.climate.source.IClimateSource;
import forestry.api.climate.source.IClimateSourceProxy;
import forestry.core.blocks.BlockBase;
import forestry.core.climate.ClimateLogic;
import forestry.core.tiles.AdjacentTileCache;

public class AdjacentNodeCache implements AdjacentTileCache.ICacheListener {
	private final AdjacentTileCache cache;
	private final IClimateSource[] sides = new IClimateSource[6];
	private final ClimateLogic logic;
	private boolean changed = true;

	public AdjacentNodeCache(AdjacentTileCache cache, ClimateLogic logic) {
		this.cache = cache;
		this.logic = logic;
		cache.addListener(this);
	}

	@Override
	public void changed() {
		changed = true;
	}

	@Override
	public void purge() {
		for (IClimateSource source : sides) {
			logic.onRemoveSource(source);
		}
		Arrays.fill(sides, null);
	}

	public void checkChanged() {
		cache.refresh();
		if (changed) {
			changed = false;
			purge();
			TileEntity cacheSource = cache.getSource();
			IBlockState state = cacheSource.getWorld().getBlockState(cacheSource.getPos());
			EnumFacing facing = state.getValue(BlockBase.FACING);
			for (EnumFacing side : EnumFacing.HORIZONTALS) {
				if (side == facing) {
					continue;
				}
				TileEntity tile = cache.getTileOnSide(side);
				if (tile instanceof IClimateSourceProxy) {
					IClimateSource source = ((IClimateSourceProxy) tile).getNode();
					sides[side.ordinal()] = source;
					logic.onAddSource(source);
				}
			}
		}
	}
}
