# Working with Firestorm and App Engine

If your project is hosted using Google's App Engine, you must create a new `ServletContextListener` which initializes
Firebase, each of the databases you want to use (Firestore & Real-time database), and then registers your classes.

The code executed by the context listener is only executed once, so this will allow you to use Firestorm more efficiently.

```java
public class FirestormContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //Initialize Firebase:
        //TODO - Initialize Firebase access using a service account here.

        //Initialize Firestore and/or Real-time database:
        //TODO - Initialize data store access here.

        //Register classes:
        //TODO - Register your classes here.
    }

}
```