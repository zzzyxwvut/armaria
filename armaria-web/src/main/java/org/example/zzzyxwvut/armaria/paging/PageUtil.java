package org.example.zzzyxwvut.armaria.paging;

import java.util.HashMap;
import java.util.Map;

import org.example.zzzyxwvut.armaria.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class manages common page composition tasks.
 */
public final class PageUtil
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
	 * @param firstPos	the minimal range value
	 * @param lastPos	the maximal range value
	 * @param currentPos	the current range value
	 * @param linksPerView	the number of values to make a slice,
	 *				if greater than the lastPos value,
	 *				then set to the lastPos value
	 * @throws IllegalArgumentException if any argument is not
	 *			a natural number or is misplaced
	 */
	private static void makeSlice(final int firstPos, final int lastPos,
				final int currentPos, final int linksPerView)
	{
		if (firstPos < 1 || lastPos < 1 || currentPos < 1 || linksPerView < 1
				|| currentPos < firstPos || currentPos > lastPos
				|| firstPos > lastPos)
			throw new IllegalArgumentException(new StringBuilder(32)
				.append("Out of range value [").append(firstPos)
				.append("-").append(lastPos).append("]: ")
				.append(currentPos).toString());

		final int availableLinks	= (lastPos < linksPerView)
							? lastPos
							: linksPerView;
		final int halfItemsPerView	= availableLinks >> 1;

		if (halfItemsPerView + 1 > currentPos) {
			PageUtil.begin	= firstPos;
			PageUtil.end	= availableLinks;
		} else if (lastPos - (currentPos - halfItemsPerView) < availableLinks) {
			PageUtil.begin	= lastPos - availableLinks + 1;
			PageUtil.end	= lastPos;
		} else {
			PageUtil.begin	= currentPos - halfItemsPerView + 1;
			PageUtil.end	= currentPos + halfItemsPerView;
		}
	}

	/**
	 * Creates a new ModelAndView object aware of pagination and sorting.
	 * <br><br>
	 *
	 * The following keys and values are put in the model:<pre>
	 *	pages.beginIndex	the start number link
	 *	pages.endIndex		the end number link
	 *	pages.currentIndex	the current number link
	 *	pages.totalPages	the total page count
	 *
	 *	&lt;viewName&gt;		the assembled page
	 *</pre>
	 *
	 * @param <S>		the class of a service capable of
	 *				pagination and sorting
	 * @param <T>		the class of a rendering page
	 * @param service	the instance of a service capable
	 *				of pagination and sorting
	 * @param viewName	the name of a view to reference
	 * @param model		the model to merge in the new one, or
	 *				{@code null}
	 * @param currentPage	the number of the current page
	 * @param itemsPerView	the pagination item count to render
	 * @param linksPerView	the navigation link count to render
	 * @param direction	the sorting direction, either {@code ASC} or {@code DESC}
	 * @param order		the properties to sort by, arranged in order of
	 *				decreasing significance
	 * @return	a new ModelAndView object aware of pagination and sorting
	 * @throws IllegalArgumentException if any integer argument is not
	 *			a natural number or is misplaced
	 */
	public static <T, S extends GenericService> ModelAndView renderPaginated(
				S service, String viewName, Model model,
				int currentPage, int itemsPerView, int linksPerView,
				Sort.Direction direction, String[] order)
	{
		/* Account for zero-based indexing. */
		final Page<T> items	= service.getItemsOnPage(new PageRequest(
			currentPage - 1, itemsPerView, direction, order));
		final int currentLink	= items.getNumber() + 1;
		PageUtil.makeSlice(1, items.getTotalPages(), currentLink, linksPerView);
		Map<String, Integer> pages	= new HashMap<>();
		pages.put("beginIndex",	PageUtil.begin);
		pages.put("endIndex",	PageUtil.end);
		pages.put("currentIndex", currentLink);
		pages.put("totalPages",	items.getTotalPages());
		Map<String, Object> all		= new HashMap<>();
		all.put("pages",	pages);
		all.put(viewName,	items);
		return new ModelAndView(viewName, (model != null)
				? model.mergeAttributes(all).asMap()
				: all);
	}
}
