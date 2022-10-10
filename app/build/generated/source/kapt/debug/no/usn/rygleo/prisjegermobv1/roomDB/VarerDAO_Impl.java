package no.usn.rygleo.prisjegermobv1.roomDB;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class VarerDAO_Impl implements VarerDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Varer> __insertionAdapterOfVarer;

  private final EntityDeletionOrUpdateAdapter<Varer> __deletionAdapterOfVarer;

  public VarerDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVarer = new EntityInsertionAdapter<Varer>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Varer` (`varenavn`,`enhetspris`,`antall`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Varer value) {
        if (value.getVarenavn() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getVarenavn());
        }
        if (value.getEnhetspris() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindDouble(2, value.getEnhetspris());
        }
        if (value.getAntall() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, value.getAntall());
        }
      }
    };
    this.__deletionAdapterOfVarer = new EntityDeletionOrUpdateAdapter<Varer>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Varer` WHERE `varenavn` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Varer value) {
        if (value.getVarenavn() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getVarenavn());
        }
      }
    };
  }

  @Override
  public void insertAll(final Varer... varer) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVarer.insert(varer);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Varer varer) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfVarer.handle(varer);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Varer>> getAlleVarer() {
    final String _sql = "SELECT * FROM Varer";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Varer"}, false, new Callable<List<Varer>>() {
      @Override
      public List<Varer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfVarenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "varenavn");
          final int _cursorIndexOfEnhetspris = CursorUtil.getColumnIndexOrThrow(_cursor, "enhetspris");
          final int _cursorIndexOfAntall = CursorUtil.getColumnIndexOrThrow(_cursor, "antall");
          final List<Varer> _result = new ArrayList<Varer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Varer _item;
            final String _tmpVarenavn;
            if (_cursor.isNull(_cursorIndexOfVarenavn)) {
              _tmpVarenavn = null;
            } else {
              _tmpVarenavn = _cursor.getString(_cursorIndexOfVarenavn);
            }
            final Double _tmpEnhetspris;
            if (_cursor.isNull(_cursorIndexOfEnhetspris)) {
              _tmpEnhetspris = null;
            } else {
              _tmpEnhetspris = _cursor.getDouble(_cursorIndexOfEnhetspris);
            }
            final Integer _tmpAntall;
            if (_cursor.isNull(_cursorIndexOfAntall)) {
              _tmpAntall = null;
            } else {
              _tmpAntall = _cursor.getInt(_cursorIndexOfAntall);
            }
            _item = new Varer(_tmpVarenavn,_tmpEnhetspris,_tmpAntall);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Varer>> listePrId(final int[] alleVarer) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM Varer WHERE varenavn IN (");
    final int _inputSize = alleVarer.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : alleVarer) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"Varer"}, false, new Callable<List<Varer>>() {
      @Override
      public List<Varer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfVarenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "varenavn");
          final int _cursorIndexOfEnhetspris = CursorUtil.getColumnIndexOrThrow(_cursor, "enhetspris");
          final int _cursorIndexOfAntall = CursorUtil.getColumnIndexOrThrow(_cursor, "antall");
          final List<Varer> _result = new ArrayList<Varer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Varer _item_1;
            final String _tmpVarenavn;
            if (_cursor.isNull(_cursorIndexOfVarenavn)) {
              _tmpVarenavn = null;
            } else {
              _tmpVarenavn = _cursor.getString(_cursorIndexOfVarenavn);
            }
            final Double _tmpEnhetspris;
            if (_cursor.isNull(_cursorIndexOfEnhetspris)) {
              _tmpEnhetspris = null;
            } else {
              _tmpEnhetspris = _cursor.getDouble(_cursorIndexOfEnhetspris);
            }
            final Integer _tmpAntall;
            if (_cursor.isNull(_cursorIndexOfAntall)) {
              _tmpAntall = null;
            } else {
              _tmpAntall = _cursor.getInt(_cursorIndexOfAntall);
            }
            _item_1 = new Varer(_tmpVarenavn,_tmpEnhetspris,_tmpAntall);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
