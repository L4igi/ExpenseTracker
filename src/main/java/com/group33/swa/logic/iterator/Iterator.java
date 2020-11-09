package com.group33.swa.logic.iterator;

/** Interface for the Iterator */
public interface Iterator {

  /**
   * Checks if a next object exist
   *
   * @return {@link boolean}
   */
  public boolean hasNext();

  /**
   * Method to get next Value
   *
   * @return
   */
  public Object next();
}
