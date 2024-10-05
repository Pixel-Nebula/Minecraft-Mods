package com.example.inventorylossmod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// The main mod class
@Mod("inventorylossmod")
public class InventoryLossMod {

    public InventoryLossMod() {
        // Register this class for receiving events
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();

        // Check if the keepInventory game rule is set to true
        if (player.getLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            // Get all non-empty item stacks in the player's inventory
            List<ItemStack> nonEmptyStacks = new ArrayList<>();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty()) {
                    nonEmptyStacks.add(stack);
                }
            }

            int itemsToRemove = nonEmptyStacks.size() / 3; // Calculate the number of items to remove
            Random random = new Random();

            // Randomly remove 1/3 of the non-empty item stacks
            for (int i = 0; i < itemsToRemove; i++) {
                if (nonEmptyStacks.isEmpty()) break; // Break if no non-empty stacks are left

                // Pick a random index from the non-empty stacks
                int randomIndex = random.nextInt(nonEmptyStacks.size());
                ItemStack selectedStack = nonEmptyStacks.get(randomIndex);

                // Remove the selected stack entirely
                int slotIndex = -1; // Initialize to an invalid index
                for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                    if (player.getInventory().getItem(j) == selectedStack) {
                        slotIndex = j; // Find the slot index
                        break;
                    }
                }

                if (slotIndex >= 0 && slotIndex < player.getInventory().getContainerSize()) {
                    player.getInventory().setItem(slotIndex, ItemStack.EMPTY); // Set it to empty
                }

                // Remove the selected stack from the list after processing
                nonEmptyStacks.remove(randomIndex);
            }
        }
    }
}
