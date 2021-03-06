package ben_mkiv.ocdevices.common.nanoAnalyzer.events;

import ben_mkiv.ocdevices.common.nanoAnalyzer.capability.IanalyzeCapability;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static ben_mkiv.ocdevices.common.nanoAnalyzer.capability.analyzeCapability.ANALYZE;

public class TooltipEvent {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void tooltipEvent(ItemTooltipEvent evt){
        if(!evt.getItemStack().hasCapability(ANALYZE, null))
            return;

        IanalyzeCapability cap = evt.getItemStack().getCapability(ANALYZE, null);

        if(!cap.isAttached())
            return;

        evt.getToolTip().add("§2OCDevices NanoAnalyzer");
    }
}
