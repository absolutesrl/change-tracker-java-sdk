package it.changetracker.sdk.core;

import it.changetracker.sdk.models.Row;

/**
 * ChangeTrackerCalculator
 **/
public interface ChangeTrackerCalculator {
    Row diff(Row prev, Row next);
}
