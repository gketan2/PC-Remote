from ServiceLocator import ServiceLocator

import socket

password = "password"
HEADERSIZE = 10
serviceLocator = ServiceLocator.getInstance()

#
# SERVER file should ONLY receive message and SEND IT TO OTHER CLASS(Service locator)
# mouse, keyboard should NOT be here
#
#

# receives data WITHOUT-HEADER
# send this data to servoce locator
def thread_processInput(data):
	#print(f"stripped received data:{data}")
	serviceLocator.execute(data)


# main function to start server
if __name__ == "__main__":

	host = "192.168.29.143"
	port = 5000

	# Creating socket variable

	#specifying what to use IPv4, TCP
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	#bind socket on this ip, port
	s.bind((host, port))
	#Queue length     ----Think it only handle one connection at a time
	#(any later connection are stored in queue)
	s.listen(0)

	isAuthenticated = False
	connectedSockets = []

	try:

		while True:
			#listening for connection
			clientsocket, address = s.accept()
			print(f"connection from {address}")
			connectedSockets.append(clientsocket)

			fullMessage = ""
			isNewMessage = True
			lengthToReceive = 1024

			while True:
				currentMessage = clientsocket.recv(lengthToReceive)
				#To CLOSE the connection, Empty-String is sent
				if len(currentMessage) == 0:
					isAuthenticated = False
					print(f'closed from {address}')
					connectedSockets.pop()
					break

				if isNewMessage:
					# length of string going to be received.
					# New message only receive 10bytes of data (10bytes = HEADER)
					msglen = int(currentMessage[:HEADERSIZE])
					lengthToReceive = msglen
					isNewMessage = False

				fullMessage += currentMessage.decode("utf-8")

				# will be true when whole message is received
				if len(fullMessage) - HEADERSIZE == msglen:
					fullMessage = fullMessage[HEADERSIZE:]

					#do work if authenticated
					if isAuthenticated:
						#do work with received message on other thread.
						#thread = threading.Thread(target=thread_processInput, args=(fullMessage,))
						#thread.start()
						serviceLocator.execute(fullMessage)

					# check if password is being set,
					# if yes then set the password.
					if not isAuthenticated:
						if len(fullMessage) > 9:
							if fullMessage[:9] == 'password=':
								if fullMessage[9:] == password:
									isAuthenticated = True

					#print("full message received")
					isNewMessage = True
					lengthToReceive = 10
					fullMessage = "" 

	except (KeyboardInterrupt, SystemExit):
		for connected in connectedSockets:
			connected.close()
		s.close()
