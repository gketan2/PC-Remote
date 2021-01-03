#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> 
#include <string.h> 
#include <sys/types.h> 
#include <sys/socket.h> 
#include <arpa/inet.h> 
#include <netinet/in.h> 

#define DEFAULT_IP "192.168.29.143"
#define DEFAULT_PORT 8005
#define MAXLINE 1024

int server(char *ip, int port){
	int sockfd;
    char buffer[MAXLINE];
    char *hello = "Hello from client";
    struct sockaddr_in servaddr;

    // Creating socket file descriptor
    if ( (sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0 ) {
        perror("socket creation failed");
        exit(EXIT_FAILURE);
    }
  
    memset(&servaddr, 0, sizeof(servaddr));
      
    // Filling server information
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(port);
    servaddr.sin_addr.s_addr = inet_addr(ip);
      
    int n, len;
    
    char msg[20];
    while(1){
    scanf("%s", msg);
    sendto(sockfd, msg, strlen(msg), 
        MSG_CONFIRM, (const struct sockaddr *) &servaddr,  
            sizeof(servaddr)); 
    printf("%s", msg); 
          
    n = recvfrom(sockfd, (char *)buffer, MAXLINE,  
                MSG_WAITALL, (struct sockaddr *) &servaddr, 
                &len); 
    buffer[n] = '\0'; 
    printf("Server : %s\n", buffer);
    }
  
    close(sockfd); 
    return 0; 
}

int main (int argc, char *argv[]) {
	// start server
	if(argc == 3){
		server(argv[1], atoi(argv[2]));
	} else {
		server(DEFAULT_IP, DEFAULT_PORT);
	}
	return 0;
}