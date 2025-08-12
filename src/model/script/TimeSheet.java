package model.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class holds the timestamps for events.
 * @author Roman Bureacov
 * @version 2025 August
 */
public class TimeSheet implements Iterable<Object[]> {
    private int eventPlayhead = 0;
    private final List<Object[]> timesheet = new ArrayList<>();

    /**
     * Writes an event into the time sheet from the playhead.
     * @param timeDelta the time delta from the current event playhead
     * @param damage the damage caused during the event
     * @param tags the associated tags with the event
     */
    protected void writeEvent(final int timeDelta, final int damage, final String tags) {
        timesheet.add(new Object[]{eventPlayhead + timeDelta, damage, tags});
    }

    /**
     * Advances the playhead to the next-most event, or to the end.
     * @param timeDelta the amount of time in milliseconds to advance.
     */
    protected void advancePlayhead(int timeDelta) {
        eventPlayhead += timeDelta;
    }

    @Override
    public Iterator<Object[]> iterator() {
        return timesheet.iterator();
    }
}
