package ben_mkiv.ocdevices.client.gui;

import ben_mkiv.ocdevices.common.inventory.slots.ISlotTooltip;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class ContainerGUI extends GuiContainer {
    private static final int slotSize = 18;

    public ContainerGUI(Container inventorySlotsIn, int width, int height){
        super(inventorySlotsIn);
        xSize = width;
        ySize = height;
    }

    void drawCenteredString(String string, int y, int color){
        drawString(string, getXSize()/2 - mc.fontRenderer.getStringWidth(string)/2, y, color);
    }

    void drawStringAlignRight(String string, int x, int y, int color){
        drawString(string, x-mc.fontRenderer.getStringWidth(string), y, color);
    }

    void drawString(String string, int x, int y, int color){
        mc.fontRenderer.drawString(string, x, y, color);
    }

    private boolean isSlotHovered(Slot slot, int mouseX, int mouseY){
        return
                slot.xPos + guiLeft <= mouseX &&
                        slot.xPos + guiLeft + slotSize > mouseX &&
                        slot.yPos + guiTop <= mouseY &&
                        slot.yPos + guiTop + slotSize > mouseY;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);

        for(Slot slot : inventorySlots.inventorySlots){
            if(slot instanceof ISlotTooltip && isSlotHovered(slot, mouseX, mouseY)){
                drawHoveringText(((ISlotTooltip) slot).getTooltip(), mouseX, mouseY);
            }
        }

        renderHoveredToolTip(mouseX, mouseY);
    }
}
