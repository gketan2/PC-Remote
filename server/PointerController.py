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
		gui.moveTo(x, y, duration=0.25)

	# -ve x = move left
	# -ve y = move up
	# Should be ratio-handeled length
	def movePointerBy(self, x, y):
		if x < 0 or x >= self.width:
			return
		if y < 0 or y >= self.height:
			return
		gui.move(x, y, duration=0.25)

	def performClick(self):
		p = gui.position()
		if p.x <= 0:
			p.x = p.x + 1
		elif p.x >= self.width - 1:
			p.x = p.x - 1
		if p.y <= 0:
			p.y = p.y + 1
		elif p.y >= self.height - 1:
			p.y = p.y - 1
		gui.click(p.x, p.y)

	def performDoubleClick():
		p = gui.position()
		if p.x <= 0 or p.x >= self.width - 1:
			return
		if p.y <= 0 or p.y >= self.height - 1:
			return
		gui.doubleClick(p.x, p.y)

	def performRightClick():
		p = gui.position()
		if p.x <= 0 or p.x >= self.width - 1:
			return
		if p.y <= 0 or p.y >= self.height - 1:
			return
		gui.rightClick(p.x, p.y)

	# +ve scroll up, window go down
	# -ve scroll down, window go up
	def performScroll(unit):
		gui.scroll(unit)


# controller = PointerController.getInstance()
# print(controller.getScreenHeight())
# print(controller.getScreenWidth())
# controller.movePointerTo(0, 0)
# controller.performClick()
# cont = PointerController.getInstance()
# print(controller)
# print(cont)