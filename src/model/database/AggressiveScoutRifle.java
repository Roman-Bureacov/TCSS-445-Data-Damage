package model.database;

/**
 * Class that models an aggressive scout rifle that can load one shell at a time.
 * @author Roman Bureacov
 * @version August 2025
 */
public class AggressiveScoutRifle extends ShellLoading {
    private static final int INITIAL_SHELL = 580;
    private static final int SUBSEQUENT_SHELL = 380;

    AggressiveScoutRifle(final WeaponSkeleton skeleton) {
        super(skeleton);
    }

    @Override
    int getInitialShellTime() {
        return INITIAL_SHELL;
    }

    @Override
    int getSubsequentShellTime() {
        return SUBSEQUENT_SHELL;
    }
}
