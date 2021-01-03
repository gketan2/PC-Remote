#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <signal.h>

#include <X11/Xlib.h>
#include <X11/extensions/XTest.h>

#include <time.h>
#include <errno.h>

#define DEFAULT_IP "192.168.29.143"
#define DEFAULT_PORT 8005
#define MAXLINE 1024

#define SERVICE_MOUSE 1
#define MOUSE_MOVE 11
#define MOUSE_LEFT_CLICK 12
#define MOUSE_RIGHT_CLICK 13
#define MOUSE_SCROLL_CLICK 14
#define MOUSE_SCROLL 15

#define SERVICE_KEYBOARD 2
#define KEYBOARD_TYPE_CHAR 21 //single key to be pressed
#define KEYBOARD_HOTKEY 22 //combination of keys will be pressed and released in opposite order.

int sockfd;
//200ms time-struct
struct timespec ts;
//display
Display *display;

void keyboard_interrupt(int dummy){
	int true = 1;
	printf("Keyboard Interrupt\n");
	//Close socket when ctrl-c is pressed.
	setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &true, sizeof(int));
	//exit the program.
	exit(0);
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

void move_pointer_by (int x, int y) {
	XWarpPointer(display, None, None, 0, 0, 0, 0, x, y);
	XFlush(display);
}

void click_mouse (int button) {
	//press
	XTestFakeButtonEvent(display, button, True, 0);
	XFlush(display);
	//release
	XTestFakeButtonEvent(display, button, False, 0);
	XFlush(display);
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
			click_mouse(1);
		} else if (sub_service == MOUSE_RIGHT_CLICK) {
			click_mouse(3);
		} else if (sub_service == MOUSE_SCROLL_CLICK) {
			click_mouse(2);
		} else if (sub_service == MOUSE_SCROLL) {
			//perform scroll
		}
	}

	//data will be based on
}

void server (char *ip, int port) {
	char *hello = "Hello";
	char *password = "password";
	struct sockaddr_in servaddr, cliaddr;

	// Creating socket fi

	//Return int value of char stle descriptor
	if ( (sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0 ) {
		perror("socket creation failed");
		exit(EXIT_FAILURE);
	}

	memset(&servaddr, 0, sizeof(servaddr));
	memset(&cliaddr, 0, sizeof(cliaddr));

	// Filling server information 
	servaddr.sin_family = AF_INET; // IPv4
	servaddr.sin_addr.s_addr = inet_addr(ip);
	servaddr.sin_port = htons(port);

	// Bind the socket with the server address
	if ( bind(sockfd, (const struct sockaddr *) &servaddr, sizeof(servaddr)) < 0 ) {
		perror("bind failed");
		exit(EXIT_FAILURE);
	}

	//handle keyboard interrupt
	signal(SIGINT, keyboard_interrupt);


	char buffer[MAXLINE];
	int len, n;
	//WHILE LOOP HERE
	while(1){
		len = sizeof(cliaddr);  //len is value/resuslt
		
		n = recvfrom(sockfd, (char *)buffer, MAXLINE, MSG_WAITALL, ( struct sockaddr *) &cliaddr, &len);
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
		// printf("Client : %s\n", buffer);
		sendto(sockfd, (const char *)hello, strlen(hello), MSG_CONFIRM, (const struct sockaddr *) &cliaddr, len);
		// printf("Hello message sent.\n");
	}
}

// Driver code
int main(int argc, char *argv[]) {

	//Initialising variables
	ts.tv_sec = 0;
	ts.tv_nsec = 200*1000000;
	display = XOpenDisplay(NULL);
	if(display == NULL){
		fprintf(stderr, "Display is null.\nCannot capture display to control :(\nExitting...");
		return -1;
	}

	// Start server with
	// input from command line(if provided).
	if(argc == 3){
		server(argv[1], atoi(argv[2]));
	} else {
		server(DEFAULT_IP, DEFAULT_PORT);
	}

	return 0;
}