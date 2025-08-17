package model.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class holds the timestamps for events. When the timesheet reaches past 60
 * seconds, no more events can be written to it.
 * <br>
 * The data is represented in three columns:
 * <ul>
 *  <li>timestamp (integer, milliseconds)
 *  <li>damage (integer)
 *  <li>event description (string)
 * </ul>
 *
 * @author Roman Bureacov
 * @version 2025 August
 */
public class TimeSheet implements Iterable<Object[]> {
    private static final int SIXTY_SECONDS = 60000;
    private int eventPlayhead = 0;
    private final List<Object[]> timesheet = new ArrayList<>();

    /**
     * Writes an event into the time sheet from the playhead and then
     * advances the playhead.
     * @param timeDelta the time delta to advance the playhead in milliseconds
     * @param damage the damage caused during the event
     * @param tags the associated tags with the event
     * @throws NoMoreTimeException when the playhead surpasses 60 seconds
     */
    public void writeEvent(final int timeDelta, final int damage, final  String tags)
            throws NoMoreTimeException {
        timesheet.add(new Object[]{eventPlayhead, damage, tags});
        advancePlayhead(timeDelta);
    }

    /**
     * Advances the playhead to the next-most event, or to the end.
     * @param timeDelta the amount of time in milliseconds to advance.
     * @throws NoMoreTimeException if the playhead has surpassed 60 seconds
     */
    public void advancePlayhead(int timeDelta) throws NoMoreTimeException {
        eventPlayhead += timeDelta;
        if (eventPlayhead > SIXTY_SECONDS) throw new NoMoreTimeException();
    }

    @Override
    public Iterator<Object[]> iterator() {
        return timesheet.iterator();
    }

    /**
     * An exception representing the state when a timesheet is full and cannot write more events.
     * <br>
     * A timesheet is considered full when the event playhead has surpassed 60 seconds.
     */
    public static final class NoMoreTimeException extends Exception {
        /**
         * Constructs the basic exception.
         */
        public NoMoreTimeException() {
            super("Timesheet has surpassed 60 seconds");
        }
    }
}
