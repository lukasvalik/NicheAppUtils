package eu.valics.library.AppPausingProcesses;

import java.util.ArrayList;

/**
 * Created by L on 7/19/2017.
 */

public class AppPausingProcessesManager {

    private AppPausingProcesses mAppPausingProcesses;

    public AppPausingProcessesManager(ArrayList<AppPausingProcess> appPausingProcesses) {
        mAppPausingProcesses = new AppPausingProcesses(appPausingProcesses) ;
    }

    public boolean areAppPausesProcessesInProgress() {
        mAppPausingProcesses.invalidate();
        return mAppPausingProcesses.areHandled();
    }

    static AppPausingProcess getDefaltProcess() {
        return new AppPausingProcess() {
            @Override
            public void start() {
                finish();
            }

            @Override
            public boolean finish() {
                return false;
            }

            @Override
            public boolean isDefault() {
                return true;
            }
        };
    }
}
