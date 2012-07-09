package de.devbliss.apitester;

import org.apache.http.client.HttpClient;
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
        bind(HttpClient.class).to(DefaultHttpClient.class);
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
     * Creates an instance of the Apache HTTP client implementation that is used in ApiTester.
     * 
     * @return
     */
    public static HttpClient createHttpClient() {
        return injector.getInstance(HttpClient.class);
    }
}
