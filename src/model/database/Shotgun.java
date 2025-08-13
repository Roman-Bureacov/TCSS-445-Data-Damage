package model.database;

/**
 * CLass that models a shotgun.
 * @author Roman Bureacov
 * @version August 2025
 */
public class Shotgun extends ShellLoading {
    private static final int INITIAL_SHELL = 680;
    private static final int SUBSEQUENT_SHELL = 430;

    Shotgun(final WeaponSkeleton skeleton) {
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
