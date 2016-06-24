package com.github.lwhite1.tablesaw.table;

import com.github.lwhite1.tablesaw.api.Table;
import com.github.lwhite1.tablesaw.columns.BooleanColumn;
import com.github.lwhite1.tablesaw.columns.CategoryColumn;
import com.github.lwhite1.tablesaw.columns.Column;
import com.github.lwhite1.tablesaw.columns.FloatColumn;
import com.github.lwhite1.tablesaw.columns.IntColumn;
import com.github.lwhite1.tablesaw.columns.DateColumn;
import com.github.lwhite1.tablesaw.columns.DateTimeColumn;
import com.github.lwhite1.tablesaw.columns.TimeColumn;
import com.github.lwhite1.tablesaw.columns.LongColumn;
import com.github.lwhite1.tablesaw.api.ColumnType;
import com.github.lwhite1.tablesaw.columns.ShortColumn;
import com.github.lwhite1.tablesaw.util.ReversingIntComparator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import org.roaringbitmap.RoaringBitmap;

import javax.annotation.concurrent.Immutable;

/**
 * A static utility class for row operations
 */
@Immutable
public class Rows {

  // Don't instantiate
  private Rows() {
  }

  public static void copyRowsToTable(IntArrayList rows, Table oldTable, Table newTable) {

    for (int columnIndex = 0; columnIndex < oldTable.columnCount(); columnIndex++) {
      ColumnType columnType = oldTable.column(columnIndex).type();
      switch (columnType) {
        case FLOAT:
          copy(rows, (FloatColumn) oldTable.column(columnIndex), (FloatColumn) newTable.column(columnIndex));
          break;
        case INTEGER:
          copy(rows, (IntColumn) oldTable.column(columnIndex), (IntColumn) newTable.column(columnIndex));
          break;
        case SHORT_INT:
          copy(rows, (ShortColumn) oldTable.column(columnIndex), (ShortColumn) newTable.column(columnIndex));
          break;
        case LONG_INT:
          copy(rows, (LongColumn) oldTable.column(columnIndex), (LongColumn) newTable.column(columnIndex));
          break;
        case CATEGORY:
          copy(rows, (CategoryColumn) oldTable.column(columnIndex), (CategoryColumn) newTable.column(columnIndex));
          break;
        case BOOLEAN:
          copy(rows, (BooleanColumn) oldTable.column(columnIndex), (BooleanColumn) newTable.column(columnIndex));
          break;
        case LOCAL_DATE:
          copy(rows, (DateColumn) oldTable.column(columnIndex), (DateColumn) newTable.column(columnIndex));
          break;
        case LOCAL_DATE_TIME:
          copy(rows, (DateTimeColumn) oldTable.column(columnIndex), (DateTimeColumn) newTable.column
              (columnIndex));
          break;
        case LOCAL_TIME:
          copy(rows, (TimeColumn) oldTable.column(columnIndex), (TimeColumn) newTable.column(columnIndex));
          break;
        default:
          throw new RuntimeException("Unhandled column type in case statement");
      }
    }
  }

  public static void copyRowsToTable(RoaringBitmap rows, Table oldTable, Table newTable) {
    int[] r = rows.toArray();
    IntArrayList rowArray = new IntArrayList(r);
    copyRowsToTable(rowArray, oldTable, newTable);
  }

  public static void head(int rowCount, Table oldTable, Table newTable) {
    IntArrayList rows = new IntArrayList(rowCount);
    for (int i = 0; i < rowCount; i++) {
      rows.add(i);
    }
    copyRowsToTable(rows, oldTable, newTable);
  }

  public static void tail(int rowCount, Table oldTable, Table newTable) {
    int oldTableSize = oldTable.rowCount();
    int end = oldTableSize - 1;
    int start = end - rowCount;
    IntArrayList rows = new IntArrayList(rowCount);
    for (int i = start; i < end; i++) {
      rows.add(i);
    }
    copyRowsToTable(rows, oldTable, newTable);
  }

  private static void copy(IntArrayList rows, FloatColumn oldColumn, FloatColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.get(index));
    }
  }

  private static void copy(IntArrayList rows, CategoryColumn oldColumn, CategoryColumn newColumn) {
    newColumn.initializeWith(oldColumn.getValues(rows), oldColumn.dictionaryMap());
  }

  private static void copy(IntArrayList rows, BooleanColumn oldColumn, BooleanColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.get(index));
    }
  }

  private static void copy(IntArrayList rows, IntColumn oldColumn, IntColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.get(index));
    }
  }

  private static void copy(IntArrayList rows, ShortColumn oldColumn, ShortColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.get(index));
    }
  }

  private static void copy(IntArrayList rows, LongColumn oldColumn, LongColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.get(index));
    }
  }

  private static void copy(IntArrayList rows, DateTimeColumn oldColumn, DateTimeColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.getLong(index));
    }
  }

  private static void copy(IntArrayList rows, DateColumn oldColumn, DateColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.getInt(index));
    }
  }

  private static void copy(IntArrayList rows, TimeColumn oldColumn, TimeColumn newColumn) {
    for (int index : rows) {
      newColumn.add(oldColumn.getInt(index));
    }
  }

  /**
   * Returns a comparator for the column matching the specified name
   */
  private IntComparator rowComparator(Column column, boolean reverse) {

    IntComparator rowComparator = column.rowComparator();

    if (reverse) {
      return ReversingIntComparator.reverse(rowComparator);
    } else {
      return rowComparator;
    }
  }
}