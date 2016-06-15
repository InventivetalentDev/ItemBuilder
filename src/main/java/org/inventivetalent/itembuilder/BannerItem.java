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
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

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
public class BannerItem extends Item {

	private DyeColor baseColor;
	private List<Pattern> patterns = new ArrayList<>();

	@Override
	public ItemStack asStack() {
		ItemStack stack = super.asStack();
		if (stack.getItemMeta() != null) {
			BannerMeta meta = (BannerMeta) stack.getItemMeta();
			meta.setBaseColor(this.baseColor);
			meta.setPatterns(this.patterns);
			stack.setItemMeta(meta);
		}
		return stack;
	}

	public static BannerItem of(ItemStack itemStack) {
		BannerItem item = (BannerItem) Item.of(itemStack, false);
		if (itemStack.getItemMeta() != null) {
			item.baseColor = ((BannerMeta) itemStack.getItemMeta()).getBaseColor();
			item.patterns.addAll(((BannerMeta) itemStack.getItemMeta()).getPatterns());
		}
		return item;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends Item.Builder<BannerItem, Builder> {
		protected Builder() {
		}

		protected Builder(BannerItem item) {
			super(item);
		}

		@Override
		protected BannerItem init() {
			return new BannerItem();
		}

		@Override
		public BannerItem build() {
			return super.build();
		}

		public Builder baseColor(DyeColor baseColor) {
			item.baseColor = baseColor;
			return this;
		}

		public Builder patterns(Collection<Pattern> patterns) {
			item.patterns.addAll(patterns);
			return this;
		}

		public Builder patterns(Pattern... patterns) {
			item.patterns.addAll(Arrays.asList(patterns));
			return this;
		}

		public Builder pattern(Pattern pattern) {
			item.patterns.add(pattern);
			return this;
		}

	}

}
