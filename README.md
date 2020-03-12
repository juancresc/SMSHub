# SMSHub

## Update

I'n not currently giving support or updating this app. However I'm using it in a production environment with no issues

## what?

SMSHub is an SMS Gateway application for Android phones (Android Studio project developed in Kotlin) you can use to add SMS functionality to your software.
It connects to a webpage to retrieve messages to be sent (in JSON format) at regular intervals. It also notifies about delivery status and incoming messages. 


## why?

Commercial SMS APIs are (for most cases) prohibitively expensive. 
Instead you can use your own phone line to send SMS with an Android phone as a gateway.

There are other SMS gateways projects but as far as I could check when this project started, none of them can be use to send and receive SMS via HTTP API easily and freely (with no commercial dependencies).


## how?

You can download a compiled .apk file from the beta release [here](https://github.com/juancrescente/SMSHub/releases/download/0.1/app-release.apk)

### settings

you can customize the next settings directly in the application

#### Send SMS:
+ *Enable sending*: whether the app should read from the API and send messages
+ *send URL*: messages will be parsed from this URL, you return a JSON containing *message*, *number* and *id*
+ *interval*: the app will check whether there is an incoming message for sending each specific interval in minutes
+ *status URL*: once a message is sent, status will be reported to this URL via GET parameters, *id* and *status* (SENT, FAILED, DELIVERED)

#### Receive SMS:
+ *receive URL*: Message received will be posted here. If nothing is specified it will skip this action.


### How sending SMSs works

1- The application connects at regular intervals to a URL

```
POST https://yourcustomurl.com/send_api
    deviceId: 1
    action: SEND
```

2- It should read a JSON containing *message*, *number* and *id*, or an empty response if there is nothing to send
```
{ "message": "hola mundo!", "number": "3472664455", "messageId": "1" }
```

3- The app will send the SMS *message* to *number*

4- Once sent (or failed) the app will notify the status to the status URL
```
POST https://yourcustomurl.com/status_api
    deviceId: 1
    messageId: 1
    status: SENT
    action: STATUS
```

5- Once delivered the app will notify the status to the status URL

```
POST https://yourcustomurl.com/status_api
    deviceId: 1
    messageId: 1
    status: DELIVERED
    action: STATUS
```

Possible _status_ values are: SENT, FAILED, DELIVERED (notice that it is unlikely but possible to get the DELIVERED update before the SENT update due to requests delay).


### How receiving SMSs works

1- Each time a SMS is received the app will notify the received URL
```
POST https://yourcustomurl.com/received_api
    deviceId: 1
    number: 3472556699
    message: Hello man!
    action: RECEIVED
```

