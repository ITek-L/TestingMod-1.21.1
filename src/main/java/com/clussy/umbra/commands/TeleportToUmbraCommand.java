package com.clussy.umbra.commands;

import com.clussy.umbra.UmbraMod; // Import your main mod class
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack; // Correct import for CommandSourceStack
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition; // Import DimensionTransition
import net.minecraft.world.phys.Vec3; // Import Vec3

public class TeleportToUmbraCommand {

    // Register the command
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gotoumbra")
                .requires(source -> source.hasPermission(2)) // Requires permission level 2 (usually Op)
                .executes(context -> teleportToUmbra(context.getSource()))); // Execute the teleport logic
    }

    // Teleport logic
    private static int teleportToUmbra(CommandSourceStack source) throws CommandSyntaxException {
        Entity entity = source.getEntityOrException(); // Get the entity (player) running the command
        ServerLevel currentLevel = (ServerLevel) entity.level(); // Get the current level
        ServerLevel umbraLevel = source.getServer().getLevel(UmbraMod.UMBRA_DIMENSION_KEY); // Get the Umbra dimension level

        if (umbraLevel == null) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("Umbra dimension not found!"));
            return 0; // Command failed
        }

        if (currentLevel.dimension() == UmbraMod.UMBRA_DIMENSION_KEY) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("You are already in the Umbra dimension!"));
            return 0; // Command failed
        }

        if (entity instanceof ServerPlayer player) {
            // Create a DimensionTransition object for teleportation in 1.20+
            DimensionTransition transition = new DimensionTransition(
                    umbraLevel, // Target level
                    player.position(), // Suggested destination position (game finds a safe spot)
                    Vec3.ZERO, // Portal physics (use ZERO for command teleport)
                    player.getYRot(), // Maintain player's Y rotation
                    player.getXRot(), // Maintain player's X rotation
                    true, // Play portal sound? (set to true or false)
                    DimensionTransition.PLAY_PORTAL_SOUND // Use FORCED for a non-portal teleport
            );

            player.changeDimension(transition); // Perform the dimension change
            source.sendSuccess(() -> net.minecraft.network.chat.Component.literal("Teleported to the Umbra!"), true);
        } else {
            source.sendFailure(net.minecraft.network.chat.Component.literal("Only players can be teleported to the Umbra!"));
            return 0; // Command failed
        }

        return 1; // Command succeeded
    }
}