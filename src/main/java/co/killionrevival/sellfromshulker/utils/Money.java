package co.killionrevival.sellfromshulker.utils;

import co.killionrevival.sellfromshulker.SellFromShulker;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

import java.util.UUID;

public class Money {
    public static void giveMoney(UUID player, double amount) {
        Currency currency = CoinsEngineAPI.getCurrency("Money");
        if (currency == null) {
            SellFromShulker.getMyLogger().sendError("Currency 'Money' not found.");
            return;
        }
        CoinsEngineAPI.addBalance(player, currency, amount);
        SellFromShulker.getMyLogger().sendDebug("Gave " + amount + " Money to " + player.toString());
    }
}
