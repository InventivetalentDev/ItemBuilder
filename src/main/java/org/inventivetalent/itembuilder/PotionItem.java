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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@ToString(doNotUseGetters = true,
		  callSuper = true)
@EqualsAndHashCode(doNotUseGetters = true,
				   callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PotionItem extends Item {

	private PotionData basePotion;
	private List<PotionEffect> customEffects = new ArrayList<>();

	@Override
	public ItemStack asStack() {
		ItemStack stack = super.asStack();
		if (stack.getItemMeta() != null) {
			PotionMeta meta = (PotionMeta) stack.getItemMeta();
			meta.setBasePotionData(this.basePotion);
			for (PotionEffect effect : this.customEffects) {
				meta.addCustomEffect(effect, true);
			}
			stack.setItemMeta(meta);
		}
		return stack;
	}

	public static PotionItem of(ItemStack itemStack) {
		PotionItem item = (PotionItem) Item.of(itemStack, false);
		if (itemStack.getItemMeta() != null) {
			item.basePotion = ((PotionMeta) itemStack.getItemMeta()).getBasePotionData();
			item.customEffects.addAll(((PotionMeta) itemStack.getItemMeta()).getCustomEffects());
		}
		return item;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends Item.Builder<PotionItem, Builder> {

		protected Builder() {
		}

		protected Builder(PotionItem item) {
			super(item);
		}

		@Override
		protected PotionItem init() {
			return new PotionItem();
		}

		@Override
		public PotionItem build() {
			return super.build();
		}

		public Builder basePotion(PotionData basePotion) {
			item.basePotion = basePotion;
			return this;
		}

		public Builder effects(Collection<PotionEffect> effects) {
			item.customEffects.addAll(effects);
			return this;
		}

		public Builder effects(PotionEffect... effects) {
			item.customEffects.addAll(Arrays.asList(effects));
			return this;
		}

		public Builder effect(PotionEffect effect) {
			item.customEffects.add(effect);
			return this;
		}

	}

}
