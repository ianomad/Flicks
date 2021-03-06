package codepath.flicks.components;

import javax.inject.Singleton;

import codepath.flicks.DetailsActivity;
import codepath.flicks.MainActivity;
import codepath.flicks.modules.CommonModule;
import codepath.flicks.presenters.MainPresenter;
import dagger.Component;

@Singleton
@Component(modules = CommonModule.class)
public interface AppComponent {

    public void inject(MainActivity activity);

    public void inject(DetailsActivity activity);

    public void inject(MainPresenter target);
}
