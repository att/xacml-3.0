/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * ListUtil contains a number of useful utilities on java <code>Collection</code> objects.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ListUtil {

	protected ListUtil() {
	}
	
	/**
	 * If the given <code>Iterator</code> is not null, iterate over all <code>T</code> elements
	 * in it and add them to the given <code>Collection</code>.
	 * 
     * @param <T> Object
	 * @param iteratorFrom the <code>Iterator</code> to copy from
	 * @param collectionTo the <code>Collection</code> to copy to
	 */
	public static <T> void addAll(Iterator<T> iteratorFrom, Collection<T> collectionTo) {
		if (iteratorFrom != null) {
			while (iteratorFrom.hasNext()) {
				collectionTo.add(iteratorFrom.next());
			}
		}
	}
	
	public static <T> boolean equalsAllowNulls(Iterator<T> iterator1, Iterator<T> iterator2) {
		if (iterator1 == null || !iterator1.hasNext()) {
			return (iterator2 == null || !iterator2.hasNext());
		} else if (iterator2 == null || !iterator2.hasNext()) {
			return false;
		} else {
			boolean result	= true;
			while (result && iterator1.hasNext() && iterator2.hasNext()) {
				result	= ObjUtil.equalsAllowNull(iterator1.next(), iterator2.next());
			}
			return result && !iterator1.hasNext() && !iterator2.hasNext();
		}
	}

	public static <T> boolean equalsAllowNulls(Collection<T> collection1, Collection<T> collection2) {
		if (collection1 == collection2) {
			return true;
		} else if (collection1 == null || collection2 == null) {
			return false;
		} else if (collection1.size() != collection2.size()) {
			return false;
		} else {
			for (Iterator<T> i1 = collection1.iterator(), i2 = collection2.iterator() ; i1.hasNext() ; ) {
				if (!ObjUtil.equalsAllowNull(i1.next(), i2.next())) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static <T> String toString(Collection<T> collection) {
		StringBuilder stringBuilder	= new StringBuilder("[");
		boolean first				= true;
		for (T t : collection) {
			if (!first) {
				stringBuilder.append(',');
			} else {
				first	= true;
			}
			if (t == null) {
				stringBuilder.append("null");
			} else {
				stringBuilder.append(t.toString());
			}
		}
		stringBuilder.append(']');
		return stringBuilder.toString();
	}

}
