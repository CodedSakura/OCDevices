package ben_mkiv.ocdevices.common.drivers;

import ben_mkiv.ocdevices.common.blocks.BlockRecipeDictionary;
import ben_mkiv.ocdevices.common.component.ManagedDatabaseComponent;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.item.HostAware;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.common.Tier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ManagedDatabaseDriver implements DriverItem, EnvironmentProvider, HostAware {
    public static ManagedDatabaseDriver driver = new ManagedDatabaseDriver();

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().equals(new ItemStack(BlockRecipeDictionary.DEFAULTITEM, 1).getItem());
    }

    @Override
    public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
        return worksWith(stack);
    }

    @Override
    public Class<? extends Environment> getEnvironment(ItemStack stack) {
        return worksWith(stack) ? ManagedDatabaseComponent.class : null;
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost container) {
        return new ManagedDatabaseComponent(container, 0, "what?");
    }

    @Override
    public String slot(ItemStack stack){
        return Slot.None;
    }

    @Override
    public int tier(ItemStack stack) {
        return Tier.Four();
    }

    @Override
    public NBTTagCompound dataTag(ItemStack stack) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        final NBTTagCompound nbt = stack.getTagCompound();
        // This is the suggested key under which to store item component data.
        // You are free to change this as you please.
        if(!nbt.hasKey("oc:data")) {
            nbt.setTag("oc:data", new NBTTagCompound());
        }
        return nbt.getCompoundTag("oc:data");
    }
}
