import socket
import fcntl
import struct
import RPi.GPIO as GPIO
import threading
from threading import Thread
import thread

###################### SETUP #####################
#Name des remote.entity.Relais und Command fuer Socket --> i[0]=name&command | i[1]=output-pin | i[2]=input-pin | i[3]=on/off-Flag
relaisArray = [["Monitor Backlight",12,37,False],["remote.entity.Relais 2",16,35,False],["remote.entity.Relais 3",18,33,False],["remote.entity.Relais 4",22,31,False]]

######################## DO NOT EDIT ###################
# waehle GPIO.Pin modus
GPIO.setmode(GPIO.BOARD)

#global static variables
BAUNZTEIM = 200			#interrupt safetime

#global variables
control_server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
status_server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientCounter = 0
statusConnections = []

############################## INTERRUPTS ##################################
def Interrupt_BUTTONS(channel):
	for i in relaisArray:
		if(channel == i[2]):
			i[3] = not i[3]
			GPIO.output(i[1],i[3])

############################## THREADING ##################################
def control_thread():
	while(True):
		connection, addr = control_server_socket.accept()
		thread.start_new_thread(socket_thread, (connection, addr))

def socket_thread(connection, address):
	global clientCounter
	global statusConnections

	try:
		print("Connection established to "+str(address)+"!")
		clientCounter = clientCounter + 1;
		print("Connected clients: "+str(clientCounter))

		while True:
			buf = removeSeperator(connection.recv(1024))
			#print("\nReceived from client: " + buf)

			if(buf == 'info'):
				connection.send(createStatusString() + '\n')
			else:
				for i in relaisArray:
					if(buf == i[0]):
						i[3] = not i[3]
						GPIO.output(i[1],i[3])
						#if(i[3] == False):
						#	print("Disabled "+i[0])
						#else:
						#	print("Enabled "+i[0])
						break

				toSend = createStatusString()
				for i in statusConnections:
					try:
						i.send(toSend + '\n')
					except Exception:
						print "exception"
						statusConnections.remove(i)
	except Exception:
		connection.close()
		clientCounter = clientCounter-1
		print("Lost socket connection with "+str(address)+"!")
		print("Connected clients: "+str(clientCounter))


############################# HELP FUNCTIONS ###############################
def createStatusString():
	toSend = 0
	for i in relaisArray:
		if(toSend == 0):
			toSend = ""
			toSend = toSend + i[0] + "," + str(i[3])
		else:
			toSend = toSend + ";" + i[0] + "," + str(i[3])

	return toSend

def get_ip_address():
	s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	address = ""
	try:
		address = socket.inet_ntoa(fcntl.ioctl(s.fileno(), 0x8915, struct.pack('256s', "eth0"[:15]))[20:24])
		print("Connected by LAN")
	except Exception:
		try:
			address = socket.inet_ntoa(fcntl.ioctl(s.fileno(), 0x8915, struct.pack('256s', "wlan0"[:15]))[20:24])
			print("Connected by W-LAN")
		except Exception:
			print("Can't find IP-Address")
	return address

def removeSeperator(text):
	if "\r\n" in text:
		return text.replace("\r\n", "")
	else:
		if "\n" in text:
			return text.replace("\n", "")

################################## INIT ####################################
for i in relaisArray:
	#Pins als Ausgaenge definieren
	GPIO.setup(i[1],GPIO.OUT)
	GPIO.output(i[1],False)

	#Pins als Eingaenge definieren
	GPIO.setup(i[2],GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

	#Interrupts anmelden
	GPIO.add_event_detect(i[2], GPIO.RISING, callback = Interrupt_BUTTONS, bouncetime=BAUNZTEIM)


################################## MAIN ####################################
local_ip = get_ip_address()
print("Waiting for requests on: "+local_ip)

try:
	control_server_socket.bind((local_ip, 18745))
	control_server_socket.listen(5) # become a server socket, maximum 5 connections

	status_server_socket.bind((local_ip, 18744))
	status_server_socket.listen(5) # become a server socket, maximum 5 connections

	t1 = Thread(target=control_thread)
	t1.daemon = True
	t1.start()

	while(True):
		connection, addr = status_server_socket.accept()
		statusConnections.append(connection)

except KeyboardInterrupt,Exception:
	print("Resetting GPIO...")
	for i in relaisArray:
		GPIO.output(i[1],False)
	GPIO.cleanup()
