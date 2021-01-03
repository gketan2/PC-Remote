#include <stdio.h>
#include <unistd.h>
#include <linux/input.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/time.h>

int main() {
  struct input_event event, event_end;

  int fd = open("/dev/input/event6", O_RDWR);
  if (fd < 0) {
    printf("Errro open mouse:\n");
    return -1;
  }
  memset(&event, 0, sizeof(event));
  memset(&event_end, 0, sizeof(event_end));
  gettimeofday(&event.time, NULL);
  event.type = EV_REL;
  event.code = REL_X;
  event.value = 100;
  gettimeofday(&event_end.time, NULL);
  event_end.type = EV_SYN;
  event_end.code = SYN_REPORT;
  event_end.value = 0;
  for (int i=0; i<5; i++) {
    write(fd, &event, sizeof(event));// Move the mouse
    sleep(1);
    write(fd, &event_end, sizeof(event_end));// Show move
    //sleep(0.5);// wait
  }
  close(fd);
  return 0;
}
