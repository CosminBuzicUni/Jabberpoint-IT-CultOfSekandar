package jabberpoint.observer;

import jabberpoint.model.Presentation;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(Presentation presentation);
}
