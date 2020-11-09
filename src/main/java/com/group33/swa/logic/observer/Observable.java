package com.group33.swa.logic.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Observable Class is used to store Observer and notifies all observers on new changes.
 *
 * @author Alexander Garber
 */
public class Observable<T> {

  private final List<Observer<T>> observers = new ArrayList<Observer<T>>();

  /**
   * Adds a Observer to the Observer-List
   *
   * @param observer {@link Observer}
   */
  public void addObserver(Observer<T> observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  /**
   * Removes a Observer from the Observer-List
   *
   * @param observer {@link Observer}
   */
  public void removeObserver(Observer<T> observer) {
    if (observers.contains(observer)) observers.remove(observer);
  }

  /**
   * Method where all observers of the Observer-list where notified on a new Object
   *
   * @param arg
   */
  public void notifyObservers(T arg) {
    for (Observer<T> observer : observers) {
      observer.update(this, arg);
    }
  }
}
