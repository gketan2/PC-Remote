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
			'0': 'esc',
			'1': 'enter',
			'2': 'shiftleft',
			'3': 'shiftright',
			'4': 'ctrlleft',
			'5': 'ctrlright',
			'6': 'tab',
			'7': 'backspace',
			'8': 'delete',
			'9': 'pageup',
			'10': 'pagedown',
			'11': 'home',
			'12': 'end',
			'13': 'up',
			'14': 'down',
			'15': 'left',
			'16': 'right',
			'17': 'volumemute',
			'18': 'volumeup',
			'19': 'volumedown',
			'20': 'pause',
			'21': 'capslock',
			'22': 'numlock',
			'23': 'scrolllock',
			'24': 'insert',
			'25': 'printscreen',
			'26': 'winleft',
			'27': 'winright',
			'28': 'command',#macos
			'29': 'option',#macos
			'30': 'leaving for number match for f1, f2',
			'31': 'f1',
			'32': 'f2',
			'33': 'f3',
			'34': 'f4',
			'35': 'f5',
			'36': 'f6',
			'37': 'f7',
			'38': 'f8',
			'39': 'f9',
			'40': 'f10',
			'41': 'f11',
			'42': 'f12'
		}

	#parameter: single value (not code)
	#receives list(size=1) ex. [(1,4)] or [(0,'t')]
	#
	def pressKeys(self, keys):
		for key in keys:
			try:
				if key["type"] == 1: # if is a special key
					if key["value"] in self.SpecialKeyCodes:
						gui.press(self.SpecialKeyCodes[key["value"]])
				else:
					temp = key["value"]
					if len(temp) == 1:
						gui.press(temp)
			except:
				pass

	#write the given string
	#parameter: string, type = string
	def pressString(self, string):
		gui.write(string)

	#parameter: list of JSON ex. [{"type":1, "value":5},{"type":0, value: "n"},...]
	# type  -> if special or normal  1-> special, 0-> normal
	# value -> ehat to press
	def pressHotKeys(self, keys):
		#make a stack
		stack = Stack()
		temp = 0

		for key in keys:
			try:
				if key["type"] == 1: # if is special type
					if key["value"] in self.SpecialKeyCodes:
						temp = self.SpecialKeyCodes[key["value"]]
						gui.keyDown(temp)
						stack.put(temp)
				else:
					temp = key["value"]
					if len(temp) == 1:
						gui.keyDown(temp)
						stack.put(temp)
			except:
				pass

		#empty the stack
		#unpress the key in the opposite order they were pressed
		while not stack.empty():
			temp = stack.get()
			gui.keyUp(temp)