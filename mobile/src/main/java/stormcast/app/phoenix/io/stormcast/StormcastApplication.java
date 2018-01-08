package stormcast.app.phoenix.io.stormcast;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by sudharti on 1/7/18.
 */

public class StormcastApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.Initializer initializer = Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build();
        Stetho.initialize(initializer);
    }
}
