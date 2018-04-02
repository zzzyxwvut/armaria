package org.example.zzzyxwvut.armaria.paging;

/**
 * This class manages common page composition tasks.
 */
public class PageUtil
{
	private static int begin	= -1;
	private static int end		= -1;

	/* Suppress instantiability. */
	private PageUtil()
	{
		throw new AssertionError();
	}

	/**
	 * Calculates the sub-range of values.
	 *
	 * @param firstPos	the minimal value of a range
	 * @param lastPos	the maximal value of a range
	 * @param currentPos	the current value within range
	 * @param itemsPerView	the number of values to make a slice
	 * @throws	IllegalArgumentException if any argument is not
	 *			a natural number or is misplaced
	 */
	public static void makeSlice(final int firstPos, final int lastPos,
				final int currentPos, final int itemsPerView)
	{
		if (firstPos < 1 || lastPos < 1 || currentPos < 1 || itemsPerView < 1
				|| currentPos < firstPos || currentPos > lastPos
				|| firstPos > lastPos)
			throw new IllegalArgumentException();

		final int halfItemsPerView	= itemsPerView >> 1;

		if (halfItemsPerView + 1 > currentPos) {
			PageUtil.begin	= firstPos;
			PageUtil.end	= itemsPerView;
		} else if (lastPos - (currentPos - halfItemsPerView) < itemsPerView) {
			PageUtil.begin	= lastPos - itemsPerView + 1;
			PageUtil.end	= lastPos;
		} else {
			PageUtil.begin	= currentPos - halfItemsPerView + 1;
			PageUtil.end	= currentPos + halfItemsPerView;
		}
	}

	/**
	 * Gets the minimal value of a sub-range.
	 *
	 * @return	the minimal value of a sub-range
	 */
	public static int getBegin()	{ return PageUtil.begin; }

	/**
	 * Gets the maximal value of a sub-range.
	 *
	 * @return	the maximal value of a sub-range
	 */
	public static int getEnd()	{ return PageUtil.end; }
}
