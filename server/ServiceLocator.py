from PointerController import PointerController
from KeyboardController import KeyboardController

import json

class ServiceLocator:

	__instance = None

	@staticmethod
	def getInstance():
		if ServiceLocator.__instance == None:
			ServiceLocator.__instance = ServiceLocator()
		return ServiceLocator.__instance

	def __init__(self):
		self.keyboard = KeyboardController.getInstance()
		self.mouse = PointerController.getInstance()

	def stringToJson(self, stringData):
		return json.loads(stringData)

	# entry point of this class
	def execute(self, data):
		try:
			jsonData = self.stringToJson(data)
		except:
			print("Bad request: json Format not found")
			return False

		if "service" in jsonData:
			service = jsonData["service"]
		else:
			print("Bad request: No \"service\" Found.")
			return False

		if service == 0:
			print("password service")
			# password related service
			return True
		elif service == 1:
			print("keyboard service")
			# keyboard related service
			if "type" in jsonData:
				if jsonData["type"] == 0: #type word
					if "value" in jsonData:
						self.keyboard.pressString(str(jsonData["value"]))
						return True
					return False
				elif jsonData["type"] == 1: #press list
					if "key_list" in jsonData:
						self.keyboard.pressKeys(jsonData["key_list"])
						return True
					return False
				elif jsonData["type"] == 2: #press hotkeys
					if "key_list" in jsonData:
						self.keyboard.pressHotKeys(jsonData["key_list"])
					return True

			return False
		elif service == 2:
			print("Mouse service")
			# mouse related service
			return True
		else:
			print("Bad request: No corresponding service present")
			return False

		return False
"""
Format:
	KEYBOARD:
		{
			1."service":"1",
			2."type":"0"    #0-write string, 1-list of keySet, 2-hotKeys list
			3."key_list":
					[
					{"type":1, "value":4},
					{"type":0, "value":'t'},
					...
					] #array
			  "value":"abcd" #string
		}
	MOUSE:
		{

		}
"""