package com.absolute.changetracker.sdk.core;

import com.absolute.changetracker.sdk.models.Row;

/**
 * ChangeTrackerCalculator
 **/
public interface ChangeTrackerCalculator {
    Row diff(Row prev, Row next);
}
