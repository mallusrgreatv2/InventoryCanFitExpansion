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
        boolean spaceLeft = params.startsWith("spaceleft_");
        String prefix = "Got placeholder: " + params + " | ";
        if (player == null) {
            if (debug) info(prefix + "Player is null!");
            return null;
        }
        String[] split = params.split("_");
        String itemName = String.join("_", Arrays.copyOfRange(split, spaceLeft ? 1 : 0, split.length - (spaceLeft ? 0 : 1)));
        Integer amount = null;
        try {
            amount = Integer.parseInt(split[split.length - 1]);
        } catch (NumberFormatException ignored) {}
        if (!spaceLeft) {
            if (amount == null) {
                if (debug) info(prefix + "Amount is null!");
                return null;
            }
            if (amount <= 0) {
                if (debug) info(prefix + "Amount is less than or equal to zero!");
                return null;
            }
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
        ItemStack stack = new ItemStack(material, amount == null ? 1 : amount);
        PlayerInventory inventory = player.getInventory();
        return spaceLeft
                ? String.valueOf(getSpaceLeft(inventory, stack))
                : String.valueOf(getSpaceLeft(inventory, stack) >= amount);
    }

    private int getSpaceLeft(PlayerInventory inventory, ItemStack item) {
        int space = 0;
        for (ItemStack content : inventory.getStorageContents()) {
            if(content == null) space += item.getMaxStackSize();
            else if(item.isSimilar(content)) space += item.getMaxStackSize() - content.getAmount();
        }
        return space;
    }

    @Override
    public Map<String, Object> getDefaults() {
        return new LinkedHashMap<>() {{
            put("debug", false);
        }};
    }
}
