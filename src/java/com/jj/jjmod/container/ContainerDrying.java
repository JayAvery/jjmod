package com.jj.jjmod.container;

import javax.annotation.Nullable;
import com.jj.jjmod.container.slots.SlotCrafting;
import com.jj.jjmod.tileentities.TEDrying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Container for Drying Rack. */
public class ContainerDrying extends ContainerAbstract {

    private static final int INPUT_X = 56;
    private static final int OUTPUT_X = 116;
    private static final int SLOTS_Y = 35;

    private static final int INPUT_I = 0;
    private static final int OUTPUT_I = 1;
    private static final int HOT_START = 2;
    private static final int HOT_END = 10;
    private static final int INV_START = 11;

    private final int invEnd;

    public final IInventory dryingInv;
    private int drySpent;
    private int dryEach;

    public ContainerDrying(EntityPlayer player, World world,
            IInventory dryingInv) {

        super(player, world);
        this.dryingInv = dryingInv;

        // Drying slots
        this.addSlotToContainer(new Slot(this.dryingInv,
                INPUT_I, INPUT_X, SLOTS_Y));
        this.addSlotToContainer(new SlotFurnaceOutput(player,
                this.dryingInv, OUTPUT_I, OUTPUT_X, SLOTS_Y));

        // Inventory grid
        this.buildHotbar();
        int invIndex = this.buildInvgrid();

        // Container indices
        this.invEnd = INV_START + invIndex;
    }

    @Override
    public void addListener(IContainerListener listener) {

        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.dryingInv);
    }

    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            
            IContainerListener icontainerlistener =
                    this.listeners.get(i);

            if (this.drySpent != this.dryingInv.getField(0)) {

                icontainerlistener.sendProgressBarUpdate(this, 0,
                        this.dryingInv.getField(0));
            }

            if (this.dryEach != this.dryingInv.getField(1)) {

                icontainerlistener.sendProgressBarUpdate(this, 1,
                        this.dryingInv.getField(1));
            }
        }

        this.drySpent = this.dryingInv.getField(0);
        this.dryEach = this.dryingInv.getField(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {

        this.dryingInv.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return this.dryingInv.isUsableByPlayer(player);
    }

    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {

            ItemStack stack1 = slot.getStack();
            itemstack = stack1.copy();

            if (index == OUTPUT_I) {

                if (!this.mergeItemStack(stack1, HOT_START,
                        this.invEnd + 1, true)) {

                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(stack1, itemstack);

            } else if (index != INPUT_I) {

                if (((TEDrying) this.dryingInv).recipes
                        .getSmeltingResult(stack1) != null) {

                    if (!this.mergeItemStack(stack1, INPUT_I,
                            INPUT_I + 1, false)) {

                        return ItemStack.EMPTY;
                    }

                } else if (index >= INV_START && index <= this.invEnd) {

                    if (!this.mergeItemStack(stack1, HOT_START,
                            HOT_END + 1, false)) {

                        return ItemStack.EMPTY;
                    }

                } else if (index >= HOT_START && index <= HOT_END)

                    if (!this.mergeItemStack(stack1, INV_START,
                            this.invEnd + 1, false)) {

                        return ItemStack.EMPTY;
                    }

            } else if (!this.mergeItemStack(stack1, HOT_START,
                    this.invEnd + 1, false)) {

                return ItemStack.EMPTY;
            }

            if (stack1.getCount() == 0) {

                slot.putStack(ItemStack.EMPTY);

            } else {

                slot.onSlotChanged();
            }

            if (stack1.getCount() == itemstack.getCount()) {

                return ItemStack.EMPTY;
            }

            ((SlotCrafting) slot).onTake(player, stack1);
        }

        return itemstack;
    }
}
