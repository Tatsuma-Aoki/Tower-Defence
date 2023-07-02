package uectd.gameSystem.util;

import java.util.Observable;

public class ObservableComponent<T> extends Observable {
    private T value;

    public ObservableComponent(T value) {
        setValue(value);
    }

    public ObservableComponent() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        setChanged();
        notifyObservers();
    }

    public void update() {
        setChanged();
        notifyObservers();
    }
}
