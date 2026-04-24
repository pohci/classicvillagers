package pohci.classicvillagers.client.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

public final class PanelButton extends AbstractButton {
	private final Runnable press;
	private final boolean primary;

	public PanelButton(int x, int y, int w, int h, Component message, boolean primary, Runnable press) {
		super(x, y, w, h, message);
		this.primary = primary;
		this.press = press;
	}

	@Override
	public void onPress(InputWithModifiers input) {
		press.run();
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		defaultButtonNarrationText(output);
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		int x = getX();
		int y = getY();
		int w = getWidth();
		int h = getHeight();
		boolean hover = isHoveredOrFocused();
		int face;
		int rim;
		int cap;
		if (primary) {
			face = hover ? 0xFF2D5A42 : 0xFF244A36;
			rim = 0xFF5C9A72;
			cap = 0xFF3D8264;
		} else {
			face = hover ? 0xFF2F3D38 : 0xFF252E2A;
			rim = 0xFF6B7B74;
			cap = 0xFF3A4540;
		}
		g.fill(x + 1, y + 1, x + w - 1, y + h - 1, face);
		g.fill(x + 2, y + 2, x + w - 2, y + 4, cap);
		g.horizontalLine(x, x + w - 1, y, rim);
		g.horizontalLine(x, x + w - 1, y + h - 1, rim);
		g.verticalLine(x, y, y + h - 1, rim);
		g.verticalLine(x + w - 1, y, y + h - 1, rim);

		int textColor = active ? 0xFFF5F5F5 : 0xFF9A9A9A;
		g.centeredText(Minecraft.getInstance().font, getMessage(), x + w / 2, y + (h - 8) / 2, textColor);
	}
}
