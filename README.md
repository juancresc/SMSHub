# SMSHub
Android application for sending and receiving SMS via HTTP API in Kotlin

## what?

SMSHub is an application for Android phones you can use to add SMS functionality to your software (notifications).

## how?

### Send SMSs

1- The application connects at regular intervals to a URL

```
https://yourcustomurl.com/send_api
```

2- It should read a JSON containing *message*, *number* and *id*, or an empty response if there is nothing to send
```
{ "message": "hola mundo!", "number": "3472664455", "id": "1" }
```

3- The app will send the SMS *message* to *number*

4- Once sent (or failed) the app will notify the status to the status URL
```
POST https://yourcustomurl.com/status_api
    deviceId: 1
    messageId=1
    status=SENT
```

5- Once delivered the app will notify the status to the status URL

```
POST https://yourcustomurl.com/status_api
    deviceId: 1
    messageId=1
    status=DELIVERED
```

Possible _status_ values are: SENT, FAILED, DELIVERED

### Receive SMSs

1- Each time a SMS is received the app will notify the received URL
```
POST https://yourcustomurl.com/received_api
    deviceId: 1
    number: 3472556699
    message: Hello man!
```


## why?

Commercial SMS APIs are (depending on the case) prohibitively expensive. 
Instead you can use your own phone line to send SMS with an Android phone as a gateway.

## settings

you can customize the next settings directly in the application

+ *send URL*: messages will be parsed from this URL, you return a JSON containing *message*, *number* and *id*
+ *interval*: the app will check whether there is an incoming message each specific interval in minutes
+ *status URL*: once a message is sent, status will be reported to this URL via GET parameters, *id* and *status* (SENT, FAILED, DELIVERED)
+ *received URL*: Message received will be posted here. If nothing is specified it will skip this action.
