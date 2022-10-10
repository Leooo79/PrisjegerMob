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
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BrukerDAO_Impl implements BrukerDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Bruker> __insertionAdapterOfBruker;

  private final EntityDeletionOrUpdateAdapter<Bruker> __deletionAdapterOfBruker;

  public BrukerDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBruker = new EntityInsertionAdapter<Bruker>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Bruker` (`brukerId`,`brukerNavn`,`passord`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Bruker value) {
        stmt.bindLong(1, value.getBrukerId());
        if (value.getBrukerNavn() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getBrukerNavn());
        }
        if (value.getPassord() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPassord());
        }
      }
    };
    this.__deletionAdapterOfBruker = new EntityDeletionOrUpdateAdapter<Bruker>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Bruker` WHERE `brukerId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Bruker value) {
        stmt.bindLong(1, value.getBrukerId());
      }
    };
  }

  @Override
  public void insertAll(final Bruker... brukere) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBruker.insert(brukere);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Bruker bruker) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfBruker.handle(bruker);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Bruker>> getAlleBrukere() {
    final String _sql = "SELECT * FROM Bruker";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Bruker"}, false, new Callable<List<Bruker>>() {
      @Override
      public List<Bruker> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBrukerId = CursorUtil.getColumnIndexOrThrow(_cursor, "brukerId");
          final int _cursorIndexOfBrukerNavn = CursorUtil.getColumnIndexOrThrow(_cursor, "brukerNavn");
          final int _cursorIndexOfPassord = CursorUtil.getColumnIndexOrThrow(_cursor, "passord");
          final List<Bruker> _result = new ArrayList<Bruker>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Bruker _item;
            final int _tmpBrukerId;
            _tmpBrukerId = _cursor.getInt(_cursorIndexOfBrukerId);
            final String _tmpBrukerNavn;
            if (_cursor.isNull(_cursorIndexOfBrukerNavn)) {
              _tmpBrukerNavn = null;
            } else {
              _tmpBrukerNavn = _cursor.getString(_cursorIndexOfBrukerNavn);
            }
            final String _tmpPassord;
            if (_cursor.isNull(_cursorIndexOfPassord)) {
              _tmpPassord = null;
            } else {
              _tmpPassord = _cursor.getString(_cursorIndexOfPassord);
            }
            _item = new Bruker(_tmpBrukerId,_tmpBrukerNavn,_tmpPassord);
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
  public List<Bruker> listePrId(final int[] alleBrukerId) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM Bruker WHERE brukerId IN (");
    final int _inputSize = alleBrukerId.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : alleBrukerId) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBrukerId = CursorUtil.getColumnIndexOrThrow(_cursor, "brukerId");
      final int _cursorIndexOfBrukerNavn = CursorUtil.getColumnIndexOrThrow(_cursor, "brukerNavn");
      final int _cursorIndexOfPassord = CursorUtil.getColumnIndexOrThrow(_cursor, "passord");
      final List<Bruker> _result = new ArrayList<Bruker>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Bruker _item_1;
        final int _tmpBrukerId;
        _tmpBrukerId = _cursor.getInt(_cursorIndexOfBrukerId);
        final String _tmpBrukerNavn;
        if (_cursor.isNull(_cursorIndexOfBrukerNavn)) {
          _tmpBrukerNavn = null;
        } else {
          _tmpBrukerNavn = _cursor.getString(_cursorIndexOfBrukerNavn);
        }
        final String _tmpPassord;
        if (_cursor.isNull(_cursorIndexOfPassord)) {
          _tmpPassord = null;
        } else {
          _tmpPassord = _cursor.getString(_cursorIndexOfPassord);
        }
        _item_1 = new Bruker(_tmpBrukerId,_tmpBrukerNavn,_tmpPassord);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Bruker findByBrukerNavnOgPassord(final String first, final String last) {
    final String _sql = "SELECT * FROM Bruker WHERE brukerNavn LIKE ? AND passord LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (first == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, first);
    }
    _argIndex = 2;
    if (last == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, last);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBrukerId = CursorUtil.getColumnIndexOrThrow(_cursor, "brukerId");
      final int _cursorIndexOfBrukerNavn = CursorUtil.getColumnIndexOrThrow(_cursor, "brukerNavn");
      final int _cursorIndexOfPassord = CursorUtil.getColumnIndexOrThrow(_cursor, "passord");
      final Bruker _result;
      if(_cursor.moveToFirst()) {
        final int _tmpBrukerId;
        _tmpBrukerId = _cursor.getInt(_cursorIndexOfBrukerId);
        final String _tmpBrukerNavn;
        if (_cursor.isNull(_cursorIndexOfBrukerNavn)) {
          _tmpBrukerNavn = null;
        } else {
          _tmpBrukerNavn = _cursor.getString(_cursorIndexOfBrukerNavn);
        }
        final String _tmpPassord;
        if (_cursor.isNull(_cursorIndexOfPassord)) {
          _tmpPassord = null;
        } else {
          _tmpPassord = _cursor.getString(_cursorIndexOfPassord);
        }
        _result = new Bruker(_tmpBrukerId,_tmpBrukerNavn,_tmpPassord);
      } else {
        _result = null;
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
