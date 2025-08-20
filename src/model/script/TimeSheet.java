package model.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
    private static final double SEC_PER_MS = 1/1000d;
    private int eventPlayhead = 0;
    private final List<Object[]> timesheet = new ArrayList<>();

    /**
     * Returns the appropriate damage-per-second as a function of time timesheet
     * based on this timesheet.
     * @param resolution the number of milliseconds to iterate over
     * @return the DPS based on this timesheet as a list of object,
     * with the first column as the integer timestamps, the second column as the double DPS,
     * and the third column as null
     */
    public TimeSheet getDPSTimesheet(final int resolution) {
        final TimeSheet t = new TimeSheet();
        int damageSum = 0;
        int position = 0;
        final int len = this.timesheet.size();
        for (int ms = 1; ms < SIXTY_SECONDS; ms += resolution) {
            while (position < len) {
                final Object[] datapoint = this.timesheet.get(position);
                if ((Integer)datapoint[0] < ms) {
                    damageSum += (Integer)datapoint[1];
                    position++;
                } else break;
            }
            t.timesheet.add(new Object[] {ms, (double)damageSum / (ms * SEC_PER_MS), null});
        }

        return t;
    }

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
