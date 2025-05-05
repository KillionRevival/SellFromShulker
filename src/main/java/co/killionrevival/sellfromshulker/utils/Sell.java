package co.killionrevival.sellfromshulker.utils;

import co.killionrevival.sellfromshulker.SellFromShulker;
import co.killionrevival.sellfromshulker.database.HistoryDao;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Sell {
    public static void sellFromShulker(Player player, ItemStack shopItem, double itemPrice, String itemPath) {
        HistoryDao historyDao = HistoryDao.getInstance();

        ItemStack playerHeldItem = player.getInventory().getItemInMainHand();
        String sellItemMaterialName = shopItem.getType().name();
        int totalItemsInShulker = 0;

        ItemMeta itemMeta = playerHeldItem.getItemMeta();
        if (itemMeta instanceof BlockStateMeta meta) {
            if (meta.getBlockState() instanceof ShulkerBox shulkerBox) {
                Inventory shulkerInventory = shulkerBox.getInventory();

                for (ItemStack content : shulkerInventory.getContents()) {
                    if (content != null && content.getType().name().equals(sellItemMaterialName)) {
                        int amountInStack = content.getAmount();
                        totalItemsInShulker += amountInStack;
                    }
                }

                if (totalItemsInShulker == 0) {
                    SellFromShulker.getMyLogger().sendDebug(String.format("Player %s did not have any %s in their shulker", player.getName(), sellItemMaterialName));
                    player.sendMessage(Component.text(String.format("You do not have any %s in your shulker", sellItemMaterialName)).color(NamedTextColor.RED));
                    return;
                }

                double totalPrice = itemPrice * totalItemsInShulker;

                // Remove the items from the shulker
                for (int i = 0; i < shulkerInventory.getSize(); i++) {
                    ItemStack item = shulkerInventory.getItem(i);
                    if (item != null && item.getType().name().equals(sellItemMaterialName)) {
                        SellFromShulker.getMyLogger().sendDebug(String.format("Removing %d %s from %s's shulker", item.getAmount(), sellItemMaterialName, player.getName()));
                        shulkerInventory.setItem(i, null);
                    }
                }

                shulkerBox.update();
                meta.setBlockState(shulkerBox);
                playerHeldItem.setItemMeta(meta);

                // Give the player the money
                Money.giveMoney(player.getUniqueId(), totalPrice);

                historyDao.addHistory(player, shopItem.getType(), totalItemsInShulker, itemPrice, itemPath);

                player.sendMessage(Component.text(String.format("You have sold %d %s from your shulker for $%.2f", totalItemsInShulker, sellItemMaterialName, totalPrice)).color(NamedTextColor.GREEN));

                SellFromShulker.getMyLogger().sendInfo(String.format("%s has sold %d %s from their shulker for $%.2f", player.getName(), totalItemsInShulker, sellItemMaterialName, totalPrice));
            }
        }
    }
}
