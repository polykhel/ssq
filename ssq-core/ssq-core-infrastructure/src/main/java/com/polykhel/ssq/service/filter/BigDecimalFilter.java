package com.polykhel.ssq.service.filter;

import java.math.BigDecimal;

/**
 * Filter class for {@link BigDecimal} type attributes.
 *
 * @see RangeFilter
 */
public class BigDecimalFilter extends RangeFilter<BigDecimal> {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for BigDecimalFilter.</p>
     */
    public BigDecimalFilter() {
    }

    /**
     * <p>Constructor for BigDecimalFilter.</p>
     *
     * @param filter a {@link com.polykhel.ssq.service.filter.BigDecimalFilter} object.
     */
    public BigDecimalFilter(final BigDecimalFilter filter) {
        super(filter);
    }

    /**
     * <p>copy.</p>
     *
     * @return a {@link com.polykhel.ssq.service.filter.BigDecimalFilter} object.
     */
    public BigDecimalFilter copy() {
        return new BigDecimalFilter(this);
    }

}
