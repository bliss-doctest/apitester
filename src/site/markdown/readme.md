apitester
=========

Small wrapper around Apache HttpClient to make the most common tasks for testing a REST API more convenient.

For single HTTP calls, just use one of the static overloaded methods in Getter, Poster, Putter or Deleter. 
They allow you to use a custom request factory in case you have special requirements how a request should look like, 
as well as they let you hand over your TestState instance that holds the HTTP client.

In case you need to make a sequence of calls with same request implementation and the same TestState instance, you may instanciate ApiTester and use its methods, which will delegate to Getter, Poster, Putter or Deleter mentioned above.
Of course, you can use ApiTester as superclass of your testclass. 

As a result of your HTTP calls, you receive an ApiResponse object, which wraps the HTTP statuscode and the raw response body, and can automatically convert the raw response body by parsing it as JSON to any class you wish (assuming the JSON fits your class definition, of course).
