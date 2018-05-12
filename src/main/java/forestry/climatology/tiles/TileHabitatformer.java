package forestry.climatology.tiles;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.Biome;

import forestry.api.circuits.ChipsetManager;
import forestry.api.circuits.CircuitSocketType;
import forestry.api.circuits.ICircuitBoard;
import forestry.api.circuits.ICircuitSocketType;
import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateLogic;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.climatology.gui.ContainerHabitatformer;
import forestry.climatology.gui.GuiHabitatformer;
import forestry.core.circuits.ISocketable;
import forestry.core.climate.ClimateLogic;
import forestry.core.inventory.InventoryAdapter;
import forestry.core.network.PacketBufferForestry;
import forestry.core.tiles.IClimatised;
import forestry.core.tiles.TilePowered;

public class TileHabitatformer extends TilePowered implements IClimateHousing, IClimatised, ISocketable {
	private static final String LOGIC_NBT_KEY = "Logic";

	//A cache that caches every neighboring tile that contains a climate source
	private final AdjacentNodeCache nodeCache;
	//A inventory that contains the circuits of this tile.
	private final InventoryAdapter sockets = new InventoryAdapter(1, "sockets");

	//The logic that handles the climate  changes.
	private final ClimateLogic logic;

	public TileHabitatformer() {
		super(800, 10000);
		this.logic = new ClimateLogic(this);
		this.nodeCache = new AdjacentNodeCache(getTileCache(), logic);
	}

	@Override
	protected void updateServerSide() {
		nodeCache.checkChanged();
		logic.update();
		super.updateServerSide();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		logic.invalidate();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		logic.invalidate();
	}

	@Override
	public boolean hasWork() {
		return false;
	}

	@Override
	protected boolean workCycle() {
		return false;
	}

	@Override
	public GuiContainer getGui(EntityPlayer player, int data) {
		return new GuiHabitatformer(player, this);
	}

	@Override
	public Container getContainer(EntityPlayer player, int data) {
		return new ContainerHabitatformer(player.inventory, this);
	}

	@Override
	public EnumTemperature getTemperature() {
		return EnumTemperature.getFromValue(getExactTemperature());
	}

	@Override
	public EnumHumidity getHumidity() {
		return EnumHumidity.getFromValue(getExactHumidity());
	}

	@Override
	public Biome getBiome() {
		return world.getBiome(getPos());
	}

	@Override
	public float getExactTemperature() {
		return logic.getCurrent().getTemperature();
	}

	@Override
	public float getExactHumidity() {
		return logic.getCurrent().getHumidity();
	}

	/* Methods - Implement IGreenhouseHousing */
	@Override
	public IClimateLogic getLogic() {
		return logic;
	}

	/* Methods - Implement IStreamableGui */
	@Override
	public void writeGuiData(PacketBufferForestry data) {
		super.writeGuiData(data);
		logic.writeData(data);
	}

	@Override
	public void readGuiData(PacketBufferForestry data) throws IOException {
		super.readGuiData(data);
		logic.readData(data);
	}

	/* Methods - Implement ISocketable */
	@Override
	public int getSocketCount() {
		return sockets.getSizeInventory();
	}

	@Override
	public ItemStack getSocket(int slot) {
		return sockets.getStackInSlot(slot);
	}

	@Override
	public void setSocket(int slot, ItemStack stack) {

		if (!stack.isEmpty() && !ChipsetManager.circuitRegistry.isChipset(stack)) {
			return;
		}

		// Dispose correctly of old chipsets
		if (!sockets.getStackInSlot(slot).isEmpty()) {
			if (ChipsetManager.circuitRegistry.isChipset(sockets.getStackInSlot(slot))) {
				ICircuitBoard chipset = ChipsetManager.circuitRegistry.getCircuitBoard(sockets.getStackInSlot(slot));
				if (chipset != null) {
					chipset.onRemoval(this);
				}
			}
		}

		sockets.setInventorySlotContents(slot, stack);
		if (stack.isEmpty()) {
			return;
		}

		ICircuitBoard chipset = ChipsetManager.circuitRegistry.getCircuitBoard(stack);
		if (chipset != null) {
			chipset.onInsertion(this);
		}
	}

	@Override
	public ICircuitSocketType getSocketType() {
		return CircuitSocketType.HABITAT_FORMER;
	}

	/* Methods - SAVING & LOADING */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);

		data.setTag(LOGIC_NBT_KEY, logic.writeToNBT(new NBTTagCompound()));

		sockets.writeToNBT(data);

		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);

		if (data.hasKey(LOGIC_NBT_KEY)) {
			NBTTagCompound nbtTag = data.getCompoundTag(LOGIC_NBT_KEY);
			logic.readFromNBT(nbtTag);
		}

		sockets.readFromNBT(data);

		ItemStack chip = sockets.getStackInSlot(0);
		if (!chip.isEmpty()) {
			ICircuitBoard chipset = ChipsetManager.circuitRegistry.getCircuitBoard(chip);
			if (chipset != null) {
				chipset.onLoad(this);
			}
		}
	}

	public void changeClimateConfig(float changeChange, float rangeChange, float energyChange) {
		logic.changeClimateConfig(changeChange, rangeChange, energyChange);
	}
}
