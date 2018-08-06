package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;


@Database(entities = {ClienteMin.class, Usuario.class, Producto.class, Actualizacion.class, Pedido.class, DetallePedido.class} , version = 6)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ClienteMinDao clienteMinDao();
    public abstract UsuarioDao usuarioDao();
    public abstract ProductoDao productoDao();
    public abstract ActualizacionDao actualizacionDao();
    public abstract PedidoDao pedidoDao();
    public abstract DetallePedidoDao detallePedidoDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "camaleondb")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
