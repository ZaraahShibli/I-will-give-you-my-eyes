package org.yourorghere;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * Project1.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Project1 implements GLEventListener {

    //*********************************************************
    int counter = 0;
    int Time = 0;
    Texture GrayBackground, Library, Text, Floor,
            Keyboard, Screen, WallPink, Carpets,
            Paper, Table, Coffee, Streat, Sky;

    int Right = 0, Left = 1, BlindHand = 1, WordNumber = 1;
    float MoveBraille = 0;
    float TitleSize = 1, Scene1Size = 1, Scene2Size = 1, EndText = -1.5f, SSize = 1;
    float Setp = 8;
    int dMove = 600;
    TextRenderer text;

    //****************************************************************
    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Project1());
        frame.add(canvas);
        frame.setSize(1500, 1000);
        frame.setResizable(false);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        gl.glEnable(GL.GL_TEXTURE_2D);
        try {
            GrayBackground = TextureIO.newTexture(new File("Gray Background.jpg"), true);
            Library = TextureIO.newTexture(new File("Library.jpg"), true);
            Floor = TextureIO.newTexture(new File("Floor.jpg"), true);
            Keyboard = TextureIO.newTexture(new File("keyboard.png"), true);
            Screen = TextureIO.newTexture(new File("screen.png"), true);
            WallPink = TextureIO.newTexture(new File("wallPink.jpg"), true);
            Carpets = TextureIO.newTexture(new File("Carpets.jpg"), true);
            Table = TextureIO.newTexture(new File("Table.jpg"), true);
            Coffee = TextureIO.newTexture(new File("Coffee.png"), true);
            Paper = TextureIO.newTexture(new File("Paper.png"), true);
            Streat = TextureIO.newTexture(new File("Streat.jpg"), true);
            Sky = TextureIO.newTexture(new File("sky1.png"), true);

        } catch (IOException ex) {
            System.err.print(ex);
        }

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        //////////////////////////////////////////
        if (Time < 250) {
            TitleSize += 0.001;
            Title(drawable, gl);
        } else if (Time < 500) {
            Scene1Size += 0.001;
            Scene1(drawable, gl);
        } else if (Time < 750) {
            Room(gl, glut, glu);
        } else if (Time < 1000) {
            Typing(gl, glut);
        } else if (Time < 1250) {
            Braille(gl, glut);
        } else if (Time < 1500) {
            Scene2Size += 0.001;
            Scene2(drawable, gl);
        } else if (Time < 1750) {
            Man(gl, glut);
        } else if (Time < 2250) {
            
            BlindLibrary(drawable, gl);
            if (dMove > 0) {
                Setp -= 0.01;
                dMove--;
                gl.glTranslated(Setp, -1.5f, -3f);
            } else {

                gl.glTranslated(0, -1.5f, -3f);
            }
            BlindMan(glut, gl);

        } else if (Time < 2700) {
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            BookLibrary(gl);
        } else if (Time < 2900) {
            StillGivingSomething(drawable, gl);
            SSize += 0.0001;
        } else {
            TheEnd(drawable, gl);
            EndText += 0.5;
        }

        Time++;
        //---------------------------------------------------
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    ///////////////////////////////////////////////////////////////////////////
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    ///////////////////////////////////////////////////////////////////////////
    void Title(GLAutoDrawable drawable, GL gl) {
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        text = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 80));
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(0.568f, 0.188f, 0.317f, 1f);
        text.draw3D("I will give you my eyes", 200, 500, 0, TitleSize);
        text.endRendering();
    }

    ///////////////////////////////////////////////////////////////////////////
    void Scene1(GLAutoDrawable drawable, GL gl) {
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        text = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 80));
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(0.568f, 0.188f, 0.317f, 1f);
        text.draw3D("Scene (1)", 500, 500, 0, Scene1Size);
        text.endRendering();
    }

    ///////////////////////////////////////////////////////////////////////////
    void Scene2(GLAutoDrawable drawable, GL gl) {
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        text = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 80));
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(0.568f, 0.188f, 0.317f, 1f);
        text.draw3D("Scene (2)", 500, 500, 0, Scene2Size);
        text.endRendering();
    }

    ///////////////////////////////////////////////////////////////////////////
    void StillGivingSomething(GLAutoDrawable drawable, GL gl) {
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        text = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 60));
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(0.568f, 0.188f, 0.317f, 1f);
        text.draw3D("You are not loosing anything", 300, 500, 0, SSize);
        text.draw3D("    still giving something", 300, 400, 0, SSize);
        text.endRendering();
    }

    void TheEnd(GLAutoDrawable drawable, GL gl) {
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        text = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 40));
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(0.568f, 0.188f, 0.317f, 1f);
        text.draw3D("Computer Graphics Project", 500, EndText, 0, 1);
        text.draw3D("         CPCS-391        ", 500, (EndText - 50), 0, 1);
        text.draw3D("                         ", 500, (EndText - 100), 0, 1);
        text.draw3D("        Instructors       ", 500, (EndText - 150), 0, 1);
        text.draw3D("    Dr.Salma kammoun    ", 500, (EndText - 200), 0, 1);
        text.draw3D("        I.Mai Nassier      ", 500, (EndText - 250), 0, 1);
        text.draw3D("                         ", 500, (EndText - 300), 0, 1);
        text.draw3D("       Team members       ", 500, (EndText - 350), 0, 1);
        text.draw3D("      Afnan Balghaith     ", 500, (EndText - 400), 0, 1);
        text.draw3D("      Walaa Almarzougi    ", 500, (EndText - 450), 0, 1);
        text.draw3D("        Zaraah Shibli      ", 500, (EndText - 500), 0, 1);
        text.draw3D("                         ", 500, (EndText - 550), 0, 1);
        text.draw3D("         Spring 2018       ", 500, (EndText - 600), 0, 1);
        text.endRendering();
    }

    /////////////////////////////////////////////////////////////////////////
    void Room(GL gl, GLUT glut, GLU glu) {
        gl.glClearColor(0.568f, 0.188f, 0.317f, 0.0f);
        gl.glTranslatef(0f, 0f, -1.2f);
        //************************************************
        // Room
        // Floor
        gl.glColor3f(1, 1, 1);
        gl.glEnable(GL.GL_TEXTURE_2D);
        Floor.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(1f, 1f, 1f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.6f, -0.5f, 0.6f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-0.6f, -0.5f, 0.6f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);

        //Ceiling
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(1, 1, 1);
        gl.glColor3f(0.894f, 0.764f, 0.803f);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glVertex3f(0.6f, 0.5f, 0.6f);
        gl.glVertex3f(-0.6f, 0.5f, 0.6f);
        gl.glEnd();

        // Right wall 
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.568f, 0.188f, 0.317f);
        gl.glVertex3f(0.6f, 0.5f, 0.6f);
        gl.glVertex3f(0.6f, -0.5f, 0.6f);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glEnd();

        // Left wall 
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(-0.6f, 0.5f, 0.6f);
        gl.glVertex3f(-0.6f, -0.5f, 0.6f);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glEnd();

        // Front wall (Background)
        gl.glColor3f(1, 1, 1);
        gl.glEnable(GL.GL_TEXTURE_2D);
        WallPink.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glRotatef(1, 1, 1, 1);
        gl.glColor3f(1f, 1f, 1f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);

        //************************************************
        // Table 
        gl.glTranslatef(0f, 0f, -0.6f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.301f, 0.141f, 0.039f);
        gl.glVertex3f(0, -.3f, -0.6f);
        gl.glVertex3f(0.7f, -0.3f, -0.6f);
        gl.glVertex3f(0.7f, -0.4f, -0.6f);
        gl.glVertex3f(0, -0.4f, -0.6f);
        gl.glEnd();
        //*****************************
        gl.glTranslatef(0f, -0.8f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.301f, 0.141f, 0.039f);
        gl.glVertex3f(0, 0, -0.7f);
        gl.glVertex3f(0.1f, 0.0f, -0.7f);
        gl.glVertex3f(0.1f, 0.4f, -0.7f);
        gl.glVertex3f(0, 0.4f, -0.7f);
        gl.glEnd();
        //*****************************
        gl.glTranslatef(0.9f, -0.2f, -0.8f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.301f, 0.141f, 0.039f);
        gl.glVertex3f(0, 0, -0.51f);
        gl.glVertex3f(0.12f, 0.0f, -0.51f);
        gl.glVertex3f(0.12f, 0.61f, -0.51f);
        gl.glVertex3f(0, 0.61f, -0.51f);
        gl.glEnd();

        //*********************************************
        // Labtop 
        gl.glColor3f(1, 1, 1);
        gl.glTranslatef(0.11f, 0.4f, -6f);

        // Keyboard
        gl.glEnable(GL.GL_TEXTURE_2D);
        Keyboard.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(1.0f, 0.0f, 1.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.6f, -0.4f, 0.6f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-0.6f, -0.4f, 0.6f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);

        // Screen
        gl.glEnable(GL.GL_TEXTURE_2D);
        Screen.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(1f, 1.f, 1.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);

        //****************************************************
        // Man + Chair 
        //*******************************
        // Face
        gl.glTranslatef(1.2f, 0f, -4f);
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        int numPoints = 10000;
        float Radius = 1f;
        gl.glTranslatef(0.90f, -0.5f, -4f);
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (2.0 * Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle) * Radius + 0.5;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();

        //*******************************
        // Neck
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glTranslatef(0.94f, -0.5f, -4f);
        gl.glVertex3f(-0.5f, -0.4f, 0.0f);
        gl.glVertex3f(0.5f, 0.5f, 0.0f);
        gl.glVertex3f(0.5f, -0.5f, 0.0f);
        gl.glVertex3f(-0.5f, -0.5f, 0.0f);
        gl.glEnd();

        //*******************************
        // Cap 
        float numPoint = 10000;
        float Radiu = 1f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoint; i++) {
            gl.glColor3f(0f, 0.25f, 0.50f);
            double Angle = i * (Math.PI / numPoint);
            double X = Math.cos(Angle) * Radiu;
            double Y = Math.sin(Angle) * Radiu;
            gl.glVertex2d(X, Y + 0.5);
        }
        gl.glEnd();

        //*******************************
        //Back
        gl.glTranslatef(0f, -0.7f, 0f);
        gl.glColor3f(0.301f, 0.141f, 0.039f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(-1.3f, 0.25f, 0.0f);
        gl.glVertex3f(1.3f, 0.25f, 0.0f);
        gl.glVertex3f(1.3f, -2f, 0.0f);
        gl.glVertex3f(-1.3f, -2f, 0.0f);
        gl.glEnd();

        //*******************************
        // Leg - Left
        gl.glColor3f(0, 0.25f, 0.50f);
        gl.glTranslatef(0.5f, -3.5f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(0f, 1.5f, 0.0f);
        gl.glVertex3f(0.5f, 1.5f, 0.0f);
        gl.glVertex3f(0.5f, 0.5f, 0.0f);
        gl.glVertex3f(0f, 0.5f, 0.0f);
        gl.glEnd();

        //*******************************
        // Leg - Right
        gl.glColor3f(0, 0.25f, 0.50f);
        gl.glTranslatef(-1.5f, 0f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(0f, 1.5f, 0.0f);
        gl.glVertex3f(0.5f, 1.5f, 0.0f);
        gl.glVertex3f(0.5f, 0.5f, 0.0f);
        gl.glVertex3f(0f, 0.5f, 0.0f);
        gl.glEnd();

        //*******************************
        // Shoes - Right
        gl.glTranslatef(0f, 0.1f, 0f);
        gl.glColor3f(0.50f, 0.50f, 0.50f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(0f, 0.4f, 0.0f);
        gl.glVertex3f(0.5f, 0.4f, 0.0f);
        gl.glVertex3f(0.5f, 0f, 0.0f);
        gl.glVertex3f(0f, 0f, 0.0f);
        gl.glEnd();

        //*******************************
        // Shoes - Left
        gl.glTranslatef(1.5f, 0.f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(0f, 0.4f, 0.0f);
        gl.glVertex3f(0.5f, 0.4f, 0.0f);
        gl.glVertex3f(0.5f, 0f, 0.0f);
        gl.glVertex3f(0f, 0f, 0.0f);
        gl.glEnd();

        //***************************************************
        // Chair 
        gl.glTranslatef(0.5f, 1f, -5f);
        gl.glTranslatef(-.5f, -.5f, -.00000009f);
        gl.glBegin(gl.GL_QUADS);
        gl.glColor3f(0.301f, 0.141f, 0.039f);

        //*******************************
        //Front  
        gl.glTranslatef(0f, 0f, 0f);
        gl.glVertex3f(-2.0f, -0.2f, 2.0f);
        gl.glVertex3f(2.0f, -0.2f, 2.0f);
        gl.glVertex3f(2.0f, 0.2f, 2.0f);
        gl.glVertex3f(-2.0f, 0.2f, 2.0f);

        //*******************************
        //Back
        gl.glVertex3f(-2.0f, -0.2f, -2.0f);
        gl.glVertex3f(-2.0f, 0.2f, -2.0f);
        gl.glVertex3f(2.0f, 0.2f, -2.0f);
        gl.glVertex3f(2.0f, -0.2f, -2.0f);

        //*******************************
        //Left
        gl.glVertex3f(-2.0f, -0.2f, -2.0f);
        gl.glVertex3f(-2.0f, -0.2f, 2.0f);
        gl.glVertex3f(-2.0f, 0.2f, 2.0f);
        gl.glVertex3f(-2.0f, 0.2f, -2.0f);

        //*******************************
        //Top
        gl.glVertex3f(2.0f, 0.2f, 2.0f);
        gl.glVertex3f(-2.0f, 0.2f, 2.0f);
        gl.glVertex3f(-2.0f, 0.2f, -2.0f);
        gl.glVertex3f(2.0f, 0.2f, -2.0f);

        //*******************************
        //Bottom
        gl.glVertex3f(2.0f, -0.2f, 2.0f);
        gl.glVertex3f(-2.0f, -0.2f, 2.0f);
        gl.glVertex3f(-2.0f, -0.2f, -2.0f);
        gl.glVertex3f(2.0f, -0.2f, -2.0f);

        //*******************************
        //Table front leg
        //front
        gl.glVertex3f(1.8f, -0.2f, 1.6f);
        gl.glVertex3f(1.4f, -0.2f, 1.6f);
        gl.glVertex3f(1.4f, -2.0f, 1.6f);
        gl.glVertex3f(1.8f, -2.0f, 1.6f);

        //*******************************
        //back leg back
        //front
        gl.glVertex3f(1.8f, -0.2f, -1.2f);
        gl.glVertex3f(1.4f, -0.2f, -1.2f);
        gl.glVertex3f(1.4f, -2.0f, -1.2f);
        gl.glVertex3f(1.8f, -2.0f, -1.2f);

        //*******************************
        //leg left front
        gl.glVertex3f(-1.8f, -0.2f, 1.6f);
        gl.glVertex3f(-1.4f, -0.2f, 1.6f);
        gl.glVertex3f(-1.4f, -2.0f, 1.6f);
        gl.glVertex3f(-1.8f, -2.0f, 1.6f);

        //*******************************
        //left leg back front
        //front
        gl.glVertex3f(-1.8f, -0.2f, -1.2f);
        gl.glVertex3f(-1.4f, -0.2f, -1.2f);
        gl.glVertex3f(-1.4f, -2.0f, -1.2f);
        gl.glVertex3f(-1.8f, -2.0f, -1.2f);
        gl.glEnd();

        //************************************************
        // Painting
        // Outer frame  
        gl.glTranslatef(-8f, 7f, 0);
        gl.glColor3f(0.568f, 0.188f, 0.317f);
        glut.glutSolidSphere(2, 30, 16);
        // Internal frame
        gl.glTranslatef(0f, 0f, -0.20f);
        gl.glColor3f(0.99f, 94f, 0.97f);
        glut.glutSolidSphere(2, 30, 16);
        //internal-small one
        gl.glTranslatef(0f, 0f, -0.10f);
        gl.glColor3f(0.921f, 0.819f, 0.854f);
        glut.glutSolidSphere(2, 30, 16);

        //**************************************
        // Carpets 
        gl.glTranslatef(3.1f, -7f, 10f);
        gl.glColor3f(1f, 1f, 1f);
        numPoints = 100000;
        Radius = 1.7f;
        gl.glRotatef(5, 1, 1, 1);
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (2.0 * Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle) * Radius;
            double z = Math.sin(Angle + 0.05) * Radius;
            gl.glVertex3d(x, y, z);
        }
        gl.glEnd();
        gl.glRotatef(-5, 1, 1, 1);

        //*******************************
        // Set image on carpets
        numPoints = 1000000;
        Radius = 1f;
        gl.glEnable(GL.GL_TEXTURE_2D);
        Carpets.bind();
        gl.glTranslatef(0.1f, 0.8f, 2f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(1f, 1.f, 1.0f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);

        // Set color as white 
        gl.glColor3f(1, 1, 1);

    }

    ////////////////////////////////////////////////////////////////////
    public void Man(GL gl, GLUT glut) {

        //*********************************************************
        // Background 
        gl.glPushMatrix();
        gl.glTranslatef(-5f, -2.5f, -6f);
        gl.glScalef(10f, 5f, 1f);
        GrayBackground.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(0, 0, 0);
        gl.glPopMatrix();

        //*******************************************************
        //T-shirt 
        gl.glTranslatef(-1f, -2.2f, -2f);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(0.250f, 0.011f, 0.019f);
        gl.glVertex2f(2.0f, 1.f);
        gl.glVertex2f(1.f, 2.0f);
        gl.glVertex2f(0.0f, 1.f);
        gl.glEnd();

        gl.glTranslatef(0.76f, 1f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.250f, 0.011f, 0.019f);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(0.5f, 0);
        gl.glVertex2f(0.5f, 1f);
        gl.glVertex2f(0, 1f);
        gl.glEnd();

        //***********************************************************
        // Add light for draw face 
        gl.glTranslatef(0.28f, 0.6f, -5.5f);

        gl.glPushMatrix();
        float[] ambiColor = {0.894f, 0.796f, 0.686f, 0f};
        float[] deffColor = {0.894f, 0.796f, 0.686f, 0f};
        float[] shininess = {500.0f};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, ambiColor, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, deffColor, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, shininess, 0);
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_LIGHTING);
        gl.glEnable(gl.GL_LIGHT0);
        gl.glDisable(gl.GL_CULL_FACE);

        // Face
        gl.glTranslatef(-0.03f, 1.1f, 4.7f);
        glut.glutSolidSphere(0.9, 100, 100);
        gl.glDisable(gl.GL_DEPTH_TEST);
        gl.glDisable(gl.GL_LIGHTING);
        gl.glDisable(gl.GL_LIGHT0);
        gl.glPopMatrix();

        //************************************************************
        // Mouth
        gl.glTranslatef(-0.03f, 1.1f, 4.7f);
        gl.glTranslatef(0.2f, -0.5f, -9f);
        gl.glColor3f(0.443f, 0.074f, 0.090f);
        gl.glPushMatrix();

        // Moving 
        if (counter % 15 == 0) {
            gl.glScalef(1.2f, 1f, 3.5f);
        } else {
            gl.glScalef(1.2f, 0.9f, 3.5f);
        }
        counter++;
        glut.glutSolidSphere(0.8, 100, 100);
        gl.glPopMatrix();

        //*******************************************************
        // Mic 
        gl.glTranslatef(-5f, 0f, -6f);

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0, 0, 0);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(4f, 0);
        gl.glVertex2f(4f, 0.2f);
        gl.glVertex2f(0, 0.2f);
        gl.glEnd();
        //*************************************
        gl.glPushMatrix();
        gl.glTranslatef(-1.7f, 1.31f, 0f);
        gl.glRotatef(-64, 1, 1, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0, 0, 0);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(4f, 0);
        gl.glVertex2f(4f, 0.15f);
        gl.glVertex2f(0, 0.15f);
        gl.glEnd();
        //*************************************
        gl.glPopMatrix();
        gl.glTranslatef(4f, 0.2f, -2f);
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        int numPoints = 1000000;
        float Radius = 0.7f;

        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (2.0 * Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle) * Radius;
            gl.glVertex2d(x, y);

        }
        gl.glEnd();

        //**********************************************************
        // Glasses
        gl.glTranslatef(-6f, 5f, 1f);
        //Glasses frame 
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.270f, 0.058f, 0.027f);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(13f, 0);
        gl.glVertex2f(13f, 0.2f);
        gl.glVertex2f(0, 0.2f);
        gl.glEnd();
        //***********************************
        // First Lens glasses - Left
        gl.glTranslatef(1.8f, -1.1f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.835f, 0.901f, 0.945f);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(4f, 0);
        gl.glVertex2f(4f, 2.5f);
        gl.glVertex2f(0, 2.5f);
        gl.glEnd();
        gl.glColor3f(1f, 1f, 1f);
        gl.glPushMatrix();

        //***********************************
        // Left eyes
        gl.glTranslatef(2f, 0.9f, 1f);
        glut.glutSolidSphere(1, 100, 100);
        gl.glTranslatef(-0.4f, 0.4f, -2.9f);
        gl.glColor3f(0.7f, 0.7f, 0.7f);
        glut.glutSolidSphere(1, 100, 100);
        //***************************************
        // Enable blend
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA,
                GL.GL_ONE_MINUS_SRC_ALPHA);
        //***************************************
        //  Secound lens glasses - Left
        gl.glTranslatef(-1.6f, -1.3f, 1.9f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor4f(0.890f, 0.937f, 0.972f, 0.6f);
        gl.glVertex2f(0, 0);
        gl.glColor4f(0.486f, 0.733f, 0.894f, 0.6f);
        gl.glVertex2f(4f, 0);
        gl.glColor4f(0.890f, 0.937f, 0.972f, 0.6f);
        gl.glVertex2f(4f, 2.5f);
        gl.glColor4f(0.486f, 0.733f, 0.894f, 0.6f);
        gl.glVertex2f(0, 2.5f);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
        gl.glPopMatrix();

        //*******************************************
        // First Lens glasses - Right
        gl.glTranslatef(5.4f, 0f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.835f, 0.901f, 0.945f);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(4f, 0);
        gl.glVertex2f(4f, 2.5f);
        gl.glVertex2f(0, 2.5f);
        gl.glEnd();

        //*******************************************
        // Right eyes
        gl.glPushMatrix();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef(2f, 0.9f, 1f);
        glut.glutSolidSphere(1, 100, 100);
        gl.glTranslatef(0.2f, 0.4f, -2.9f);
        gl.glColor3f(0.7f, 0.7f, 0.7f);
        glut.glutSolidSphere(1, 100, 100);

        //********************************************
        // Enable blend 
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA,
                GL.GL_ONE_MINUS_SRC_ALPHA);
        //***************************************
        // Secound lens glasses - Right
        gl.glTranslatef(-2.2f, -1.3f, 1.9f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor4f(0.890f, 0.937f, 0.972f, 1f);
        gl.glVertex2f(0, 0);
        gl.glColor4f(0.486f, 0.733f, 0.894f, 0.6f);
        gl.glVertex2f(4f, 0);
        gl.glColor4f(0.890f, 0.937f, 0.972f, 1f);
        gl.glVertex2f(4f, 2.5f);
        gl.glColor4f(0.486f, 0.733f, 0.894f, 0.6f);
        gl.glVertex2f(0, 2.5f);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
        gl.glPopMatrix();

        //**********************************************************
        // Headphones 
        // Left 
        gl.glColor3f(0, 0, 0);
        gl.glTranslatef(-4.4f, -1.8f, 8f);
        gl.glPushMatrix();
        gl.glScalef(1f, 3f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        //****************************************
        gl.glPushMatrix();
        gl.glTranslatef(-0.1f, 0.5f, 0f);
        gl.glColor3f(0, 0, 0);
        gl.glRotatef(85, 1, 1, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(5f, 0);
        gl.glVertex2f(5f, 0.4f);
        gl.glVertex2f(0, 0.4f);
        gl.glEnd();
        gl.glPopMatrix();

        //****************************************
        gl.glColor3f(0.450f, 0.047f, 0.058f);
        gl.glTranslatef(-0.69f, 0.4f, -2f);
        gl.glPushMatrix();
        gl.glScalef(1f, 3f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //****************************************
        // Right 
        gl.glColor3f(0, 0, 0);
        gl.glTranslatef(8.7f, -0.4f, 2f);
        gl.glPushMatrix();
        gl.glScalef(1f, 3f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //*****************************************
        gl.glPushMatrix();
        gl.glTranslatef(0.1f, 0.5f, 0f);
        gl.glColor3f(0, 0, 0);
        gl.glRotatef(85, 1, 1, 1);
        gl.glRotatef(85, 1, 1, 0);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(5f, 0);
        gl.glVertex2f(5f, 0.3f);
        gl.glVertex2f(0, 0.3f);
        gl.glEnd();
        gl.glPopMatrix();
        //****************************************
        gl.glColor3f(0.450f, 0.047f, 0.058f);
        gl.glTranslatef(+0.69f, 0.4f, -2f);
        gl.glPushMatrix();
        gl.glScalef(1f, 3f, 0f);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        //****************************************
        // Set color white al the end 
        gl.glColor3f(1, 1, 1);

    }
/////////////////////////////////////////////////////////////////////////////

    public void BookLibrary(GL gl) {
        gl.glClearColor(0.188f, 0.117f, 0.031f, 0.0f);
        gl.glTranslatef(0f, 0f, -1.2f);
        gl.glBegin(GL.GL_QUADS);

        //*********************************************************
        // Floor
        gl.glColor3f(0.803f, 0.803f, 0.803f);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glVertex3f(0.6f, -0.5f, 0.6f);
        gl.glVertex3f(-0.6f, -0.5f, 0.6f);

        //*********************************************************
        // Ceiling
        gl.glColor3f(0.721f, 0.690f, 0.6f);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glVertex3f(0.6f, 0.5f, 0.6f);
        gl.glVertex3f(-0.6f, 0.5f, 0.6f);

        //*********************************************************
        // Right wall 
        gl.glColor3f(0.188f, 0.117f, 0.031f);
        gl.glVertex3f(0.6f, 0.5f, 0.6f);
        gl.glVertex3f(0.6f, -0.5f, 0.6f);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);

        //*********************************************************
        // Left wall 
        gl.glColor3f(0.188f, 0.117f, 0.031f);
        gl.glVertex3f(-0.6f, 0.5f, 0.6f);
        gl.glVertex3f(-0.6f, -0.5f, 0.6f);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);

        //*********************************************************
        // Background
        gl.glEnd();
        gl.glEnable(GL.GL_TEXTURE_2D);
        Library.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glRotatef(1, 1, 1, 1);
        gl.glColor3f(1f, 1f, 1f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(0.6f, 0.5f, -0.6f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-0.6f, -0.5f, -0.6f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-0.6f, 0.5f, -0.6f);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);

        //*********************************************************
        // Table       
        gl.glTranslatef(0f, -1.5f, -5f);
        gl.glColor3f(0.294f, 0.227f, 0.149f);
        gl.glRectf(0, -2f, 2f, -0.7f);

        //*********************************************************
        // Face
        gl.glTranslatef(2.8f, -3f, -12f);
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        int numPoints = 10000;
        float Radius = 1f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (2.0 * Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle) * Radius + 0.5;
            //double z = Math.sin(Angle + 0.05) * Radius+1;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();

        //***********************************************************
        // Mouth 
        gl.glColor3f(0.537f, 0.082f, 0.145f);
        numPoints = 100;
        Radius = -0.2f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (2.0 * Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle + 0.2) * Radius;
            double z = Math.sin(Angle + 50) * Radius;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();

        //**************************************************************
        // T-Shirt
        gl.glTranslatef(0f, -2f, 0f);
        float numPoint = 100;
        float Radiu = 1.5f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoint; i++) {
            gl.glColor3f(0.513f, 0.678f, 0.745f);
            double Angle = i * (Math.PI / numPoint);
            double X = Math.cos(Angle) * Radiu;
            double Y = Math.sin(Angle) * Radiu;
            gl.glVertex2d(X, Y);
        }
        gl.glEnd();

        //************************************************************
        // Glass
        gl.glTranslatef(-0.5f, 3.5f, 4f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0, 0, 0);
        gl.glVertex2f(0f, 0f);
        gl.glVertex2f(0.5f, 0);
        gl.glVertex2f(0.5f, 0.3f);
        gl.glVertex2f(0, 0.3f);
        gl.glEnd();
        //*******************************
        gl.glRectf(-0.2f, 0.1f, 0f, 0.2f);
        //*******************************
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0, 0, 0);
        gl.glVertex2f(-0.7f, 0f);
        gl.glVertex2f(-0.2f, 0);
        gl.glVertex2f(-0.2f, 0.3f);
        gl.glVertex2f(-0.7f, 0.3f);
        gl.glEnd();

        //////////////////////////////////////////////////////////
        // Heads
        gl.glTranslatef(-0.06f, 0.5f, 3f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.035f, 0.215f, 0.243f);
        gl.glVertex2f(0f, 0f);
        gl.glVertex2f(0.2f, 0);
        gl.glVertex2f(0.2f, 0.4f);
        gl.glVertex2f(0, 0.4f);
        gl.glEnd();

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.035f, 0.215f, 0.243f);
        gl.glVertex2f(-1.2f, 0.4f);
        gl.glVertex2f(-1.2f, 0);
        gl.glVertex2f(-1f, 0f);
        gl.glVertex2f(-1f, 0.4f);
        gl.glEnd();

        ////////////////////////////////////////////
        // Tree 
        gl.glTranslatef(0.7f, 0f, 4f);
        gl.glBegin(gl.GL_POLYGON);
        gl.glColor3f(0.18f, 0.18f, 0.18f);
        gl.glVertex2f(-0.59f, 0.3f);
        gl.glVertex2f(-0.5f, 0.45f);
        gl.glVertex2f(-0.6f, 0.6f);
        gl.glVertex2f(-0.8f, 0.6f);
        gl.glVertex2f(-0.9f, 0.45f);
        gl.glVertex2f(-0.81f, 0.3f);
        gl.glEnd();
        //******************************
        gl.glTranslatef(2.4f, -1f, -7f);
        gl.glBegin(GL.GL_POLYGON);
        gl.glColor3f(0f, 0.32f, 0f);
        gl.glVertex2f(-0.59f, 0.3f);
        gl.glVertex2f(-0.5f, 0.45f);
        gl.glVertex2f(-0.6f, 0.6f);
        gl.glVertex2f(-0.8f, 0.6f);
        gl.glVertex2f(-0.9f, 0.45f);
        gl.glVertex2f(-0.81f, 0.3f);
        gl.glEnd();
        //******************************
        gl.glTranslatef(1f, -1.5f, -5f);
        gl.glBegin(GL.GL_POLYGON);
        gl.glColor3f(0f, 0.32f, 0f);
        gl.glVertex2f(-0.59f, 0.3f);
        gl.glVertex2f(-0.5f, 0.45f);
        gl.glVertex2f(-0.6f, 0.6f);
        gl.glVertex2f(-0.8f, 0.6f);
        gl.glVertex2f(-0.9f, 0.45f);
        gl.glVertex2f(-0.81f, 0.3f);
        gl.glEnd();
        //*****************************
        gl.glLineWidth(5);
        gl.glTranslatef(0.6f, 0.4f, 0f);
        gl.glBegin(gl.GL_LINES);
        gl.glColor3f(0.27f, 0.13f, 0.13f);
        gl.glVertex2f(-0.7f, 0.3f);
        gl.glVertex2f(-0.9f, -0.5f);
        gl.glEnd();
        //*******************************
        gl.glBegin(gl.GL_LINES);
        gl.glColor3f(0.27f, 0.13f, 0.13f);
        gl.glVertex2f(-1.2f, -0.1f);
        gl.glVertex2f(-1f, -0.5f);
        gl.glEnd();

        ///////////////////////////////////////////////////
        // Recorder Device
        gl.glTranslatef(-2.8f, -1.2f, 3f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.035f, 0.215f, 0.243f);
        gl.glVertex2f(-2.3f, 0.7f);
        gl.glVertex2f(-2.3f, 0);
        gl.glVertex2f(-1f, 0f);
        gl.glVertex2f(-1f, 0.7f);
        gl.glEnd();
        //********************************
        gl.glTranslatef(-1.5f, -0.7f, -3f);
        gl.glColor3f(0.537f, 0.082f, 0.145f);
        numPoints = 10;
        Radius = -0.3f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (2.0 * Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle + 0.2) * Radius;
            double z = Math.sin(Angle + 50) * Radius;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
        //*****************************
        gl.glTranslatef(0.3f, 2.4f, 0f);
        gl.glColor3f(0.035f, 0.215f, 0.243f);
        numPoints = 10000;
        Radius = 1f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle) * Radius + 0.5;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
        //**************************

        gl.glTranslatef(0f, 0f, 0f);
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        numPoints = 10000;
        Radius = 0.8f;
        gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < numPoints; i++) {
            double Angle = i * (Math.PI / numPoints);
            double x = Math.cos(Angle) * Radius;
            double y = Math.sin(Angle) * Radius + 0.5;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }
//////////////////////////////////////////////////////////////////////////////

    void Typing(GL gl, GLUT glut) {
        // Set background white 
        gl.glClearColor(1f, 1f, 1f, 0.0f);
        try {

            Text = TextureIO.newTexture(new File("W" + WordNumber + ".jpg"), true);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        //**************************************
        gl.glTranslatef(-3f, -3f, -6.0f);
        //************************************************
        // Keyboard background
        gl.glPushMatrix();
        gl.glColor4f(0.8f, 0.8f, 0.8f, .6f);
        gl.glRectf(0, 0, 6f, 2.5f);
        gl.glPopMatrix();
        //************************************************
        // Keys
        gl.glTranslatef(-2.7f, 1.2f, -6f);
        gl.glPushMatrix();
        gl.glColor3f(0, 0, 0);

        // First  top row 
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        // First right row 
        gl.glTranslatef(1.2f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);

        // Back to left on keybord 
        gl.glTranslatef(-11f, -0.7f, 0f);

        // Scond left row 
        gl.glRectf(0, 0, 0.8f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 1.2f, 0.5f);

        // Second right row 
        gl.glTranslatef(1.9f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, -0.7f, 0.5f, 0.5f);

        // Back to left on keybord 
        gl.glTranslatef(-11f, -0.7f, 0f);

        //Threied left raw
        gl.glRectf(0, 0, 0.9f, 0.5f);
        gl.glTranslatef(1.1f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 1.5f, 0.5f);

        // Threied right row 
        gl.glTranslatef(2.2f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);

        // Back to left on keybord 
        gl.glTranslatef(-11f, -0.7f, 0f);

        //Fourth left raw
        gl.glRectf(0, 0, 1.2f, 0.5f);
        gl.glTranslatef(1.4f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 1.2f, 0.5f);

        // Fourth right row 
        gl.glTranslatef(1.9f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);

        // Back to left on keybord 
        gl.glTranslatef(-11f, -0.7f, 0f);

        // Fiveth  top row 
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 3.2f, 0.5f);
        gl.glTranslatef(3.5f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);

        // Fiveth right row 
        gl.glTranslatef(1.2f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 0.5f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glRectf(0, 0, 0.5f, 1.2f);
        gl.glTranslatef(0.7f, 0f, 0f);
        gl.glPopMatrix();

        //**************************************
        // Monitor 
        gl.glTranslatef(2.7f, 1.3f, 6f);
        gl.glPushMatrix();
        gl.glRotatef(15, 1, 0, 0);
        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glRectf(0, 0, 6f, 2.5f);
        gl.glTranslatef(0f, 0f, -0.5f);

        gl.glColor3f(1, 1, 1);
        Text.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glScalef(6, 2.4f, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);
        WordNumber++;

        gl.glPopMatrix();

        //**************************************************
        // Hand 
        // Right hand 
        gl.glTranslatef(3.3f, -1f, 2f);
        gl.glPushMatrix();

        if (Right % 10 == 0) {
            gl.glTranslatef(0f, -0.1f, 0f);
        } else if (Right % 100 == 0) {
            gl.glTranslatef(0f, 0.1f, 0f);
        }

        // R1
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();

        if (Right % 20 == 0) {
            gl.glTranslatef(0f, 0.05f, 0f);
        } else if (Right % 90 == 0) {
            gl.glTranslatef(0f, -0.05f, 0f);
        }

        //R2
        gl.glTranslatef(0.3f, 0.1f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.16, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.08, 100, 100);
        gl.glPopMatrix();

        if (Right % 30 == 0) {
            gl.glTranslatef(0f, -0.1f, 0f);
        } else if (Right % 80 == 0) {
            gl.glTranslatef(0f, 0.1f, 0f);
        }

        //R3
        gl.glTranslatef(0.3f, -0.1f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();

        if (Right % 40 == 0) {
            gl.glTranslatef(0f, 0.05f, 0f);
        } else if (Right % 70 == 0) {
            gl.glTranslatef(0f, -0.05f, 0f);
        }

        //R4
        gl.glTranslatef(0.3f, -0.2f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();
        gl.glPopMatrix();

        // increment R counter 
        Right++;

        //************************************************************8
        // Left hand 
        gl.glTranslatef(-1.8f, -0.2f, 0f);
        gl.glPushMatrix();

        if (Left % 100 == 0) {
            gl.glTranslatef(0f, -0.1f, 0f);
        } else if (Left % 10 == 0) {
            gl.glTranslatef(0f, 0.1f, 0f);
        }

        // L1
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.16, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.07, 100, 100);
        gl.glPopMatrix();

        if (Left % 90 == 0) {
            gl.glTranslatef(0f, 0.05f, 0f);
        } else if (Left % 20 == 0) {
            gl.glTranslatef(0f, -0.05f, 0f);
        }

        //L2
        gl.glTranslatef(0.3f, 0.17f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.16, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.08, 100, 100);
        gl.glPopMatrix();

        if (Left % 80 == 0) {
            gl.glTranslatef(0f, -0.1f, 0f);
        } else if (Left % 30 == 0) {
            gl.glTranslatef(0f, 0.1f, 0f);
        }

        //L3
        gl.glTranslatef(0.3f, 0.1f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();

        if (Left % 70 == 0) {
            gl.glTranslatef(0f, 0.05f, 0f);
        } else if (Left % 40 == 0) {
            gl.glTranslatef(0f, -0.05f, 0f);
        }

        //L4
        gl.glTranslatef(0.3f, -0.1f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 3, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();
        gl.glPopMatrix();

        // increment R counter 
        Left++;

        // Set background white 
        gl.glColor3f(1, 1, 1);
    }
////////////////////////////////////////////////////////////////////////////////

    void Braille(GL gl, GLUT glut) {
        //**************************************
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA,
                GL.GL_ONE_MINUS_SRC_ALPHA);
        //**************************************
        gl.glTranslatef(-5f, -2.5f, -6.0f);

        // Table 
        gl.glPushMatrix();
        Table.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glScalef(10, 6f, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);
        gl.glPopMatrix();

        //**************************************
        // Peper
        gl.glTranslatef(1.8f, 0f, 0f);
        gl.glPushMatrix();
        gl.glColor4f(0, 0, 0, 0.3f);
        gl.glRectf(0, 0, 4.1f, 4f);
        gl.glColor3f(1, 1, 1);
        gl.glTranslatef(0.05f, -0.05f, 0f);
        gl.glRectf(0, 0, 4f, 4f);
        gl.glColor3f(1, 1, 1);
        gl.glPopMatrix();

        //**************************************
        // Cup of coffee
        gl.glTranslatef(4.5f, 0.5f, 0f);
        gl.glPushMatrix();
        Coffee.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glScalef(2, 2f, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);
        gl.glPopMatrix();

        //**************************************
        // Braille Words 
        gl.glTranslatef(-9f, -5.5f, -10f);

        gl.glPushMatrix();
        Paper.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glScalef(9, 11f, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1, 1, 1);
        gl.glPopMatrix();
        //**************************************

        if (BlindHand < 150) {
            gl.glTranslatef(2f, 6.5f, 9f);
            gl.glTranslatef(MoveBraille, 0, 0);
        } else {
            gl.glTranslatef(3.5f, 6.5f, 9f);
        }
        // Hand 
        // Right hand 
        gl.glTranslatef(3.3f, -1f, 2f);
        gl.glPushMatrix();

        if (BlindHand % 10 == 0 && BlindHand < 150) {
            gl.glTranslatef(0f, -0.1f, 0f);
        } else if (BlindHand % 100 == 0 && BlindHand < 150) {
            gl.glTranslatef(0f, 0.1f, 0f);
        }

        // R1
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 4.5f, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();

        if (BlindHand % 20 == 0 && BlindHand < 150) {
            gl.glTranslatef(0f, 0.05f, 0f);
        } else if (BlindHand % 90 == 0 && BlindHand < 150) {
            gl.glTranslatef(0f, -0.05f, 0f);
        }

        //R2
        gl.glTranslatef(0.3f, 0.1f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 4.5f, 1);
        glut.glutSolidSphere(0.16, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.08, 100, 100);
        gl.glPopMatrix();

        //R3
        gl.glTranslatef(0.3f, -0.1f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 4.5f, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();

        //R4
        gl.glTranslatef(0.3f, -0.2f, 0f);
        gl.glPushMatrix();
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glScalef(0.7f, 4.5f, 1);
        glut.glutSolidSphere(0.17, 100, 100);
        gl.glTranslatef(0f, 0.11f, 0f);
        gl.glColor3f(1, 1, 1);
        gl.glScalef(1f, 0.4f, 1);
        glut.glutSolidSphere(0.09, 100, 100);
        gl.glPopMatrix();
        gl.glPopMatrix();

        MoveBraille += 0.01;
        BlindHand++;
        //*********************************
        gl.glColor3f(1, 1, 1);
    }

    /////////////////////////////////////////////////////////////////
    void BlindMan(GLUT glut, GL gl) {
        gl.glPushMatrix();

        gl.glRotatef(-90, 0f, 1, 0);
        head(glut, gl);
        neck(glut, gl);
        leftleg(glut, gl);
        leftFoot(glut, gl);
        leftArm(glut, gl);
        leftHand(glut, gl);
        rightleg(glut, gl);
        rightFoot(glut, gl);
        torus(glut, gl);
        rightArm(glut, gl);
        rightHand(glut, gl);
        Stick(glut, gl);
        gl.glPopMatrix();

    }

    //***************************************
    // Head 
    public void head(GLUT glut, GL gl) {
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glTranslatef(0.0f, 1f, 0f);
        glut.glutSolidSphere(0.5, 30, 16);

    }

    //***************************************
    // Neck
    public void neck(GLUT glut, GL gl) {;
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glTranslatef(0.0f, -0.7f, 0f);
        gl.glRotatef(-90.0f, 1f, 0.0f, 0.0f);
        glut.glutSolidCylinder(0.26f, .3f, 50, 50);
        gl.glRotatef(90.0f, 1f, 0.0f, 0.0f);
    }

    //***************************************
    // Body
    public void torus(GLUT glut, GL gl) {
        gl.glColor3f(0.513f, 0.678f, 0.745f);
        gl.glTranslatef(-0.35f, 0.6f, 0f);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        glut.glutSolidCylinder(0.6f, 1.5f, 10, 10);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

    }

    //***************************************
    //Left Part 
    // Left leg
    public void leftleg(GLUT glut, GL gl) {

        gl.glColor3f(0, 0, 0);
        gl.glTranslatef(-0.2f, -1f, 0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        glut.glutSolidCylinder(0.1, 1, 100, 50);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);

    }

    // Left foot
    public void leftFoot(GLUT glut, GL gl) {

        gl.glColor3f(1, 1, 1);
        gl.glTranslatef(0f, -1f, 0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        glut.glutSolidCylinder(0.1, 0.2, 100, 50);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
    }

    // Left Arm
    public void leftArm(GLUT glut, GL gl) {
        gl.glColor3f(0.513f, 0.678f, 0.745f);
        gl.glTranslatef(-0.6f, 0.8f, 0f);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(13f, 0f, 1f, 0.0f);
        glut.glutSolidCylinder(0.1, 1.3, 500, 500);
        gl.glRotatef(-13f, 0f, 1f, 0.0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
    }

    // Left Hand 
    public void leftHand(GLUT glut, GL gl) {

        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glTranslatef(0f, 0f, 0f);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(13f, 0f, 1f, 0.0f);
        glut.glutSolidCylinder(0.1, 0.2, 100, 100);
        gl.glRotatef(-13f, 0f, 1f, 0.0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

    }

    // Left glass
    public void leftGlass(GL gl) {
        gl.glColor3f(0f, 0f, 0f);
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, 1.7f, 0f);
        gl.glRectf(0f, 0f, 0.3f, 0.2f);
        gl.glPopMatrix();
    }

    //***************************************
    //Right Part
    // Right leg 
    public void rightleg(GLUT glut, GL gl) {
        gl.glColor3f(0, 0, 0);
        gl.glTranslatef(1.2f, 0.2f, 0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        glut.glutSolidCylinder(0.1, 1.2, 100, 50);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
    }

    //Right foot
    public void rightFoot(GLUT glut, GL gl) {
        gl.glColor3f(1, 1, 1);
        gl.glTranslatef(0f, -1f, 0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        glut.glutSolidCylinder(0.1, 0.2, 100, 50);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
    }

    // Right Arm 
    public void rightArm(GLUT glut, GL gl) {
        gl.glColor3f(0.513f, 0.678f, 0.745f);
        gl.glTranslatef(0.82f, 0.2f, 0f);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(-13f, 0f, 1f, 0.0f);
        glut.glutSolidCylinder(0.1, 1.3, 500, 500);
        gl.glRotatef(13f, 0f, 1f, 0.0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

    }

    // Right Hand 
    public void rightHand(GLUT glut, GL gl) {
        gl.glColor3f(0.894f, 0.796f, 0.686f);
        gl.glTranslatef(0f, 0f, 0f);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(-13f, 0f, 1f, 0.0f);
        glut.glutSolidCylinder(0.1, 0.2, 100, 100);
        gl.glRotatef(13f, 0f, 1f, 0.0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

    }

    // Right glass
    public void rightGlass(GLUT glut, GL gl) {
        gl.glColor3f(0f, 0f, 0f);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 5f, -0.6f);
        gl.glRectf(0.5f, 0.4f, 0.8f, 0.4f);
        gl.glPopMatrix();
    }

    //***************************************
    // Gray stick
    void Stick(GLUT glut, GL gl) {

        gl.glColor3f(0.2f, 0.2f, 0.2f);
        gl.glTranslatef(0f, 0.1f, 0f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(60f, 0f, 1.0f, 1.0f);
        glut.glutSolidCylinder(0.03, 1, 100, 50);
        gl.glRotatef(-60f, 0f, 1.0f, 1.0f);
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
    }
//////////////////////////////////////////////////////////////////////////

    void BlindLibrary(GLAutoDrawable drawable, GL gl) {

        gl.glColor3f(1, 1, 1);
        //***********************************

        // Streat 
        gl.glPushMatrix();
        street(gl);
        gl.glPopMatrix();
        //***********************************
        gl.glTranslatef(0f, -1.0f, -10.0f);
        //***********************************
        // Door
        gl.glPushMatrix();
        quad(gl);
        gl.glPopMatrix();
        //***********************************
        // Wall 
        gl.glPushMatrix();
        wall(gl);
        gl.glPopMatrix();
        //***********************************
        // Top of library 
        gl.glPushMatrix();
        top(gl);
        gl.glPopMatrix();
        //***********************************
        gl.glPushMatrix();
        traing(gl);
        gl.glPopMatrix();
        //***********************************
        // Left window 
        gl.glPushMatrix();
        squaresLeft(gl);
        gl.glPopMatrix();
        //***********************************
        // Right window 
        gl.glPushMatrix();
        rightSquares(gl);
        gl.glPopMatrix();
        //***********************************
        gl.glColor3f(1, 1, 1);

        //***********************************
        text = new TextRenderer(new Font("monotype corsiva", Font.BOLD, 80));
        text.beginRendering(drawable.getWidth(), drawable.getHeight());
        text.setColor(0.540f, 0.267f, 0.058f, 1f);
        text.draw("Library ", 600, 550);
        text.endRendering();

    }
    //****************************************************

    public void quad(GL gl) {

        //***************************************
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        //***************************************
        // Inside library
        gl.glPushMatrix();
        gl.glTranslatef(-2.1f, -2.1f, -0.5f);
        gl.glScaled(4.2, 3.1, 0);
        gl.glColor3f(1, 1, 1);
        Library.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        //**************************************
        if (dMove == 0) {
            gl.glTranslatef(-1f, 0, 0);
        }
        //**************************************
        // Left side of door 
        gl.glBegin(GL.GL_QUADS);
        gl.glColor4f(0.90f, 0.98f, 0.99f, 0.8f);
        gl.glVertex3f(-2.0f, 1.0f, 0.0f);
        gl.glColor4f(1f, 1f, 1f, 0.8f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor4f(0.90f, 0.98f, 0.99f, 0.8f);
        gl.glVertex3f(0.0f, -2.0f, 0.0f);
        gl.glColor4f(1f, 1f, 1f, 0.8f);
        gl.glVertex3f(-2.0f, -2.0f, 0.0f);
        gl.glEnd();
        //**************************************

        if (dMove == 0) {
            gl.glTranslatef(1f, 0, 0);
        }
        //**************************************
        if (dMove == 0) {
            gl.glTranslatef(1f, 0, 0);
        }
        //**************************************
        // Right side of door 
        gl.glBegin(GL.GL_QUADS);
        gl.glColor4f(0.90f, 0.98f, 0.99f, 0.8f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor4f(1f, 1f, 1f, 0.8f);
        gl.glVertex3f(2.0f, 1.0f, 0.0f);
        gl.glColor4f(0.90f, 0.98f, 0.99f, 0.8f);
        gl.glVertex3f(2.0f, -2.0f, 0.0f);
        gl.glColor4f(1f, 1f, 1f, 0.8f);
        gl.glVertex3f(0.0f, -2.0f, 0.0f);
        gl.glEnd();
        //**************************************
        if (dMove == 0) {
            gl.glTranslatef(-1f, 0, 0);
        }
        //**************************************
        gl.glDisable(GL.GL_BLEND);

    }

    //*************************************************
    public void wall(GL gl) {

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.905f, 0.756f, 0.113f);
        gl.glVertex3f(-10.0f, 1.0f, 0.0f);
        gl.glVertex3f(-2.0f, 1.0f, 0.0f);
        gl.glVertex3f(-2.0f, -2.0f, 0.0f);
        gl.glVertex3f(-10.0f, -2.0f, 0.0f);
        gl.glEnd();
        //**************************************
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.905f, 0.756f, 0.113f);
        gl.glVertex3f(2.0f, 1.0f, 0.0f);
        gl.glVertex3f(10.0f, 1.0f, 0.0f);
        gl.glVertex3f(10.0f, -2.0f, 0.0f);
        gl.glVertex3f(2.0f, -2.0f, 0.0f);
        gl.glEnd();

    }

    //****************************************************************
    public void top(GL gl) {

        gl.glTranslatef(0f, 2.0f, -6.0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.905f, 0.756f, 0.113f);
        gl.glVertex3f(-6.0f, 1.0f, 0.0f);
        gl.glVertex3f(6.0f, 1.0f, 0.0f);
        gl.glVertex3f(6.0f, -1.0f, 0.0f);
        gl.glVertex3f(-6.0f, -1.0f, 0.0f);
        gl.glEnd();

    }

    //********************************************************
    public void squaresLeft(GL gl) {

        gl.glTranslatef(-10f, -.5f, -10);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 0.99f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(3.5f, 0f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //******************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(0f, -3f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(-3.5f, 0f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(4f, 5.5f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();

    }

    //******************************************************************
    public void rightSquares(GL gl) {

        gl.glTranslatef(6f, -.5f, -9);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(3.5f, 0f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //******************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(0f, -3f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(-3.5f, 0f, 0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glTranslatef(-0.5f, 5.5f, 0);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.540f, 0.267f, 0.058f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glEnd();
        //**************************************

        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(-.8f, .8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(.8f, .8f, 0.0f);
        gl.glColor3f(0.90f, 0.98f, 0.99f);
        gl.glVertex3f(.8f, -.8f, 0.0f);
        gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f(-.8f, -.8f, 0.0f);
        gl.glEnd();

    }

    //******************************************************
    public void traing(GL gl) {

        gl.glTranslatef(0f, 5.69f, -6.0f);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(0.905f, 0.756f, 0.113f);
        gl.glVertex3f(0.0f, .5f, 0.0f);
        gl.glVertex3f(-3.12f, -3.12f, 3.12f);
        gl.glVertex3f(3.12f, -3.12f, 3.12f);
        gl.glEnd();
    }
    //******************************************************

    public void street(GL gl) {

        gl.glPushMatrix();
        gl.glTranslatef(-2f, -0.5f, -3f);
        gl.glScaled(4, 2.5, 0);
        Sky.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPopMatrix();

        gl.glTranslatef(-2.5f, -1.3f, -3f);
        gl.glScaled(5, 0.5, 0);
        gl.glColor3f(1, 1, 1);
        Streat.bind();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 1);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
    }
}
