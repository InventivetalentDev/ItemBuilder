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
import org.bukkit.inventory.meta.MapMeta;

@Data
@ToString(doNotUseGetters = true,
		  callSuper = true)
@EqualsAndHashCode(doNotUseGetters = true,
				   callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MapItem extends Item {

	private boolean scaling;

	@Override
	public ItemStack asStack() {
		ItemStack stack = super.asStack();
		if (stack.getItemMeta() != null) {
			MapMeta meta = (MapMeta) stack.getItemMeta();
			meta.setScaling(this.scaling);
			stack.setItemMeta(meta);
		}
		return stack;
	}

	public static MapItem of(ItemStack itemStack) {
		MapItem item = (MapItem) Item.of(itemStack, false);
		if (itemStack.getItemMeta() != null) {
			item.scaling = ((MapMeta) itemStack.getItemMeta()).isScaling();
		}
		return item;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends Item.Builder<MapItem, Builder> {

		protected Builder() {
		}

		protected Builder(MapItem item) {
			super(item);
		}

		@Override
		protected MapItem init() {
			return new MapItem();
		}

		@Override
		public MapItem build() {
			return super.build();
		}

		public Builder scaling(boolean scaling) {
			item.scaling = scaling;
			return this;
		}

	}

}
