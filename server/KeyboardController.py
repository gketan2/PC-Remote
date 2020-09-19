import pyautogui as gui
from queue import LifoQueue as Stack

class KeyboardController:
	__instance = None

	@staticmethod
	def getInstance():
		if KeyboardController.__instance == None:
			KeyboardController.__instance = KeyboardController()
		return KeyboardController.__instance

	def __init__(self):
		self.SpecialKeyCodes = {
			0: 'esc',
			1: 'enter',
			2: 'shiftleft',
			3: 'shiftright',
			4: 'ctrlleft',
			5: 'ctrlright',
			6: 'tab',
			7: 'backspace',
			8: 'delete',
			9: 'pageup',
			10: 'pagedown',
			11: 'home',
			12: 'end',
			13: 'up',
			14: 'down',
			15: 'left',
			16: 'right',
			17: 'volumemute',
			18: 'volumeup',
			19: 'volumedown',
			20: 'pause',
			21: 'capslock',
			22: 'numlock',
			23: 'scrolllock',
			24: 'insert',
			25: 'printscreen',
			26: 'winleft',
			27: 'winright',
			28: 'command',#macos
			29: 'option',#macos
			30: 'leaving for number match for f1, f2',
			31: 'f1',
			32: 'f2',
			33: 'f3',
			34: 'f4',
			35: 'f5',
			36: 'f6',
			37: 'f7',
			38: 'f8',
			39: 'f9',
			40: 'f10',
			41: 'f11',
			42: 'f12'
		}

	#parameter: single KeyCode
	def pressKey(self, value):
		if len(alue) != 1:
			return
		#ascii = ord(alphabet)  #ascii code
		#if ascii >= 65 and ascii <= 90:
		gui.press(value)

	#write the given string
	def pressString(self, string):
		gui.write(string)

	#parameter: dictionary of KeyCode    keytype: Code   ex- special:28 or normal: 5
	# 0 -> normal, 1 -> special
	def pressHotKeys(self, dictionary):
		# make a stack
		stack = Stack()
		temp = 0
		for key  in dictionary:
			if key == 1: # if it is special
				if dictionary[key] in self.SpecialKeyCodes:
					temp = self.SpecialKeyCodes[dictionary[key]]
					stack.put(temp)
					gui.keyDown(temp)
					#print(temp)
			else:
				temp = dictionary[key]
				if len(temp) == 1:
					stack.put(temp)
					gui.keyDown(temp)
					#print(temp)

		#empty the stack
		#unpress the key in the opposite order they were pressed
		while not stack.empty():
			temp = stack.get()
			gui.keyUp(temp)
			#print(temp)

	#parameter: keyCode
	def pressSpecialKey(self, code):
		if code in self.SpecialKeyCodes:
			gui.press(self.SpecialKeyCodes[code])

#1. gui.keyDown(codeValue) -> gui.keyUp(same-codeValue)
#2. gui.press(code) (codeValue or alphabet)
#3. gui.write(string/listOfCodeValues)
#4. gui.hotkey(vararg of string/codeValue)    can use in place of 1

#testing
#keyboard = KeyboardController.getInstance()
#keyboard.pressKey('#')
#list = {
#	1: 26,
#	0: '3'
#}
#keyboard.pressHotKeys(list)