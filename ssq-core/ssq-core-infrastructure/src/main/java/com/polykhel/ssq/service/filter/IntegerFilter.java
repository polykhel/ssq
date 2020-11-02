

package com.polykhel.ssq.service.filter;

/**
 * Filter class for {@link Integer} type attributes.
 *
 * @see RangeFilter
 */
public class IntegerFilter extends RangeFilter<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for IntegerFilter.</p>
     */
    public IntegerFilter() {
    }

    /**
     * <p>Constructor for IntegerFilter.</p>
     *
     * @param filter a {@link com.polykhel.ssq.service.filter.IntegerFilter} object.
     */
    public IntegerFilter(final IntegerFilter filter) {
        super(filter);
    }

    /**
     * <p>copy.</p>
     *
     * @return a {@link com.polykhel.ssq.service.filter.IntegerFilter} object.
     */
    public IntegerFilter copy() {
        return new IntegerFilter(this);
    }

}
