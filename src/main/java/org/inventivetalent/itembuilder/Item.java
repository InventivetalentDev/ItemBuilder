/*
 * Copyright (c) inventivetalent.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.itembuilder;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.*;

@Data
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true,
				   callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Item extends ItemAbstract {

	protected static final Map<Class<? extends ItemMeta>, Class<? extends Item>> specialMetaMap      = new HashMap<>();
	protected static final Map<String, Class<? extends Item>>                    specialCraftMetaMap = new HashMap<>();

	static {
		specialMetaMap.put(SkullMeta.class, SkullItem.class);
		specialMetaMap.put(BannerMeta.class, BannerItem.class);
		specialMetaMap.put(BookMeta.class, BookItem.class);
		specialMetaMap.put(FireworkMeta.class, FireworkItem.class);
		specialMetaMap.put(FireworkEffectMeta.class, FireworkEffectItem.class);
		specialMetaMap.put(LeatherArmorMeta.class, LeatherArmorItem.class);
		specialMetaMap.put(MapMeta.class, MapItem.class);
		specialMetaMap.put(PotionMeta.class, PotionItem.class);

		specialCraftMetaMap.put("CraftMetaSkull", SkullItem.class);
		specialCraftMetaMap.put("CraftMetaBanner", BannerItem.class);
		specialCraftMetaMap.put("CraftMetaBook", BookItem.class);
		specialCraftMetaMap.put("CraftMetaFirework", FireworkItem.class);
		specialCraftMetaMap.put("CraftMetaCharge", FireworkEffectItem.class);
		specialCraftMetaMap.put("CraftMetaLeatherArmor", LeatherArmorItem.class);
		specialCraftMetaMap.put("CraftMetaMap", MapItem.class);
		specialCraftMetaMap.put("CraftMetaPotion", PotionItem.class);
	}

	private Material type       = Material.AIR;
	private int      amount     = 1;
	private short    durability = 0;

	// Meta
	private           String                    displayName = null;
	@Singular private List<String>              loreLines   = new ArrayList<>();
	@Singular private Map<Enchantment, Integer> enchants    = new HashMap<>();
	@Singular private List<ItemFlag>            flags       = new ArrayList<>();
	private           boolean                   unbreakable = false;

	public Item(Material type) {
		this.type = type;
	}

	public Item(Material type, int amount) {
		this.type = type;
		this.amount = amount;
	}

	public Item(Material type, int amount, short durability) {
		this.type = type;
		this.amount = amount;
		this.durability = durability;
	}

	public Item(Material type, int amount, short durability, String displayName) {
		this.type = type;
		this.amount = amount;
		this.durability = durability;
		this.displayName = displayName;
	}

	public Item(Item other) {
		this(other.type, other.amount, other.durability, other.displayName, other.loreLines, other.enchants, other.flags, other.unbreakable);
	}

	public ItemStack asStack() {
		ItemStack itemStack = new ItemStack(this.type, this.amount, this.durability);
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			if (this.displayName != null) { meta.setDisplayName(this.displayName); }
			meta.setLore(this.loreLines);
			for (Map.Entry<Enchantment, Integer> entry : this.enchants.entrySet()) {
				meta.addEnchant(entry.getKey(), entry.getValue(), true);// Just always ignore restrictions
			}
			for (ItemFlag flag : this.flags) {
				meta.addItemFlags(flag);
			}
			try {
				meta.spigot().setUnbreakable(this.unbreakable);
			} catch (Exception ignored) {
				// Probably not spigot
			}
			itemStack.setItemMeta(meta);
		}
		return itemStack;
	}

	public Builder modify() {
		if (this.type == null) { throw new IllegalStateException("type must be specified"); }
		try {
			ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
			if (specialCraftMetaMap.containsKey(meta.getClass().getSimpleName())) {
				return (Builder) specialCraftMetaMap.get(meta.getClass().getSimpleName()).getDeclaredClasses()[0].getDeclaredConstructors()[1].newInstance(this);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new Builder(this);
	}

	public static Item of(ItemStack itemStack) {
		return of(itemStack, true);
	}

	protected static Item of(ItemStack itemStack, boolean convert) {
		if (itemStack == null) { return null; }
		if (itemStack.getItemMeta() != null && specialCraftMetaMap.containsKey(itemStack.getItemMeta().getClass().getSimpleName())) {
			try {
				if (convert) {
					return (Item) specialCraftMetaMap.get(itemStack.getItemMeta().getClass().getSimpleName()).getDeclaredMethod("of", ItemStack.class).invoke(null, itemStack);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Builder builder = Item.builder(itemStack.getType());
		//		builder.type(itemStack.getType());
		builder.amount(itemStack.getAmount());
		builder.durability(itemStack.getDurability());
		if (itemStack.getItemMeta() != null) {
			builder.displayName(itemStack.getItemMeta().getDisplayName());
			builder.lore(itemStack.getItemMeta().getLore());
			builder.enchants(itemStack.getItemMeta().getEnchants());
			try {
				builder.unbreakable(itemStack.getItemMeta().spigot().isUnbreakable());
			} catch (Exception ignored) {
				// Probably not spigot
			}
		}
		return builder.build();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(Material type) {
		Builder builder = builder();
		try {
			ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
			if (specialCraftMetaMap.containsKey(meta.getClass().getSimpleName())) {
				builder = (Builder) specialCraftMetaMap.get(meta.getClass().getSimpleName()).getDeclaredMethod("builder").invoke(null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return builder.type(type);
	}

	public static class Builder<I extends Item, B extends Builder> extends BuilderAbstract<I, B> {

		protected Builder() {
		}

		protected Builder(I item) {
			super(item);
		}

		@Override
		protected I init() {
			return (I) new Item();
		}

		public I build() {
			return this.item;
		}

		public ItemStack buildStack() {
			return build().asStack();
		}

		public B type(Material type) {
			item.setType(type);
			return (B) this;
		}

		public B amount(int amount) {
			item.setAmount(amount);
			return (B) this;
		}

		public B durability(short durability) {
			item.setDurability(durability);
			return (B) this;
		}

		public B displayName(String displayName) {
			item.setDisplayName(displayName);
			return (B) this;
		}

		public B lore(Collection<String> loreLines) {
			item.getLoreLines().addAll(loreLines);
			return (B) this;
		}

		public B lore(String... lines) {
			item.getLoreLines().addAll(Arrays.asList(lines));
			return (B) this;
		}

		public B lore(String line) {
			item.getLoreLines().add(line);
			return (B) this;
		}

		public B enchants(Map<Enchantment, Integer> enchants) {
			item.getEnchants().putAll(enchants);
			return (B) this;
		}

		public B enchant(Enchantment enchantment, int level) {
			item.getEnchants().put(enchantment, level);
			return (B) this;
		}

		public B flags(ItemFlag... flags) {
			item.getFlags().addAll(Arrays.asList(flags));
			return (B) this;
		}

		public B flag(ItemFlag flag) {
			item.getFlags().add(flag);
			return (B) this;
		}

		public B unbreakable(boolean unbreakable) {
			item.setUnbreakable(unbreakable);
			return (B) this;
		}

	}

}
