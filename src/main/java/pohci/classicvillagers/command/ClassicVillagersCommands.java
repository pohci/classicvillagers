package pohci.classicvillagers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import pohci.classicvillagers.config.ModConfig;
import pohci.classicvillagers.config.ServerHitboxRefresh;
import pohci.classicvillagers.util.CommandMessages;

public final class ClassicVillagersCommands {
	private static final String ROOT = "classicvillagers";

	private ClassicVillagersCommands() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal(ROOT)
			.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
			.then(Commands.literal("set")
				.then(Commands.argument("value", BoolArgumentType.bool())
					.executes(context -> {
						boolean value = BoolArgumentType.getBool(context, "value");
						ModConfig.setBabyVillagerHitboxEnabled(value);
						int refreshed = ServerHitboxRefresh.refreshAllVillagers(context.getSource().getServer());
						context.getSource().sendSuccess(
							() -> CommandMessages.hitboxSet(value)
								.plainCopy()
								.append(Component.literal(" (" + refreshed + " baby villagers refreshed)")),
							true
						);
						return 1;
					})))
			.then(Commands.literal("model")
				.then(Commands.literal("new")
					.executes(context -> {
						ModConfig.setBabyVillagerOldModelEnabled(false);
						context.getSource().sendSuccess(
							() -> Component.literal("Baby villager model set to New"),
							true
						);
						return 1;
					}))
				.then(Commands.literal("old")
					.executes(context -> {
						ModConfig.setBabyVillagerOldModelEnabled(true);
						context.getSource().sendSuccess(
							() -> Component.literal("Baby villager model set to Old"),
							true
						);
						return 1;
					})))
			.then(Commands.literal("reload")
				.executes(context -> {
					if (!ModConfig.reloadFromFile()) {
						context.getSource().sendFailure(Component.literal("Failed to reload ClassicVillagers config (check the log and classicvillagers.json)"));
						return 0;
					}
					int refreshed = ServerHitboxRefresh.refreshAllVillagers(context.getSource().getServer());
					context.getSource().sendSuccess(
						() -> Component.literal("Reloaded baby villagers successfully (" + refreshed + " refreshed)"),
						true
					);
					return 1;
				})));
	}
}
