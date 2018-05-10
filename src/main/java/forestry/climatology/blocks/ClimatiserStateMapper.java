package forestry.climatology.blocks;

import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.core.blocks.BlockBase;
import forestry.core.render.ForestryStateMapper;

@SideOnly(Side.CLIENT)
public class ClimatiserStateMapper extends ForestryStateMapper {
	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block) {
		for (EnumFacing facing : EnumFacing.values()) {
			if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
				continue;
			}
			for (State state : State.values()) {
				IBlockState blockState = block.getDefaultState().withProperty(BlockBase.FACING, facing).withProperty(State.PROPERTY, state);
				LinkedHashMap<IProperty<?>, Comparable<?>> properties = Maps.newLinkedHashMap(blockState.getProperties());
				ResourceLocation blockLocation = Block.REGISTRY.getNameForObject(block);
				String s = String.format("%s:%s", blockLocation.getResourceDomain(), "habitatformer/" + blockLocation.getResourcePath());
				mapStateModelLocations.put(blockState, new ModelResourceLocation(s, getPropertyString(properties)));
			}
		}

		return this.mapStateModelLocations;
	}
}
