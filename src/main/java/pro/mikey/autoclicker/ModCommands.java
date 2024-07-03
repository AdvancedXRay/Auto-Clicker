package pro.mikey.autoclicker;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

public class ModCommands {
    public static void registerCommands(){
        ClientCommandRegistrationCallback.EVENT.register(ModCommands::hudCommands);
    }

    private static void hudCommands(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal("autoclicker")
                .then(ClientCommandManager.literal("hud")
                    .then(ClientCommandManager.literal("setlocation")
                        .then(ClientCommandManager.argument("location", StringArgumentType.string())
                            .suggests((context, builder) -> {
                                builder.suggest("top-left");
                                builder.suggest("top-right");
                                builder.suggest("bottom-left");
                                builder.suggest("bottom-right");
                                return builder.buildFuture();
                                }).executes(context -> {
                                    AutoClicker.getInstance().config.getHudConfig().setLocation(StringArgumentType.getString(context, "location"));
                                    AutoClicker.getInstance().saveConfig();
                                   return 1;
                                }))
                    ).then(ClientCommandManager.literal("disable")
                        .executes(context -> {
                            AutoClicker.getInstance().config.getHudConfig().setEnabled(false);
                            AutoClicker.getInstance().saveConfig();
                            return 1;
                        })
                    ).then(ClientCommandManager.literal("enable")
                        .executes(context -> {
                            AutoClicker.getInstance().config.getHudConfig().setEnabled(true);
                            AutoClicker.getInstance().saveConfig();
                            return 1;
                        })
        )));
    }
}
