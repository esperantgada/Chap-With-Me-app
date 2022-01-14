This app is a small chat app that allows user to share messages between themselves or communicate.
It uses the following technologies:

#Firebase Authentication
#Firebase RealTime Database
#Kotlin
#RecyclerView pattern
#View binding class

The [Firebase authentication] is used to handle user logging and signing up.
The [RealTime Database] is used to save the messages.

How to use it?
-------------
The first time users want to login the app, they need to register by creating an account with theirs 
[name, email address and password]. After registering, they can login by using only their [email address
and password]. Then by clicking [LOGIN] button, they will be redirected to the [ChatActivity] where
they can see a list of other users or friend and start sharing messages with them.