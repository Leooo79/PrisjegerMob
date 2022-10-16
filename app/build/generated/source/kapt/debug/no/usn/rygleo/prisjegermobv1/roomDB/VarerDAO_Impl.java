package no.usn.rygleo.prisjegermobv1.roomDB;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class VarerDAO_Impl implements VarerDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Varer> __insertionAdapterOfVarer;

  private final EntityDeletionOrUpdateAdapter<Varer> __deletionAdapterOfVarer;

  private final EntityDeletionOrUpdateAdapter<Varer> __updateAdapterOfVarer;

  private final SharedSQLiteStatement __preparedStmtOfUpdate;

  private final SharedSQLiteStatement __preparedStmtOfDelete2;

  public VarerDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVarer = new EntityInsertionAdapter<Varer>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Varer` (`listenavn`,`varenavn`,`enhetspris`,`antall`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Varer value) {
        if (value.getListenavn() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getListenavn());
        }
        if (value.getVarenavn() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getVarenavn());
        }
        if (value.getEnhetspris() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.getEnhetspris());
        }
        if (value.getAntall() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getAntall());
        }
      }
    };
    this.__deletionAdapterOfVarer = new EntityDeletionOrUpdateAdapter<Varer>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Varer` WHERE `listenavn` = ? AND `varenavn` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Varer value) {
        if (value.getListenavn() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getListenavn());
        }
        if (value.getVarenavn() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getVarenavn());
        }
      }
    };
    this.__updateAdapterOfVarer = new EntityDeletionOrUpdateAdapter<Varer>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Varer` SET `listenavn` = ?,`varenavn` = ?,`enhetspris` = ?,`antall` = ? WHERE `listenavn` = ? AND `varenavn` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Varer value) {
        if (value.getListenavn() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getListenavn());
        }
        if (value.getVarenavn() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getVarenavn());
        }
        if (value.getEnhetspris() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.getEnhetspris());
        }
        if (value.getAntall() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getAntall());
        }
        if (value.getListenavn() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getListenavn());
        }
        if (value.getVarenavn() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getVarenavn());
        }
      }
    };
    this.__preparedStmtOfUpdate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE varer SET antall=? WHERE varenavn = ? AND listenavn = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDelete2 = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM varer WHERE varenavn = ? AND listenavn = ?";
        return _query;
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
  public void update2(final Varer varer) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfVarer.handle(varer);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final int nyAntall, final String varenavn, final String listenavn) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdate.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, nyAntall);
    _argIndex = 2;
    if (varenavn == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, varenavn);
    }
    _argIndex = 3;
    if (listenavn == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, listenavn);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdate.release(_stmt);
    }
  }

  @Override
  public void delete2(final String varenavn, final String listenavn) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete2.acquire();
    int _argIndex = 1;
    if (varenavn == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, varenavn);
    }
    _argIndex = 2;
    if (listenavn == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, listenavn);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete2.release(_stmt);
    }
  }

  @Override
  public Flow<List<Varer>> getAlleVarer(final String listenavn) {
    final String _sql = "SELECT * FROM Varer WHERE listenavn IN (?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (listenavn == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, listenavn);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"Varer"}, new Callable<List<Varer>>() {
      @Override
      public List<Varer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfListenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "listenavn");
          final int _cursorIndexOfVarenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "varenavn");
          final int _cursorIndexOfEnhetspris = CursorUtil.getColumnIndexOrThrow(_cursor, "enhetspris");
          final int _cursorIndexOfAntall = CursorUtil.getColumnIndexOrThrow(_cursor, "antall");
          final List<Varer> _result = new ArrayList<Varer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Varer _item;
            final String _tmpListenavn;
            if (_cursor.isNull(_cursorIndexOfListenavn)) {
              _tmpListenavn = null;
            } else {
              _tmpListenavn = _cursor.getString(_cursorIndexOfListenavn);
            }
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
            _item = new Varer(_tmpListenavn,_tmpVarenavn,_tmpEnhetspris,_tmpAntall);
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
  public Flow<List<Varer>> getAlleVarer2() {
    final String _sql = "SELECT * FROM Varer";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"Varer"}, new Callable<List<Varer>>() {
      @Override
      public List<Varer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfListenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "listenavn");
          final int _cursorIndexOfVarenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "varenavn");
          final int _cursorIndexOfEnhetspris = CursorUtil.getColumnIndexOrThrow(_cursor, "enhetspris");
          final int _cursorIndexOfAntall = CursorUtil.getColumnIndexOrThrow(_cursor, "antall");
          final List<Varer> _result = new ArrayList<Varer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Varer _item;
            final String _tmpListenavn;
            if (_cursor.isNull(_cursorIndexOfListenavn)) {
              _tmpListenavn = null;
            } else {
              _tmpListenavn = _cursor.getString(_cursorIndexOfListenavn);
            }
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
            _item = new Varer(_tmpListenavn,_tmpVarenavn,_tmpEnhetspris,_tmpAntall);
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
  public String getVare(final String varenavn) {
    final String _sql = "SELECT varenavn FROM Varer WHERE varenavn IN (?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (varenavn == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, varenavn);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Varer> listePrId(final int[] alleVarer) {
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
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfListenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "listenavn");
      final int _cursorIndexOfVarenavn = CursorUtil.getColumnIndexOrThrow(_cursor, "varenavn");
      final int _cursorIndexOfEnhetspris = CursorUtil.getColumnIndexOrThrow(_cursor, "enhetspris");
      final int _cursorIndexOfAntall = CursorUtil.getColumnIndexOrThrow(_cursor, "antall");
      final List<Varer> _result = new ArrayList<Varer>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Varer _item_1;
        final String _tmpListenavn;
        if (_cursor.isNull(_cursorIndexOfListenavn)) {
          _tmpListenavn = null;
        } else {
          _tmpListenavn = _cursor.getString(_cursorIndexOfListenavn);
        }
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
        _item_1 = new Varer(_tmpListenavn,_tmpVarenavn,_tmpEnhetspris,_tmpAntall);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
