package co.killionrevival.sellfromshulker.database.models;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class History {
    String playerUUID;
    Timestamp createdOn;
    String material;
    int amount;
    double pricePerItem;
    double totalPrice;
    String itemPath;
}
