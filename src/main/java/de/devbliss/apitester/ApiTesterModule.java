package de.devbliss.apitester;

import com.google.inject.Provides;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import de.devbliss.apitester.factory.DeleteFactory;
import de.devbliss.apitester.factory.GetFactory;
import de.devbliss.apitester.factory.PostFactory;
import de.devbliss.apitester.factory.PutFactory;
import de.devbliss.apitester.factory.impl.DefaultDeleteFactory;
import de.devbliss.apitester.factory.impl.DefaultGetFactory;
import de.devbliss.apitester.factory.impl.DefaultPostFactory;
import de.devbliss.apitester.factory.impl.DefaultPutFactory;

/**
 * Binds all dependencies of ApiTester.
 * 
 * @author hschuetz
 * 
 */
public class ApiTesterModule extends AbstractModule {

    private static final Injector injector = Guice.createInjector(new ApiTesterModule());

    @Override
    protected void configure() {
        bind(GetFactory.class).to(DefaultGetFactory.class);
        bind(DeleteFactory.class).to(DefaultDeleteFactory.class);
        bind(PostFactory.class).to(DefaultPostFactory.class);
        bind(PutFactory.class).to(DefaultPutFactory.class);
        bind(CookieStore.class).to(BasicCookieStore.class);
    }

    @Provides
    public TestState provideTestState(CookieStore cookieStore) {
        // when creating an HttpClient, we need to also create a CookieStore if we ever want to
        // access its cookies, and both of these need to be bound together.  If they were bound
        // as singleton, that wouldn't work, because you could only ever have one session, you
        // couldn't have a test client, admin client, user client, friend client etc.  If they
        // are not bound as singleton, then there would be no way to access them together, you
        // could have a cookie store injected, and a client injected, but it wouldn't be the
        // cookie store for that client.  So, we can't have Guice manage them.  Instead, we
        // have Guice manage the TestState, not singleton, and instantiate the client ourselves.
        DefaultHttpClient client = new DefaultHttpClient();
        client.setCookieStore(cookieStore);
        return new TestState(client, cookieStore);
    }

    /**
     * Creates an instance of the default implementation of {@link GetFactory} as it is bound in
     * this module.
     * 
     * @return
     */
    public static GetFactory createGetFactory() {
        return injector.getInstance(GetFactory.class);
    }

    /**
     * Creates an instance of the default implementation of {@link DeleteFactory} as it is bound in
     * this module.
     * 
     * @return
     */
    public static DeleteFactory createDeleteFactory() {
        return injector.getInstance(DeleteFactory.class);
    }

    /**
     * Creates an instance of the default implementation of {@link PutFactory} as it is bound in
     * this module.
     * 
     * @return
     */
    public static PutFactory createPutFactory() {
        return injector.getInstance(PutFactory.class);
    }

    /**
     * Creates an instance of the default implementation of {@link PostFactory} as it is bound in
     * this module.
     * 
     * @return
     */
    public static PostFactory createPostFactory() {
        return injector.getInstance(PostFactory.class);
    }

    /**
     * Creates an instance of the HTTP state.
     * 
     * @return
     */
    public static TestState createTestState() {
        return injector.getInstance(TestState.class);
    }
}
