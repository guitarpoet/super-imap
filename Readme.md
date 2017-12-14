# Super IMAP, the only local IMAP service that I really want to use

## The problem

I love to use [NeoMutt](https://www.neomutt.org) to check mail in plain text all the time.

But since using IMAP directly is not so fast, I need to use an native IMAP service to synchronize the
local folders that NeoMutt will use to the remove server's IMAP folders.

I used to use [OfflineIMAP](http://www.offlineimap.org) do this task, but there are a few problems with it.

1. It is so slow
2. It didn't support concurrent connections to get mails and check folders well(multi threading)
3. It don't ever support Push Mail

## Why Java?

1. To start an IMAP client from scratch is insane, even the IMAP is not a very complex protocol. Gladly we have an good open source implementation called [JavaMail](https://javaee.github.io/javamail/)
    * JavaMail is a part of [Java EE](http://javaee.github.io), it is good and stable enough since it support many enterprise level applications, and run for years
    * JavaMail is open sourced, so we can use it without any problems and licensed issues
    * JavaMail is still at active development, and its API is a Java standard, used by many developer
2. Java 8 has many nice features, nice concurrent library(maybe the best concurrent library on earth, I think), and Java application will be much stable than the Python based OfflineIMAP(it is not because of python
just because the code of OfflineIMAP is too old, and maybe using some quite old libraries, it still using Python 2, for example)
3. Java 8 can run across the platforms, and with JRE, I can have as many resources and libraries that I need, so the installation of this application won't be that big if you already have the JRE
4. Spring boot and JLines brings very good experience of writting console applications, and I'll make best use of them to bring the application better.

## The Parts

This project will break up to these parts:

1. A console IMAP viewer(won't support mail viewing, just view the folder and the message information), which is readline alike one, with text completion and syntax coloring(your terminal must support it)
2. An IMAP synchronizer which will support the console IMAP viewer(mostly the console viewer is the debugger for this one, this one is the most used one), it will synchronize the local and remote IMAP folders automatically,
it will run as a service in the background, and accept the server's event to synchronize the folders automatically, and will download the messages, save them to local folder, and then extract the text from the message(and header)
and the attachments, and create a lucene index for them, so that you can search for the messages
3. The IMAP viewer can also used for searching the messages, search result should be able used by the NeoMutt
4. It will have many plugin and hook support, so you can hook the notifications into it when there is some event on the server


So, this is not an easy project to do, and I don't have quite much time on it, I'll try take more time on it to make it better.

The first step is the Console IMAP Viewer, I'll try make it done ASAP.
