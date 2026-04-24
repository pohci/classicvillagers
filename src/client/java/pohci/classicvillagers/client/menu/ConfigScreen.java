package pohci.classicvillagers.client.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import pohci.classicvillagers.client.config.ClientHitboxRefresh;
import pohci.classicvillagers.config.FeatureConfig;
import pohci.classicvillagers.config.ModConfig;

public class ConfigScreen extends Screen {
	private static final int PANEL_W = 280;
	private static final int PANEL_H = 168;
	private static final int PANEL_BG_COLOR = 0xF0121412;
	private static final int PANEL_RIM_COLOR = 0xFF5E7068;
	private static final int TITLE_COLOR = 0xFFE8E8E8;
	private static final Component TITLE_CLASSIC = Component.literal("Classic").withStyle(Style.EMPTY.withBold(true));
	private static final Component TITLE_VILLAGERS = Component.literal("Villagers").withStyle(Style.EMPTY.withBold(true));

	private final Screen parent;
	private int panelLeft;
	private int panelTop;
	private StringWidget statusWidget;
	private PanelButton toggleButton;
	private PanelButton doneButton;

	public ConfigScreen(Screen parent) {
		super(Component.translatable("classicvillagers.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		if (this.minecraft == null) {
			return;
		}

		panelLeft = (this.width - PANEL_W) / 2;
		panelTop = (this.height - PANEL_H) / 2;

		Minecraft game = this.minecraft;
		Font font = game.font;
		int innerLeft = panelLeft + 20;
		int innerW = PANEL_W - 40;
		int y = panelTop + 52;

		statusWidget = new StringWidget(innerLeft, y, innerW, 12, currentStatus(), font);
		addRenderableOnly(statusWidget);
		y += 18;

		toggleButton = new PanelButton(
			innerLeft,
			y,
			innerW,
			24,
			toggleActionLabel(),
			false,
			() -> {
				ModConfig.setBabyVillagerHitboxEnabled(!FeatureConfig.isBabyVillagerHitboxEnabled());
				ClientHitboxRefresh.refreshVisibleVillagers();
				toggleButton.setMessage(toggleActionLabel());
				statusWidget.setMessage(currentStatus());
			}
		);
		addRenderableWidget(toggleButton);
		y += 32;

		StringWidget path = new StringWidget(innerLeft, y, innerW, 24, Component.translatable("classicvillagers.config.path"), font);
		path.setMaxWidth(innerW);
		addRenderableOnly(path);
		y = panelTop + PANEL_H - 34;

		doneButton = new PanelButton(
			innerLeft,
			y,
			innerW,
			24,
			Component.translatable("classicvillagers.config.done"),
			true,
			() -> {
				if (game.screen == this) {
					game.setScreen(parent);
				}
			}
		);
		addRenderableWidget(doneButton);
	}

	private Component currentStatus() {
		Component value = Component.translatable(
			FeatureConfig.isBabyVillagerHitboxEnabled() ? "classicvillagers.hitbox.skinny" : "classicvillagers.hitbox.fat"
		);
		return Component.translatable("classicvillagers.config.current", value);
	}

	private Component toggleActionLabel() {
		if (FeatureConfig.isBabyVillagerHitboxEnabled()) {
			return Component.translatable("classicvillagers.config.switchToFat");
		}
		return Component.translatable("classicvillagers.config.switchToSkinny");
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {

		int x0 = panelLeft;
		int y0 = panelTop;
		int x1 = panelLeft + PANEL_W;
		int y1 = panelTop + PANEL_H;
		drawPanel(extractor, x0, y0, x1, y1);

		if (minecraft == null) {
			super.extractRenderState(extractor, mouseX, mouseY, partialTick);
			return;
		}
		drawTitle(extractor, minecraft.font);

		int innerX1 = x0 + 1;
		int innerY1 = y0 + 1;
		int innerX2 = x1 - 1;
		int innerY2 = y1 - 1;
		extractor.enableScissor(innerX1, innerY1, innerX2, innerY2);
		try {
			super.extractRenderState(extractor, mouseX, mouseY, partialTick);
		} finally {
			extractor.disableScissor();
		}
	}

	private static void drawPanel(GuiGraphicsExtractor extractor, int x0, int y0, int x1, int y1) {
		extractor.fill(x0, y0, x1, y1, PANEL_BG_COLOR);
		extractor.horizontalLine(x0, x1 - 1, y0, PANEL_RIM_COLOR);
		extractor.horizontalLine(x0, x1 - 1, y1 - 1, PANEL_RIM_COLOR);
		extractor.verticalLine(x0, y0, y1 - 1, PANEL_RIM_COLOR);
		extractor.verticalLine(x1 - 1, y0, y1 - 1, PANEL_RIM_COLOR);
	}

	private void drawTitle(GuiGraphicsExtractor extractor, Font font) {
		int cx = this.width / 2;
		int yTitle = panelTop + 14;
		extractor.centeredText(font, TITLE_CLASSIC, cx, yTitle, TITLE_COLOR);
		extractor.centeredText(font, TITLE_VILLAGERS, cx, yTitle + 12, TITLE_COLOR);
	}

}
