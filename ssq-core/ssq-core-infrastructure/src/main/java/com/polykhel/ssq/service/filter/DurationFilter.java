
package com.polykhel.ssq.service.filter;

import java.time.Duration;

/**
 * Filter class for {@link Duration} type attributes.
 *
 * @see Filter
 */
public class DurationFilter extends RangeFilter<Duration> {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for DurationFilter.</p>
     */
    public DurationFilter() {
    }

    /**
     * <p>Constructor for DurationFilter.</p>
     *
     * @param filter a {@link com.polykhel.ssq.service.filter.DurationFilter} object.
     */
    public DurationFilter(final DurationFilter filter) {
        super(filter);
    }

    /**
     * <p>copy.</p>
     *
     * @return a {@link com.polykhel.ssq.service.filter.DurationFilter} object.
     */
    public DurationFilter copy() {
        return new DurationFilter(this);
    }

}
