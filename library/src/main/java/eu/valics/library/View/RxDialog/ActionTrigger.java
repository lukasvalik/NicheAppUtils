package eu.valics.library.View.RxDialog;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by L on 8/24/2017.
 */

public class ActionTrigger<T> {
    private final BehaviorSubject<Object> triggerConfirmationDialog = BehaviorSubject.create();

    private ActionTrigger() {}

    public static <T> ActionTrigger<T> create() {
        return new ActionTrigger<>();
    }

    @SuppressWarnings("unchecked") public Observable<T> observe() {
        return triggerConfirmationDialog
                .filter(ifNotAnEmptyValue())
                .map(aObject -> ((T) aObject))
                .doOnNext(aObject -> triggerConfirmationDialog.onNext(Empty.value));
    }

    @NonNull
    private Predicate<Object> ifNotAnEmptyValue() {
        return aObject -> !(aObject instanceof ActionTrigger.Empty);
    }

    public void trigger(final T value) {
        triggerConfirmationDialog.onNext(value);
    }

    private enum Empty {
        value,
    }
}
