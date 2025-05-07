package co.killionrevival.sellfromshulker.listeners;

import co.killionrevival.sellfromshulker.SellFromShulker;
import co.killionrevival.sellfromshulker.utils.Sell;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.util.Transaction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class TransactionListener implements Listener {
    @EventHandler
    public void onPreTransaction(PreTransactionEvent event) {
        SellFromShulker.getMyLogger().sendDebug("Got PreTransactionEvent for shopstand sell");
        if (event.isCancelled()) {
            SellFromShulker.getMyLogger().sendDebug("PreTransactionEvent was cancelled before it was handled by us");
            return;
        }

        if (event.getTransactionType() != Transaction.Type.SHOPSTAND_SELL_SCREEN) {
            SellFromShulker.getMyLogger().sendDebug("Transaction type was not SHOPSTAND_SELL_SCREEN");
            return;
        }

        SellFromShulker.getMyLogger().sendDebug("Transaction amount was " + event.getAmount());
        if (event.getAmount() != 2) {
            SellFromShulker.getMyLogger().sendDebug("Transaction amount was not 2. Is not the shulker button");
            return;
        }

        Player player = event.getPlayer();
        ItemStack playerHeldItem = player.getInventory().getItemInMainHand();
        if (!playerHeldItem.getType().name().toLowerCase().endsWith("shulker_box")) {
            SellFromShulker.getMyLogger().sendDebug("Player did not have a shulker box in their hand.");
            player.sendMessage(Component.text("You do not have a shulker box in your hand.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission("sellfromshulker.sell")) {
            SellFromShulker.getMyLogger().sendDebug(String.format("Player %s did not have permission to sell from shulker", player.getName()));
            player.sendMessage(Component.text("You do not have permission to sell from your shulker.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        Sell.sellFromShulker(player, event.getShopItem().getShopItem(), event.getPrice(), event.getShopItem().getItemPath());
        event.setCancelled(true);
    }

    @EventHandler
    public void onPostTransaction(PostTransactionEvent event) {
        SellFromShulker.getMyLogger().sendDebug("Got PostTransactionEvent for shopstand sell");

        double price = EconomyShopGUIHook.getItemSellPrice(event.getShopItem(), event.getShopItem().getShopItem(), event.getPlayer());

        if (!event.getTransactionType().equals(Transaction.Type.SHOPSTAND_SELL_SCREEN)) {
            SellFromShulker.getMyLogger().sendDebug("Transaction type was not SHOPSTAND_SELL_SCREEN");
            return;
        }

        if (!event.getTransactionResult().equals(Transaction.Result.NO_ITEMS_FOUND)) {
            SellFromShulker.getMyLogger().sendDebug("Result was not NO_ITEMS_FOUND");
            return;
        }

        Player player = event.getPlayer();
        ItemStack playerHeldItem = player.getInventory().getItemInMainHand();
        if (!playerHeldItem.getType().name().toLowerCase().endsWith("shulker_box")) {
            SellFromShulker.getMyLogger().sendDebug("Player did not have a shulker box in their hand.");
            return;
        }

        if (!player.hasPermission("sellfromshulker.sell")) {
            player.sendMessage(Component.text("You do not have permission to sell from your shulker.").color(NamedTextColor.RED));
            SellFromShulker.getMyLogger().sendDebug(String.format("Player %s did not have permission to sell from shulker", player.getName()));
            return;
        }

        Sell.sellFromShulker(player, event.getShopItem().getShopItem(), price, event.getShopItem().getItemPath());
    }
}

