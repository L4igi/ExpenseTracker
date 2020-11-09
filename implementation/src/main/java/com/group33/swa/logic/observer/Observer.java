package com.group33.swa.logic.observer;

public interface Observer<T> {

  /**
   * Update-Method which will be used by Observer
   *
   * @param o {@link Observable}
   * @param arg
   */
  void update(Observable<T> o, T arg);
}
