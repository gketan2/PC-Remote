#undef UNICODE

#define WIN32_LEAN_AND_MEAN
#define WIN32_EXTRA_LEAN

//cursor motion
#include <windows.h>
//mouse clicks
#include <winuser.h>

//sockets
#include <winsock2.h>
#include <Ws2tcpip.h>
#include <stdio.h>
#include <stdlib.h>

// Need to link with Ws2_32.lib
#pragma comment (lib, "Ws2_32.lib")
//#pragma comment (lib, "Mswsock.lib")

#define DEFAULT_IP "192.168.1.4"
#define DEFAULT_PORT 8005
#define MAXLINE 1024

#define SERVICE_MOUSE 1
#define MOUSE_MOVE 11
#define MOUSE_LEFT_CLICK 12
#define MOUSE_RIGHT_CLICK 13
#define MOUSE_SCROLL_CLICK 14
#define MOUSE_SCROLL_UP 15
#define MOUSE_SCROLL_DOWN 16
#define MOUSE_SCROLL_LEFT 17
#define MOUSE_SCROLL_RIGHT 18
#define MOUSE_FORWARD 19
#define MOUSE_BACK 20

#define SERVICE_KEYBOARD 2
#define KEYBOARD_TYPE_CHAR 21 //single key to be pressed
#define KEYBOARD_HOTKEY 22 //combination of keys will be pressed and released in opposite order.

//socket
SOCKET s;

BOOL WINAPI keyboard_interrupt(DWORD signal) {

    if (signal == CTRL_C_EVENT) {
    	closesocket(s);
    	WSACleanup();
    	exit(0); // do cleanup
    }

    return TRUE;
}

//Return int value of char starting at 'start' until the char is an integer.
int get_int(char data[], int *cursor, int length){
	int i;
	int result = 0;
	int found = 0;
	int temp;
	int multiplier = 1;
	for(i = *cursor; i < length; i++){
		temp = data[i] - '0';
		if(temp >= 0 && temp <= 9){
			found = 1;
			result *= 10;
			result += temp;
		} else {
			if(found == 1){
				break;
			} else {
				if(data[i] == '-'){
					multiplier = -1;
				}
			}
		}
	}
	*cursor = i;
	return result*multiplier;
}

INPUT input[1] = {};
void move_pointer_by (int x, int y) {
	input[0].mi.dx = x;
	input[0].mi.dy = y;
	SendInput(1, input, sizeof(INPUT));
}

void click_mouse_button(DWORD button[]) {
	INPUT input0[1] = {};
	input0[0].type = INPUT_MOUSE;
	input0[0].mi.dx = 0;
	input0[0].mi.dy = 0;
	input0[0].mi.mouseData = 0;
	input0[0].mi.dwFlags = button[0];
	input0[0].mi.time = 0;
	input0[0].mi.dwExtraInfo = 0;
	SendInput(1, input0, sizeof(INPUT));

	Sleep(10);

	INPUT input1[1] = {};
	input1[0].type = INPUT_MOUSE;
	input1[0].mi.dx = 0;
	input1[0].mi.dy = 0;
	input1[0].mi.mouseData = 0;
	input1[0].mi.dwFlags = button[1];
	input1[0].mi.time = 0;
	input1[0].mi.dwExtraInfo = 0;
	SendInput(1, input1, sizeof(INPUT));
}

void scroll_mouse(DWORD sfg, int direction) {
	INPUT input1[1] = {};
	input1[0].type = INPUT_MOUSE;
	input1[0].mi.dx = 0;
	input1[0].mi.dy = 0;
	input1[0].mi.mouseData = direction*100;
	input1[0].mi.dwFlags = sfg;
	input1[0].mi.time = 0;
	input1[0].mi.dwExtraInfo = 0;
	SendInput(1, input1, sizeof(INPUT));
}

void decode_array (char data[], int length) {

	int cursor = 0;

	int service = get_int(data, &cursor, length);

	int sub_service = get_int(data, &cursor, length);

	if (service == SERVICE_MOUSE) {
		if (sub_service == MOUSE_MOVE) {
			//move mouse
			int x = get_int(data, &cursor, length);
			int y = get_int(data, &cursor, length);
			move_pointer_by(x, y);
		} else if (sub_service == MOUSE_LEFT_CLICK) {
			DWORD inputs[2];
			inputs[0] = MOUSEEVENTF_LEFTDOWN;
			inputs[1] = MOUSEEVENTF_LEFTUP;
			click_mouse_button(inputs);
		} else if (sub_service == MOUSE_RIGHT_CLICK) {
			DWORD inputs[2];
			inputs[0] = MOUSEEVENTF_RIGHTDOWN;
			inputs[1] = MOUSEEVENTF_RIGHTUP;
			click_mouse_button(inputs);
		} else if (sub_service == MOUSE_SCROLL_CLICK) {
			DWORD inputs[2];
			inputs[0] = MOUSEEVENTF_MIDDLEDOWN;
			inputs[1] = MOUSEEVENTF_MIDDLEUP;
			click_mouse_button(inputs);
		} else if (sub_service == MOUSE_SCROLL_UP) {
			scroll_mouse(MOUSEEVENTF_WHEEL, 1);
		} else if (sub_service == MOUSE_SCROLL_DOWN) {
			scroll_mouse(MOUSEEVENTF_WHEEL, -1);
		} else if (sub_service == MOUSE_SCROLL_LEFT) {
			scroll_mouse(0x1000, -1);
		} else if (sub_service == MOUSE_SCROLL_RIGHT) {
			scroll_mouse(0x1000, 1);
		} else if (sub_service == MOUSE_FORWARD) {
			// DWORD inputs[2];
			// inputs[0] = WM_XBUTTONDOWN;
			// inputs[1] = WM_XBUTTONUP;
			// click_mouse_button(inputs);
		} else if (sub_service == MOUSE_BACK) {
			// DWORD inputs[2];
			// inputs[0] = MOUSEEVENTF_XDOWN;
			// inputs[1] = MOUSEEVENTF_XUP;
			// click_mouse_button(inputs);
		}
	}

	//data will be based on
}

void server (char *ip, int port) {

	// SOCKET s;
	struct sockaddr_in server, si_other;

	WSADATA wsa;
	int slen = sizeof(si_other);
	//Initialize winsock
	printf("\nInititalize Winsock..");
	if (WSAStartup(MAKEWORD(2,2), &wsa) != 0) {
		printf("\nFailed Error code: %d",WSAGetLastError());
		exit(EXIT_FAILURE);
	}
	printf("\nWinsock Initialize");

	//create socket
	printf("\nCreating Socket...");
	if ((s = socket(AF_INET, SOCK_DGRAM, 0)) == INVALID_SOCKET) {
		printf("\nSocket Failed: %d", WSAGetLastError());
		WSACleanup();
		exit(EXIT_FAILURE);
	}
	printf("\nSocekt Created");

	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr(ip);
	server.sin_port = htons(port);

	printf("\nBinding Socket...");
	if (bind(s, (struct sockaddr *)&server, sizeof(server)) == SOCKET_ERROR) {
		printf("\nSocket Bind Failed: %d", WSAGetLastError());
		closesocket(s);
		WSACleanup();
		exit(EXIT_FAILURE);
	}
	printf("\nSocket Bind Successful");

	//handle keyboard interrupt
	if (!SetConsoleCtrlHandler(keyboard_interrupt, TRUE)) {
		printf("Cannot set ctrl+c listener");
		exit(0);
	}
	// signal(SIGINT, keyboard_interrupt);


	char buffer[MAXLINE];
	int len, n;
	//WHILE LOOP HERE

	////MOUSE MOVEMENT INIT////////////////////
	input[0].type = INPUT_MOUSE;
	input[0].mi.mouseData = 0;
	input[0].mi.dwFlags = MOUSEEVENTF_MOVE;
	input[0].mi.time = 0;
	input[0].mi.dwExtraInfo = 0;
	//////////////////////////////////////////

	while(1){
		//clear buffer
		memset(buffer, '\0', MAXLINE);
		// len = sizeof(cliaddr);  //len is value/resuslt
		
		n = recvfrom(s, buffer, MAXLINE, 0, ( struct sockaddr *) &si_other, &slen);
		if(n == SOCKET_ERROR) {
			printf("recvfrom failed : %d", WSAGetLastError());
			exit(EXIT_FAILURE);
		}
		if(n < 0){
			//error occured;
		} else if(n == 0){
			//signal to close server.
			printf("closing server, n == 0");
			exit(0);
		} else {
			buffer[n] = '\0';
			decode_array(buffer, n);
		}
	}
}

// Driver code
int main(int argc, char *argv[]) {

	// Start server with
	// input from command line(if provided).
	if(argc == 3){
		server(argv[1], atoi(argv[2]));
	} else {
		server(DEFAULT_IP, DEFAULT_PORT);
	}

	return 0;
}
