
# taoAigFacade API V3

This page describes the API of the "taoAigFacade" module that is an extension of [TAO](http://en.wikipedia.org/wiki/TAO_%28e-Testing_platform%29). TaoAigFacade aims to create an assessment Test into TAO.

## Preamble
- taoAigFacade provide a way to create assessments tests and test-takers (users). Test-takers are used to identify user when running a assessment test
- A demo that implements the API can be found [here](http://dfki.aig.list.lu/taoAigFacade/demo/index.html).

## Create a test-taker/user
HTTP Method: POST

URL format: http://dfki.aig.list.lu/taoAigFacade/user/login/pwd/password

Where:
- login: user login
- password: user password

The request is case sensitive

HTTP Response: code 201 on success

## Create a test for an existing user
HTTP Method: POST

URL: http://dfki.aig.list.lu/taoAigFacade/testDelivery/

HTTP POST Parameters (in the HTTP body):
- request: the JSON representation of the request object described below

HTTP Response: code 201 on success with a JSON response containing the URI of the test described below

### JSON test request
- request/user: user Object for whom the test is requested
- request/user/login: user login
- request/user/password: user password
- request/source-language: the source language
- request/target-language: the target language
- request/referrer: referrer of the page
- request/words: list of words, a word is an Object that contains mainly a label and a concept URI

#### Example of JSON test request

>     {
>         "request":{
>            "user":{
>                "login":"my login",
>                "pwd":"my password"
>            },
>            "source-language":"de",
>            "target-language":"fr",
>            "referrer":"http://mydomain.com/mypage.html",
>            "words":[
>                {
>                    "label":"BMI",
>                    "uri":"http://www.interlingua-project.eu/models/ontologies/interlingua.rdf#BMI",
>                    "DBpediaURI":"http://dbpedia.org/resource/Body_mass_index",
>                    "learningRsc":"http://a-domain.com/resource.pdf"
>                },
>                {
>                    "label":"BLUE",
>                    "uri":"http://www.interlingua-project.eu/models/ontologies/interlingua.rdf#BLUE"
>                }
>                ]
>             }
>     }

### JSON test response
- test/uri: URI of the test that is mandatory to address TAO

#### Example of JSON test response

>     {
>         "test":{
>            "uri":"http://dfki.aig.list.lu/dfkiaiglistlu.rdf#i14284184651546546158"
>             }
>     }

## Launch a test
Running a test is performed directly through TAO by addressing TAO with test-taker/user credentials and the URI of the test. Then the request (i.e. the browser) will be redirected to the test.

URL: http://dfki.aig.list.lu/tao/Main/login

HTTP Method: POST

HTTP Headers:
- loginForm_sent: "1"
- login: user login
- password: user password
- connect: "Log in" 
- redirect: the test path is the concatenation of "/taoDelivery/DeliveryServer/initDeliveryExecution?uri=" and the test URI delivered by the facade. Test URI has to be [url_encoded](http://www.w3schools.com/jsref/jsref_encodeURIComponent.asp) before concatenation.

__________________

# ~~taoAigFacade API V2~~ (obsolete/broken)

## Create a test-taker/user
HTTP Method: POST

URL format: http://dfki.aig.list.lu/taoAigFacade/user/login/pwd/password

Where:
- login: user login
- password: user password

The request is case sensitive

HTTP Response: code 201 on success

## Create a test for an existing user
HTTP Method: POST

URL: http://dfki.aig.list.lu/taoAigFacade/testDelivery/

HTTP POST Parameters (in the HTTP body):
- request: the JSON representation of the request object described below

HTTP Response: code 201 on success with a JSON response containing the URI of the test described below

### JSON test request
- request/user: user Object for whom the test is requested
- request/user/login: user login
- request/user/password: user password
- request/text-language: the original language
- request/wanted-language: the wanted language
- request/referrer: referrer of the page
- request/words: list of words, a word is an Object that contains a label and an URI

#### Example of JSON test request

>     {
>         "request":{
>            "user":{
>                "login":"my login",
>                "pwd":"my password"
>            },
>            "text-language":"fr",
>            "wanted-language":"en",
>            "referrer":"http://mydomain.com/mypage.html",
>            "words":[
>                {
>                    "label":"Sucre",
>                    "uri":"http://dbpedia.org/resource/Sucre"
>                },
>                {
>                    "label":"Paris",
>                    "uri":"http://dbpedia.org/resource/Paris"
>                }
>                ]
>             }
>     }

### JSON test response
- test/uri: URI of the test that is mandatory to address TAO

#### Example of JSON test response

>     {
>         "test":{
>            "uri":"http://dfki.aig.list.lu/dfkiaiglistlu.rdf#i14284184651546546158"
>             }
>     }

## Launch a test
Running a test is performed directly through TAO by addressing TAO with test-taker/user credentials and the URI of the test. Then the request (i.e. the browser) will be redirected to the test.

URL: http://dfki.aig.list.lu/tao/Main/login

HTTP Method: POST

HTTP Headers:
- loginForm_sent: "1"
- login: user login
- password: user password
- connect: "Log in" 
- redirect: the test path is the concatenation of "/taoDelivery/DeliveryServer/initDeliveryExecution?uri=" and the test URI delivered by the facade. Test URI has to be [url_encoded](http://www.w3schools.com/jsref/jsref_encodeURIComponent.asp) before concatenation.

__________________

# ~~taoAigFacade API V1~~ (obsolete/broken)

## Preamble
- taoAigFacade extension needs client authentication using "HTTP/Basic"
- taoAigFacade provide a way to create assessments tests and test-takers (users). Test-takers are used to identify user when running a assessment test
- HTTP response can have content-type "application/json" or "application/xml"
- A demo that implements the API can be found [here](http://dfki.aig.list.lu/taoAigFacade/demo/index.html). This demo embeds TAO credentials.

## Create a test-taker/user
URL: http://dfki.aig.list.lu/taoSubjects/RestSubjects

HTTP Method: POST

HTTP Headers:
- label: the label of the test-taker (can be the login)
- login: the login of the test-taker
- password: the password of the test-taker
- Authorization: the HTTP/Basic authentication of a taoAigFacade manager (concatenation of "Basic " and base64 representation of concatenation of login, colon character and password)

## Get a test URI
URL: http://dfki.aig.list.lu/taoAigFacade/RestFacade

HTTP Headers:
- Authorization: the HTTP/Basic authentication of a taoAigFacade manager (concatenation of "Basic " and base64 representation of concatenation of login, colon character and password)
URL parameters:
- request: the request as a JSON string described below

### JSON test request
- request/user: user Object for whom the test is requested
- request/user/login: user login
- request/user/password: user password
- request/text-language: the original language
- request/wanted-language: the wanted language
- request/referrer: referrer of the page
- request/words: list of words, a word is an Object that contains a label and an URI

#### Example of JSON test request

>     {
>         "request":{
>            "user":{
>                "login":"my login",
>                "pwd":"my password"
>            },
>            "text-language":"fr",
>            "wanted-language":"en",
>            "referrer":"http://mydomain.com/mypage.html",
>            "words":[
>                {
>                    "label":"Sucre",
>                    "uri":"http://dbpedia.org/resource/Sucre"
>                },
>                {
>                    "label":"Paris",
>                    "uri":"http://dbpedia.org/resource/Paris"
>                }
>                ]
>             }
>     }


## Launch a test
Running a test is performed directly through TAO by providing test-taker/user credentials and the URI of the test.
URL: http://dfki.aig.list.lu/tao/Main/login

HTTP Method: POST

HTTP Headers:
- loginForm_sent: "1"
- login: user login
- password: user password
- connect: "Log in" 
- redirect: the test path (see below)

Test path is the concatenation of "/taoDelivery/DeliveryServer/initDeliveryExecution?uri=" and the test URI delivered by the facade. Test URI has to be [url_encoded](http://www.w3schools.com/jsref/jsref_encodeURIComponent.asp) before concatenation.