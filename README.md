# SMSHub
Android application for sending and receiving SMS via HTTP API in Kotlin

## what?

SMSHub is an application for Android phones you can use to add SMS funcitonality to your software (notifications).

## how?

1- The application connects at regular intervals to a URL

2- It should read a JSON containing *message*, *number* and *id*, or an empty response if there is nothing to send
```
{ "message": "hola mundo!", "number": "3472664455", "id": "1" }
```

3- The app will send the SMS *message* to *number*

4- Once sent (or failed) the app will notify the status to another URL

## why?

Commercial SMS APIs are (depending on the case) prohibitively expensive. 
Instead you can use your own phone line to send SMS with an Android phone as a gateway.

## settings

you can customize the next settings directly in the application

+ *send URL*: messages will be parsed from this URL, you return a JSON containing *message*, *number* and *id*
+ *interval*: the app will check whether there is an incoming message each specific interval in minutes
+ *status URL*: once a message is sent, status will be reported to this URL via GET parameters, *id* and *status* (SENT, FAILED)
