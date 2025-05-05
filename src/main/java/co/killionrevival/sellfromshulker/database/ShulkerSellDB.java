package co.killionrevival.sellfromshulker.database;

import co.killionrevival.killioncommons.database.DatabaseConnection;
import co.killionrevival.sellfromshulker.SellFromShulker;

public class ShulkerSellDB extends DatabaseConnection {
    private static ShulkerSellDB instance;

    private ShulkerSellDB() {
        super(SellFromShulker.getMyLogger(), SellFromShulker.getPlugin());
    }

    public static ShulkerSellDB getInstance() {
        if (instance == null) {
            instance = new ShulkerSellDB();
        }
        return instance;
    }
}
