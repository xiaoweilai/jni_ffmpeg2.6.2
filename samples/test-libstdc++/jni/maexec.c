#include "stdio.h"
#include "stdlib.h"

int main(int argc, char *argv[]) 
{
    fclose(fopen("mafeng", "w"));
    return 0;
}
