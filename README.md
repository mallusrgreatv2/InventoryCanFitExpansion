# InventoryCanFitExpansion

Returns whether the player's inventory can fit a certain number of an item.

## Usage

- `%inventorycanfit_<material_name>_<amount>%`
  - Returns `true` or `false`
- `%inventorycanfit_spaceleft_<material_name>%`
  - Returns the number of space the item has left to occupy in the inventory 

## Examples

- `%inventorycanfit_diamond_sword_2%`
- `%inventorycanfit_stone_50`
- `%inventorycanfit_spaceleft_stone`

## Notes

- Item names are from the `minecraft:item` registry: https://github.com/misode/mcmeta/blob/registries/item/data.json
