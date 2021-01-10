import pyautogui as gui

class PointerController:
	__instance = None

	@staticmethod
	def getInstance():
		if PointerController.__instance == None:
			PointerController.__instance = PointerController()
		return PointerController.__instance

	width = 0
	height = 0

	def __init__(self):
		gui.FAILSAFE = False
		wh = gui.size()
		self.height = wh.height # 0 based height
		self.width = wh.width   # 0 based width
		#gc.collect(wh)

	def getScreenWidth(self):
		return self.width

	def getScreenHeight(self):
		return self.height

	#NOTE :
	#Exception when click at corners
	# (0, 0), (0, height-1), (width-1, 0), (width-1, height-1)

	#Should be ratio-handled point
	def movePointerTo(self, x, y):
		if x <= 0:
			x = 1
		elif x >= self.width -1:
			x = self.width - 2
		if y <= 0:
			y = 1
		elif y >= self.height -1:
			y = self.height - 2
		gui.moveTo(x, y)

	# -ve x = move left
	# -ve y = move up
	# Should be ratio-handeled length
	def movePointerBy(self, x, y):
		gui.move(x, y)

	def performClick(self):
		p = gui.position()
		if p.x <= 0:
			p.x = 1
		elif p.x >= self.width - 1:
			p.x = self.width - 2
		if p.y <= 0:
			p.y = 1
		elif p.y >= self.height - 1:
			p.y = self.height - 2
		gui.click(p.x, p.y)

	def performDoubleClick(self):
		p = gui.position()
		if p.x <= 0:
			p.x = 1
		elif p.x >= self.width - 1:
			p.x = self.width - 2
		if p.y <= 0:
			p.y = 1
		elif p.y >= self.height - 1:
			p.y = self.height - 2
		gui.doubleClick(p.x, p.y)

	def performRightClick(self):
		p = gui.position()
		if p.x <= 0:
			p.x = 1
		elif p.x >= self.width - 1:
			p.x = self.width - 2
		if p.y <= 0:
			p.y = 1
		elif p.y >= self.height - 1:
			p.y = self.height - 2
		gui.rightClick(p.x, p.y)

	# +ve scroll up, window go down
	# -ve scroll down, window go up
	def performScroll(self, unit):
		gui.scroll(unit)