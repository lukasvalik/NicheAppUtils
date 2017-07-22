package eu.valics.library.Base;

/**
 * Created by L on 7/19/2017.
 */

public interface AppPausingProcess {

    void start();

    boolean finish();

    boolean isDefault();

    enum Type {
        DEFAULT, PROCESS
    }
}
