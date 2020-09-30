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
			print("---Bad request: json Format not found")
			return False

		if "service" in jsonData:
			service = jsonData["service"]
		else:
			print("---Bad request: No \"service\" Found.")
			return False

		if "sub_service" in jsonData:
			subService = jsonData["sub_service"]
		else:
			print("---Bad request: No \"type\" found")
			return False

		if service == 0:
			print("+password service")
			# password related service
			return False
		elif service == 1:
			print("+keyboard service")
			# keyboard related service
			if subService == 0: #type word
				if "value" in jsonData:
					self.keyboard.pressString(str(jsonData["value"]))
					print("+++Type the string")
					return True
				return False
			elif subService == 1: #press list
				if "key_list" in jsonData:
					self.keyboard.pressKeys(jsonData["key_list"])
					print("+++press keys")
					return True
				return False
			elif subService == 2: #press hotkeys
				if "key_list" in jsonData:
					self.keyboard.pressHotKeys(jsonData["key_list"])
					print("+++press HOTKeys")
					return True
				return False

			print("---Bad request: No corresponding type")
			return False

		elif service == 2:
			print("+Mouse service")
			# mouse related service
			if subService == 0:
				self.mouse.performClick()
				print("+++Single Click")
				return True
			elif subService == 1:
				self.mouse.performDoubleClick()
				print("+++Double Click")
				return True
			elif subService == 2:
				self.mouse.performRightClick()
				print("+++Right Click")
				return True
			elif subService == 3:
				if "value" in jsonData:
					value = jsonData["value"]
					if "x" in value:
						if "y" in value:
							self.mouse.movePointerBy(value["x"], value["y"])
							print("+++Move Pointer By")
							return True

				return False
			elif subService == 4:
				if "value" in jsonData:
					self.mouse.performScroll(jsonData["value"])
					print("+++Scroll")
				return False
			elif subService == 5:
				return False

			print("---Bad reques: No corresponding type")
			return False

		else:
			print("---Bad request: No corresponding service present")
			return False

		return False
"""
Format:
	KEYBOARD:
		{
			1."service":1,
			2."sub_service":0      #0-write string, 1-list of keySet, 2-hotKeys list
			3."key_list":
					[
					{"type":1, "value":4},
					{"type":0, "value":'t'},
					...
					] #array
			  "value":"abcd" #string
		}
	MOUSE:
		sub_service:
			0-click
			1-doubleclick
			2-rightclick
			3-moveTo
			4-moveBy

			1."service":2,
			2."sub_service": 3(from above),
			3."value": (FOR TYPE 3, 4 ONLY) {"x":+-34, "y":+-45}

		{

		}
"""