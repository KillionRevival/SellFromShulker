package co.killionrevival.sellfromshulker.database;

import co.killionrevival.killioncommons.database.DataAccessObject;
import co.killionrevival.sellfromshulker.SellFromShulker;
import co.killionrevival.sellfromshulker.database.models.History;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryDao extends DataAccessObject<History> {
    private static HistoryDao instance;

    private HistoryDao(final ShulkerSellDB db) {
        super(db);
        createSchema();
        createTable();
    }

    public static HistoryDao getInstance() {
        if (instance == null) {
            instance = new HistoryDao(ShulkerSellDB.getInstance());
        }
        return instance;
    }

    @Override
    public List<History> parse(ResultSet resultSet) throws SQLException {
        final List<History> historyList = new ArrayList<>();
        while (resultSet != null && resultSet.next()) {
            String playerUUID = resultSet.getString("player_uuid");
            Timestamp createdOn = resultSet.getTimestamp("created_on");
            String material = resultSet.getString("material");
            int amount = resultSet.getInt("amount");
            double pricePerItem = resultSet.getDouble("price_per_item");
            double totalPrice = resultSet.getDouble("total_price");
            String itemPath = resultSet.getString("item_path");

            History history = new History(playerUUID, createdOn, material, amount, pricePerItem, totalPrice, itemPath);
            historyList.add(history);
        }
        return historyList;
    }

    private void createSchema() {
        createSchemaIfNotExists("shulker_sell");
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS shulker_sell.history (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, player_uuid TEXT, created_on TIME DEFAULT CURRENT_TIMESTAMP, material TEXT, amount INTEGER, price_per_item DOUBLE PRECISION, total_price DOUBLE PRECISION, item_path TEXT);";
        try {
            executeQuery(query);
        } catch (Exception e) {
            SellFromShulker.getMyLogger().sendError("Failed to create table: " + e.getMessage());
        }
    }

    public void addHistory(Player player, Material material, int amount, double pricePerItem, String itemPath) {
        String UUID = player.getUniqueId().toString();
        String materialName = material.name();
        double totalPrice = amount * pricePerItem;

        String query = "INSERT INTO shulker_sell.history (player_uuid, material, amount, price_per_item, total_price, item_path) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            executeUpdate(query, UUID, materialName, amount, pricePerItem, totalPrice, itemPath);
        } catch (Exception e) {
            SellFromShulker.getMyLogger().sendError("Failed to add history: " + e.getMessage());
        }

    }
}
