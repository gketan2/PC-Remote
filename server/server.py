#use HEARTBEAT every 5 second
from PointerController import PointerController
from KeyboardController import KeyboardController
import os
import json
from flask import Flask, request
import requests

password = "password"

isAuthenticated = False
isConnected = True

mouse = PointerController.getInstance()
keyboard = KeyboardController.getInstance()
list = {
	1: 26,
	0: '3'
}
app = Flask(__name__)

@app.route("/")
def root():
	keyboard.pressHotKeys(list) #win+3
	return "I'm gROOT"

# main function to start server
if __name__ == "__main__":
	app.run(host='127.0.0.1', port=5000, debug=True)