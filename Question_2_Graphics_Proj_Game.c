#include <stdlib.h>
#include <glut.h>
#include <stdio.h>
    /* 2 spheres are drawn at a time with a random number given to change the size of the sphere,speed and the postion in which it drops
  When the user clicks on the object or when it reaches the bottom of the screen the sphere repositions itself above the screen with a new position,speed and size
	Catching 10 objects will end the game */


GLfloat vertices[][3] = {{-2.0,-2.0,-2.0},{2.0,-2.0,-2.0},
       {2.0,2.0,-2.0}, {-2.0,2.0,-2.0}, {-2.0,-2.0,2.0},
       {2.0,-2.0,2.0}, {2.0,2.0,2.0}, {-2.0,2.0,2.0}};
GLsizei ww = 650, hh = 650;
GLfloat posy[] = {2.2,2.2}; // starting position of sphere
GLfloat rangex[] = {-1.4,1.4}; // controls where it falls from
GLfloat size[] = {0.4,0.3}; // controls the size of the sphere
GLfloat speed[] = {0.0004,0.0008}; // speed in which it falls
GLdouble highx = 1.8; // upper limit for x position
GLdouble lowx = -1.8; // lower limit for x position
GLdouble highsp = 0.5; // upper limit of size of sphere
GLdouble lowsp = 0.1; // lower limit of size of sphere
GLdouble low = 0.0002; // lower limit for speed of drop
GLdouble high = 0.0009;// upper limit for speed of drop
int count = 0; // number of objects caught
GLubyte image[225][151][3];

#define SIZE 512
void dropobject(void);
void display(void);
int i;
void drawcube()
{
	 glBegin(GL_POLYGON);
	 glColor3f(1.0,1.0,1.0);
		glTexCoord2f(0.0,0.0);
		glVertex3fv(vertices[0]);
		glTexCoord2f(0.0,1.0);
		glVertex3fv(vertices[3]);
		glTexCoord2f(1.0,1.0);
		glVertex3fv(vertices[2]);
		glTexCoord2f(1.0,0.0);
		glVertex3fv(vertices[1]);
	glEnd();

	glBegin(GL_POLYGON);
		glTexCoord2f(0.0,0.0);	
		glVertex3fv(vertices[2]);
		glTexCoord2f(0.0,1.0);
		glVertex3fv(vertices[3]);
		glTexCoord2f(1.0,1.0);
		glVertex3fv(vertices[7]);
		glTexCoord2f(1.0,0.0);
		glVertex3fv(vertices[6]);
	glEnd();

	glBegin(GL_POLYGON);
		glTexCoord2f(0.0,0.0);
		glVertex3fv(vertices[0]);
		glTexCoord2f(0.0,1.0);
		glVertex3fv(vertices[4]);
		glTexCoord2f(1.0,1.0);
		glVertex3fv(vertices[7]);
		glTexCoord2f(1.0,0.0);
		glVertex3fv(vertices[3]);
	glEnd();

	glBegin(GL_POLYGON);
		glTexCoord2f(0.0,0.0);
		glVertex3fv(vertices[1]);
		glTexCoord2f(0.0,1.0);
		glVertex3fv(vertices[2]);
		glTexCoord2f(1.0,1.0);
		glVertex3fv(vertices[6]);
		glTexCoord2f(1.0,0.0);
		glVertex3fv(vertices[5]);
	glEnd();

	glBegin(GL_POLYGON);
		glTexCoord2f(0.0,0.0);
		glVertex3fv(vertices[4]);
		glTexCoord2f(0.0,1.0);
		glVertex3fv(vertices[5]);
		glTexCoord2f(1.0,1.0);
		glVertex3fv(vertices[6]);
		glTexCoord2f(1.0,0.0);
		glVertex3fv(vertices[7]);
	glEnd();

	glBegin(GL_POLYGON);
		glTexCoord2f(0.0,0.0);
		glVertex3fv(vertices[0]);
		glTexCoord2f(0.0,1.0);
		glVertex3fv(vertices[1]);
		glTexCoord2f(1.0,1.0);
		glVertex3fv(vertices[5]);
		glTexCoord2f(1.0,0.0);
 		glVertex3fv(vertices[4]);
	glEnd();

	glFlush();
}
void init(void)
{
	glClearColor(0.0,0.0,0.0,0.0);
}
void update(int num) // gives a new speed,position and size for sphere when caught or reached bottom of screen
{
	posy[num]=2.2; // sets position of sphere above the screen
	rangex[num] = (rand() * (highx - lowx)) / RAND_MAX + lowx; // random double for x position of sphere
	size[num] = (rand() * (highsp - lowsp)) / RAND_MAX + lowsp; // random double for size of sphere
	speed[num] = (rand() * (high - low)) / RAND_MAX + low; // random double for speed of fall
}
void sphere(GLenum mode)
{
	glShadeModel(GL_SMOOTH);

	if(mode == GL_SELECT) glLoadName(1);
	glPushMatrix();
		glTranslatef(rangex[0],posy[0],0.0);
		glColor3f(1.0,0.0,1.0);
		glutSolidSphere(size[0],20,20);
	glPopMatrix();

	if(mode == GL_SELECT) glLoadName(2);
	glPushMatrix();
		glTranslatef(rangex[1],posy[1],0.0);
		glColor3f(1.0,0.0,1.0);
		glutSolidSphere(size[1],20,20);
	glPopMatrix();

}
void dropobject()
{
	if(posy[0] >= -2.0)
		posy[0]-= speed[0];
	else
		update(0);

	if(posy[1] >= -2.0)
		posy[1] -= speed[1];
	else
		update(1);

	glutPostRedisplay();
} 
void display(void)
{
	glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0);
	drawcube();
	sphere(GL_RENDER);
	dropobject();
	glFlush();
	glutSwapBuffers();
}
void processHits (GLint hits, GLuint buffer[])
{
   unsigned int i, j;
   GLuint names, *ptr;

   ptr = (GLuint *) buffer;
   for (i = 0; i < hits; i++)
   { /*  for each hit  */
	   names = *ptr;
	   ptr+=3;
	   count++;
	   printf("You have caught %d %s \n ",count," Sphere(s)");
       for (j = 0; j < names; j++)
       { /*  for each name */
         if(*ptr==1) 
		 {
			update(0);
		 }
		 else{
			 update(1);
		 }
      }
      printf ("\n");
   }
   if(count >= 10) 
   {
	   printf("Congratulations, you have caught ten spheres");
	   exit(0);
   }
}
void mouse(int button, int state, int x, int y)
{
   GLuint selectBuf[SIZE]; //declare selection buffer of size SIZE
   GLint hits;
   GLint viewport[4];

   if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN)
   {
   glGetIntegerv (GL_VIEWPORT, viewport); //save info about current viewport

   glSelectBuffer (SIZE, selectBuf); //initialize selection buffer
   glRenderMode(GL_SELECT); //set glRender mode to select

   glInitNames(); //initialize name stack
   glPushName(0); //must have a non-empty stack

   glMatrixMode (GL_PROJECTION); //set to projection mode
   glPushMatrix ();
   glLoadIdentity ();

   /* create 5x5 pixel picking region near cursor location */
   gluPickMatrix ((GLdouble) x, (GLdouble) (viewport[3] - y),
                  5.0, 5.0, viewport);
   gluOrtho2D (-2.0, 2.0, -2.0, 2.0);
   sphere(GL_SELECT); //call to draw objects with associated names


   glMatrixMode (GL_PROJECTION);
   glPopMatrix ();
   glFlush ();

   hits = glRenderMode (GL_RENDER); //get number of hits
   processHits (hits, selectBuf); //handle hits in function processHits

	glutPostRedisplay();
   }
}
void reshape(int w, int h)
{
   glViewport(0, 0, w, h);
   glMatrixMode(GL_PROJECTION);
   glLoadIdentity();
   gluOrtho2D (-2.0, 2.0, -2.0, 2.0);
   glMatrixMode(GL_MODELVIEW);
   glLoadIdentity();
}
void main(int argc, char **argv)
{
	FILE *fd;
	int  k, nm;
	char c;
	int i,j;
	char b[100];
	float s;
	int red, green, blue;

	printf("enter file name\n");
	scanf("%s", b);
	fd = fopen(b, "r");
	fscanf(fd,"%[^\n] ",b);
	if(b[0]!='P'|| b[1] != '3')
	{
		printf("%s is not a PPM file!\n", b); 
		exit(0);
	}
	printf("%s is a PPM file\n",b);
	fscanf(fd, "%c",&c);
	while(c == '#') 
	{
		fscanf(fd, "%[^\n] ", b);
		printf("%s\n",b);
		fscanf(fd, "%c",&c);
	}
	ungetc(c,fd); 
	fscanf(fd, "%d %d %d", &ww, &hh, &k);

	printf("%d rows  %d columns  max value= %d\n",ww,hh,k);

		for(i=0;i<ww;i++) for(j=0;j<hh;j++)
	{
		fscanf(fd,"%d %d %d",&red, &green, &blue );
		image[i][j][0]=red;
		image[i][j][1]=green;
		image[i][j][2]=blue;
	}

	printf("read image\n");


	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowSize(650,650);
	glutInitWindowPosition (100, 100);
	glutCreateWindow("Game");
	init();
	glutReshapeFunc (reshape);
	glutDisplayFunc(display);
	glutMouseFunc (mouse);

	glPixelStorei(GL_UNPACK_ALIGNMENT,1);
	glEnable(GL_TEXTURE_2D);
	glTexImage2D(GL_TEXTURE_2D,0,3,ww,hh,0,GL_RGB,GL_UNSIGNED_BYTE, image);
	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
	glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);

	glutMainLoop();
}