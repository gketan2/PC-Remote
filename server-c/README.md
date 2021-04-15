# Server Files
This directory contain server side code for linux and windows in C/C++ language.
#### Functions
* Run a tcp / udp server with libraries corresponding to OS.
* Receive signal from mobile(can be any device with socket connection) and perform cursor movement and mouse button click.

## Linux
##### Libraries Used :
  * **X11/Xlib.h**
  * **X11/extensions/XTest.h**
  * **sys/socket.h**
  * **signal.h** (for catching keyboard interrupt)

Compilation argument :
  * `-lX11`

## Windows
#### Win32 api used :
  * **winsock2.h**
  * **windows.h**
  * **winuser.h**

Compilation arguments :
  * `-lwsock32 -lws2_32`
