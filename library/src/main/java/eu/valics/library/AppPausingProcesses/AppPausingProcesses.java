package eu.valics.library.AppPausingProcesses;

import java.util.ArrayList;

import eu.valics.library.Base.AppPausingProcess;
import io.reactivex.Observable;

/**
 * Created by L on 7/19/2017.
 */

public class AppPausingProcesses extends ArrayList<AppPausingProcess> {

    private boolean mHandled = false;

    public AppPausingProcesses(ArrayList<AppPausingProcess> appPausingProcesses) {
        addAll(appPausingProcesses);
    }

    public void invalidate() {
        AppPausingProcess process = getNextProcess();
        if (!process.isDefault()) {
            process.start();
        } else {
            mHandled = true;
        }
    }

    public AppPausingProcess getNextProcess() {
        return Observable.fromIterable(this).filter(AppPausingProcess::finish).first(AppPausingProcessesManager.getDefaltProcess()).blockingGet();
    }

    public boolean areHandled() {
        return mHandled;
    }
}
