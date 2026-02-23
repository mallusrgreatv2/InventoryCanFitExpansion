package me.mallusrgreat.inventorycanfit;

import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class InventoryCanFitExpansion extends PlaceholderExpansion implements Configurable {
    @Override
    @NonNull
    public String getAuthor() {
        return "mallusrgreat";
    }

    @NonNull
    @Override
    public String getIdentifier() {
        return "inventorycanfit";
    }

    @NonNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NonNull String params) {
        boolean debug = getBoolean("debug", false);
        String prefix = "Got placeholder: " + params + " | ";
        if (player == null) {
            if (debug) info(prefix + "Player is null!");
            return null;
        }
        String[] split = params.split("_");
        String itemName = String.join("_", Arrays.copyOfRange(split, 0, split.length - 1));
        Integer amount = null;
        try {
            amount = Integer.parseInt(split[split.length - 1]);
        } catch (NumberFormatException ignored) {}
        if (amount == null) {
            if (debug) info(prefix + "Amount is null!");
            return null;
        }
        if (amount <= 0) {
            if (debug) info(prefix + "Amount is less than or equal to zero!");
            return null;
        }
        NamespacedKey key = NamespacedKey.fromString(itemName);
        if (key == null) {
            if (debug) info(prefix + "NamespacedKey is null!");
            return null;
        }
        Material material = Registry.MATERIAL.get(key);
        if (material == null) {
            if (debug) info(prefix + "Material is null!");
            return null;
        }
        return String.valueOf(canFit(player.getInventory(), new ItemStack(material, amount)));
    }

    private boolean canFit(PlayerInventory inv, ItemStack item) {
        int space = 0;
        for (ItemStack content : inv.getStorageContents()) {
            if(content == null) space += item.getMaxStackSize();
            else if(item.isSimilar(content)) space += item.getMaxStackSize() - content.getAmount();
        }
        return space >= item.getAmount();
    }

    @Override
    public Map<String, Object> getDefaults() {
        return new LinkedHashMap<>() {{
            put("debug", false);
        }};
    }
}
