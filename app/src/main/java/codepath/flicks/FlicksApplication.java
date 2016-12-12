package codepath.flicks;

import android.app.Application;

import codepath.flicks.components.AppComponent;
import codepath.flicks.components.DaggerAppComponent;
import codepath.flicks.modules.CommonModule;


public class FlicksApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .commonModule(new CommonModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
