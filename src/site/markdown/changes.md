CHANGES
=============

Version 0.8.0: 2014-08-28
-------------------------
- string payload in requests is treated as text/plain now

Version 0.7.0: 2014-01-09
-------------------------
- add additionalHeaders parameter to all http methods
- add new http method PATCH

Version 0.6.4: 2012-10-23
-------------------------
- add a map containing the cookies in the apiRequest object

Version 0.6.3: 2012-10-19
-------------------------
- create the context object which contains the response and the request and is returned by the requests

Version 0.6.2: 2012-08-17
-------------------------
- using junit-dep as dependency
- added optional one-time requestFactory overwrite for all put, get, delete, post

Version 0.5: 2012-07-20
-----------------------
- added support to access and manipulate cookies
- added optional injected setter for the factories in the Apitest class to be able to inject the ApiTest class easily.

Version 0.3: 2012-07-10
-----------------------
- additional ApiResponse#payloadJsonAs(TypeReference)

Version 0.2: 2012-07-10
-----------------------
 - tests

Version 0.1: 2012-07-05
-----------------------
 - initial release
 - core functionality available
