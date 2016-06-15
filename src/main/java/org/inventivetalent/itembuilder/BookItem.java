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
import org.bukkit.inventory.meta.BookMeta;

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
public class BookItem extends Item {

	private String title;
	private String author;
	private List<String> pages = new ArrayList<>();

	@Override
	public ItemStack asStack() {
		ItemStack stack = super.asStack();
		if (stack.getItemMeta() != null) {
			BookMeta meta = (BookMeta) stack.getItemMeta();
			meta.setTitle(this.title);
			meta.setAuthor(this.author);
			meta.setPages(this.pages);
			stack.setItemMeta(meta);
		}
		return stack;
	}

	public static BookItem of(ItemStack itemStack) {
		BookItem item = (BookItem) Item.of(itemStack, false);
		if (itemStack.getItemMeta() != null) {
			item.title = ((BookMeta) itemStack.getItemMeta()).getTitle();
			item.author = ((BookMeta) itemStack.getItemMeta()).getAuthor();
			item.pages.addAll(((BookMeta) itemStack.getItemMeta()).getPages());
		}
		return item;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends Item.Builder<BookItem, Builder> {
		protected Builder() {
		}

		protected Builder(BookItem item) {
			super(item);
		}

		@Override
		protected BookItem init() {
			return new BookItem();
		}

		@Override
		public BookItem build() {
			return super.build();
		}

		public Builder title(String title) {
			item.title = title;
			return this;
		}

		public Builder author(String author) {
			item.author = author;
			return this;
		}

		public Builder pages(Collection<String> pages) {
			item.pages.addAll(pages);
			return this;
		}

		public Builder pages(String... pages) {
			item.pages.addAll(Arrays.asList(pages));
			return this;
		}

		public Builder page(String page) {
			item.pages.add(page);
			return this;
		}

	}

}
