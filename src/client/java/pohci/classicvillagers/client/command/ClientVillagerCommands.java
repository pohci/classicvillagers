package pohci.classicvillagers.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;
import pohci.classicvillagers.client.config.ClientHitboxRefresh;
import pohci.classicvillagers.config.ModConfig;
import pohci.classicvillagers.util.CommandMessages;

public final class ClientVillagerCommands {
	private static final String ROOT = "classicvillagers";

	private ClientVillagerCommands() {
	}

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommands.literal(ROOT)
			.requires(source -> isLocalGameMaster(source.getPlayer()))
			.then(ClientCommands.literal("set")
				.then(ClientCommands.argument("value", BoolArgumentType.bool())
					.executes(context -> {
						boolean value = BoolArgumentType.getBool(context, "value");
						ModConfig.setBabyVillagerHitboxEnabled(value);
						ClientHitboxRefresh.refreshVisibleVillagers();
						context.getSource().sendFeedback(CommandMessages.hitboxSet(value));
						return 1;
					})))
			.then(ClientCommands.literal("reload")
				.executes(context -> {
					if (!ModConfig.reloadFromFile()) {
						context.getSource().sendError(
							Component.literal("Failed to reload ClassicVillagers config (check the log and classicvillagers.json)")
						);
						return 0;
					}
					ClientHitboxRefresh.refreshVisibleVillagers();
					context.getSource().sendFeedback(Component.literal("Reloaded baby villagers successfully"));
					return 1;
				})));
	}

	private static boolean isLocalGameMaster(Player player) {
		return player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
	}
}
