import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import java.lang.System;
import java.lang.Runtime;
import java.io.IOException;


public class BookDrawings extends JPanel
    implements ActionListener, MouseListener, MouseMotionListener {
    // Variables Globales
    private int cx, cy;
    private final int nz=6; //node size
    
    // Declaración de botones
    private JButton buttonDraw = new JButton("Draw");
    
    // Declaración de texto
    private JTextField text = new JTextField("ProjectName",20);
    
    //private int mainflag=0, colorvflag=-1, binvflag=0, nodeflag;
    
    public BookDrawings( ) {
        
        add(text);
        text.setEditable(true);
	buttonDraw.addActionListener(this);
        add(buttonDraw);
        addMouseListener (this);
        addMouseMotionListener (this);
        text.addActionListener(this);
    }
    
    public static int countOccurrencesOf(String haystack, char needle){
        int count = 0;
        for (int i=0; i < haystack.length(); i++)
	    {
		if (haystack.charAt(i) == needle)
		    {
			count++;
		    }
	    }
        return count;
    }
    
    public static void printmatrix(int [][] matriz){
        int n = matriz.length;
        int m = matriz[0].length;
        for(int i=0; i<n; i++)
            for(int j=0; j<m; j++)
                System.out.print( matriz[i][j] + (j==m-1? "\n" : " " ) );
    }
    
    public static int[][] string2matrix(String cadena){
        int ini = 1;
        int fin = cadena.indexOf('}',ini);
        
        String subcad = cadena.substring(cadena.indexOf('{',1)+1,fin);
        int n = countOccurrencesOf(cadena,'{') - 1; // filas
        int m = countOccurrencesOf(subcad,',') + 1; // columnas
        int[][] matriz = new int [n][m];
        
        for(int j=0; j<n; j++){
            int in = 0;
            int fi;
            for(int i=0; i<m; i++){
                if(i== m-1)
                    fi = subcad.length();
                else
                    fi = subcad.indexOf(',',in);
                matriz[j][i] = Integer.parseInt(subcad.substring(in,fi).replaceAll(" ",""));
                in = fi+1;
            }
            if(j<n-1){
                ini = cadena.indexOf('{',fin);
                fin = cadena.indexOf('}',ini);
                subcad = cadena.substring(ini+1,fin);
            }
        }
        return matriz;
    }

    /*
    public int[][] graph2Laplacian(){
        
        int deleted = 0;
        int[] dvector = new int [nodos.size()];
        
        for(int i=0; i<nodos.size(); i++){
            if(nodos.get(i).d == true)
                deleted++;
            dvector[i] = deleted;
        }
        
        int nnodos = nodos.size()-deleted;
        int[][] matrix = new int [nnodos][nnodos];
        
        for(int i=0; i<nodos.size(); i++)
            if(!nodos.get(i).d){
                int aux1 = i - dvector[i];
                
                for(int j=0; j<nodos.get(i).list.size(); j++)
                    if(!nodos.get(nodos.get(i).list.get(j)).d){
                        int aux2 = nodos.get(i).list.get(j) - dvector[nodos.get(i).list.get(j)];
                        matrix[aux1][aux2]=-1;
                        
                        // This is in the non-directed graph case
                        if(graphtype == 0)
                            matrix[aux2][aux1]=-1;
                    }
            }
        
        for(int i=0; i<nnodos; i++){
            int suma=0;
            for(int j=0; j<nnodos; j++)
                if(i!=j)
                    suma+=matrix[i][j];
            matrix[i][i] = (-suma);
        }
        
        return matrix;
    }
    
    public String vars2string(){
        
        String graph = "";
        
        int deleted = 0;
        
        for(int i=0; i<nodos.size(); i++)
            if(nodos.get(i).d == true)
                deleted++;
        
        int nnodos = nodos.size() - deleted;
        
        for(int i=0; i<nnodos; i++)
            graph += ( i == 0? "x"  : ",x" ) + i;
        
        return graph;
        
    }
    
    public String graph2string(){
        
        String graph = "{";
        
        int deleted = 0;
        int[] dvector = new int [nodos.size()];
        
        for(int i=0; i<nodos.size(); i++){
            if(nodos.get(i).d == true)
                deleted++;
            dvector[i] = deleted;
        }
        
        int nnodos = nodos.size()-deleted;
        boolean firstused = true;
        int[][] matrix = graph2Laplacian();
        
        for(int i=0; i<nnodos; i++){
            int flag = 0;
            String graph1 = (firstused? "" : ",") + Integer.toString(i) + ":[";
            
            if(graphtype == 0){
                for(int j=i+1; j<nnodos; j++)
                    if(matrix[i][j] != 0 ){
                        graph1 += (flag == 0? "" : ",") + Integer.toString(j);
                        if(flag == 0)
                            flag = 1;
                    }
            }else
                for(int j=0; j<nnodos; j++)
                    if( i != j && matrix[i][j] != 0 ){
                        graph1 += (flag == 0? "" : ",") + Integer.toString(j);
                        if(flag == 0)
                            flag = 1;
                    }
            
            graph1 += "]";
            if(flag != 0){
                graph += graph1;
                firstused = false;
            }
        }
        
        graph += "}";
        
        return graph;
        
    }

    
    public void toLaplacian(){
        String dirName = "files";
        File dir = new File (dirName);
        String cadena = text.getText() + ".tex";
        File archivo = new File(dir,cadena);
        FileWriter file = null;
        PrintWriter fout = null;
        
        try{
            file = new FileWriter(archivo);
            fout = new PrintWriter(file);
            
            
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != file)
                    file.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public void toLaTeX (){
        String dirName = "files";
        File dir = new File (dirName);
        String cadena = text.getText() + ".tex";
        File archivo = new File(dir,cadena);
        FileWriter file = null;
        PrintWriter fout = null;
        try{
            file = new FileWriter(archivo);
            fout = new PrintWriter(file);
            fout.println("\\documentclass[11pt,twoside]{amsart}");
            fout.println( "\\usepackage{tikz}");
            fout.println("\\begin{document} ");
            fout.println("	\\begin{center}");
            fout.println("		\\begin{tikzpicture}[scale=2,thick]");
            fout.println("		\\tikzstyle{every node}=[minimum width=0pt, inner sep=2pt, circle]");
            
            int w=getSize ( ).width;
            int h=getSize ( ).height;
            cx = w/2;
            cy = h/2;
            
            int deleted = 0;
            int[] dvector = new int [nodos.size()];
            
            for(int i=0; i<nodos.size(); i++){
                if(nodos.get(i).d == true)
                    deleted++;
                dvector[i] = deleted;
            }
            
            for(int i=0; i<nodos.size(); i++){
                if(nodos.get(i).d == false)
                    fout.println("			\\draw (" + (float)  (nodos.get(i).x - cx)/100 + "," + (float) (cy - nodos.get(i).y)/100 + ") node[draw] (" + i + ") { \\tiny " + (i - dvector[i]) + "};");
            }
            
            for( int i=0; i<nodos.size(); i++ )
                for( int j=0; j<nodos.get(i).list.size(); j++ )
                    if(nodos.get(i).d == false)
                        if(nodos.get(nodos.get(i).list.get(j)).d == false)
                            if(graphtype == 1)
                                fout.println("			\\draw  (" + i + ") edge[->] (" + nodos.get(i).list.get(j) + ");");
                            else
                                if(nodos.get(i).list.get(j) > i)
                                    fout.println("			\\draw  (" + i + ") edge (" + nodos.get(i).list.get(j) + ");");
            
            fout.println("		\\end{tikzpicture}");
            fout.println("	\\end{center}");
            fout.println("\\end{document}");
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != file)
                    file.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public void runLaTeX() {
        
        try{
            String cadena = text.getText();
            String osname = System.getProperty("os.name");
            System.out.println(osname);
            if( osname.toLowerCase().compareTo("linux") == 0 ){
                System.out.println("pdflatex ./files/" + cadena + ".tex");
                String[] cmd = {"/bin/sh", "-c", "cd ./files/ && pdflatex ./" + cadena + ".tex"};
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                System.out.println("evince ./files/" + cadena + ".pdf");
                Runtime.getRuntime().exec("evince ./files/" + cadena + ".pdf");
            } else if ( osname.toLowerCase().indexOf("windows") != -1 ){
                System.out.println("Not implemented yet. In the future it will execute: pdflatex ./files/" + cadena + ".tex");
                //String[] cmd = {"cmd.exe", "/c", "cd \"files\"", "pdflatex " + cadena + ".tex"};
                //Process p = Runtime.getRuntime().exec(cmd);
                //p.waitFor();
            } else if(osname.toLowerCase().indexOf("mac")!=-1){
                System.out.println("pdflatex ./files/" + cadena + ".tex");
                String[] cmd = {"/bin/sh", "-c", "cd ./files/ && pdflatex ./" + cadena + ".tex"};
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                System.out.println("open ./files/" + cadena + ".pdf");
                Runtime.getRuntime().exec("open ./files/" + cadena + ".pdf");
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }*/
    
    public void paintComponent (Graphics g) {
        super.paintComponent( g );
        
        int w=getSize ( ).width;
        int h=getSize ( ).height;
        cx = w/2;
        cy = h/2;
        
        g.setColor (Color.white);
        g.fillRect (0, 0, w, h);
	
        /*
        //if(!(mainflag == 2 && binvflag == 1))
        // Dibuja las aristas
        for( int i=0; i<nodos.size(); i++ )
            for( int j=0; j<nodos.get(i).list.size(); j++ )
                if(!(nodos.get(i).d || nodos.get(nodos.get(i).list.get(j)).d)){
                    g.setColor(Color.black);
                    g.drawLine(nodos.get(i).x,nodos.get(i).y,nodos.get(nodos.get(i).list.get(j)).x,nodos.get(nodos.get(i).list.get(j)).y);
                    if(graphtype == 1){
                        int x1 = nodos.get(i).x, y1 = nodos.get(i).y, x2 = nodos.get(nodos.get(i).list.get(j)).x, y2 = nodos.get(nodos.get(i).list.get(j)).y;
                        double D = Math.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );
                        // u is the point at distance D-arrowW from x to y
                        double u1 = (D-arrowW)*x2/D + arrowW*x1/D;
                        double u2 = (D-arrowW)*y2/D + arrowW*y1/D;
                        // p is the perpendicular unit vector
                        double p1 = arrowH*(x2 - x1)/D;
                        double p2 = arrowH*(y1 - y2)/D;
                        // v is the point at distance D-nz from x to y
                        double v1 = (D-nz)*x2/D + nz*x1/D;
                        double v2 = (D-nz)*y2/D + nz*y1/D;
                        g.drawLine( (int) (u1+p2), (int) (u2+p1), (int)v1, (int)v2);
                        g.drawLine( (int) (u1-p2), (int) (u2-p1), (int)v1, (int)v2);
                    }
                    
                }
        
        // Dibuja los nodos
        int allowed = -1;
        for(int i=0; i<nodos.size(); i++){
            g.setColor(Color.white);
            if(nodos.get(i).d)
                g.setColor(Color.gray);
            else
                allowed++;
            if(mainflag == 1 && i == colorvflag){
                if(binvflag == 1)
                    g.setColor (Color.green);
                if(binvflag == 0)
                    g.setColor (Color.red);
                colorvflag=-1;
            }
            g.fillOval(nodos.get(i).x-nz,nodos.get(i).y-nz,2*nz,2*nz);
            g.setColor(Color.black);
            if(!nodos.get(i).d)
                g.drawString("" + (allowed),nodos.get(i).x-nz,nodos.get(i).y-2*nz);
            g.drawOval(nodos.get(i).x-nz,nodos.get(i).y-nz,2*nz,2*nz);
        }
	*/
        
    }
    
    public void mousePressed (MouseEvent event) { }
    public void mouseDragged (MouseEvent event) { }
    public void mouseReleased (MouseEvent event) { }
    public void mouseClicked (MouseEvent event) { }
    public void mouseEntered (MouseEvent event) { }
    public void mouseExited (MouseEvent event) { }
    public void mouseMoved (MouseEvent event) { }
    
    public void actionPerformed (ActionEvent event) {
	Object s = event.getSource( );
	if (s == buttonDraw){
	    
	}
	else if (s == text) {
	    String cadena = text.getText();
	    int entroACasos = 0;
	    
	    if(cadena.charAt(0) == 'K'){
		for(int i = 1; i < cadena.length(); i++)
		    if( Character.getNumericValue(cadena.charAt(i)) >= 0 && Character.getNumericValue(cadena.charAt(i)) <= 9)
			System.out.println("Entro " + cadena.charAt(i));
		    else
			System.out.println("Salio " + cadena.charAt(i));
		// if(cadena.length() > 1)
		//     nodosize = Integer.parseInt(cadena.substring(1,cadena.length()));
			
		// int w=getSize ( ).width;
		// int h=getSize ( ).height;
		// cx = w/2;
		// cy = h/2;
		// nodos.clear();
			
		// for(int j=0; j<nodosize; j++){
		//     Nodo nodo = new Nodo();
		//     nodo.x = (int) -(200* Math.cos( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cx;
		//     nodo.y = (int) (200* Math.sin( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cy;
				
		//     nodos.add(nodo);
		//     for(int k=0; k<nodosize; k++)
		// 	if(k != j)
		// 	    nodo.list.add(k);
		// }
		// repaint();
	    }
	    else if(cadena.charAt(0)== 'C'){ // Ciclo
	    // 	if(cadena.length() > 1)
	    // 	    nodosize = Integer.parseInt(cadena.substring(1,cadena.length()));
			
	    // 	int w=getSize ( ).width;
	    // 	int h=getSize ( ).height;
	    // 	cx = w/2;
	    // 	cy = h/2;
	    // 	nodos.clear();
			
	    // 	for(int j=0; j<nodosize; j++){
	    // 	    Nodo nodo = new Nodo();
	    // 	    nodo.x = (int) -(200* Math.cos( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cx;
	    // 	    nodo.y = (int) (200* Math.sin( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cy;
				
	    // 	    if(j < nodosize -1)
	    // 		nodo.list.add(j+1);
	    // 	    else
	    // 		nodo.list.add(0);
				
	    // 	    nodos.add(nodo);
	    // 	}
	    // 	repaint();
	    } 
	    else if(cadena.charAt(0)== 'P'){ // Camino
	    // 	if(cadena.length() > 1)
	    // 	    nodosize = Integer.parseInt(cadena.substring(1,cadena.length()));
			
	    // 	int w=getSize ( ).width;
	    // 	int h=getSize ( ).height;
	    // 	cx = w/2;
	    // 	cy = h/2;
	    // 	nodos.clear();
			
	    // 	for(int j=0; j<nodosize; j++){
	    // 	    Nodo nodo = new Nodo();
	    // 	    nodo.x = (int) -(200* Math.cos( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cx;
	    // 	    nodo.y = (int) (200* Math.sin( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cy;
				
	    // 	    if(j < nodosize -1)
	    // 		nodo.list.add(j+1);
				
	    // 	    nodos.add(nodo);
	    // 	}
	    // 	repaint();
	    // }
	    // else if(cadena.charAt(0)== 'T'){ // Grafica Trivial
	    // 	if(cadena.length() > 1)
	    // 	    nodosize = Integer.parseInt(cadena.substring(1,cadena.length()));
			
	    // 	int w=getSize ( ).width;
	    // 	int h=getSize ( ).height;
	    // 	cx = w/2;
	    // 	cy = h/2;
	    // 	nodos.clear();
			
	    // 	for(int j=0; j<nodosize; j++){
	    // 	    Nodo nodo = new Nodo();
	    // 	    nodo.x = (int) -(200* Math.cos( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cx;
	    // 	    nodo.y = (int) (200* Math.sin( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cy;
				
	    // 	    nodos.add(nodo);
	    // 	}
	    // 	repaint();
	    }
	    // else if(cadena.charAt(0)== '{'){ // Desde Matriz
	    // 	int [][] matriz = string2matrix(cadena);
	    // 	nodosize = matriz.length;
			
	    // 	int w=getSize ( ).width;
	    // 	int h=getSize ( ).height;
	    // 	cx = w/2;
	    // 	cy = h/2;
	    // 	nodos.clear();
			
	    // 	for(int j=0; j<nodosize; j++){
	    // 	    Nodo nodo = new Nodo();
	    // 	    nodo.x = (int) -(200* Math.cos( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cx;
	    // 	    nodo.y = (int) (200* Math.sin( ( (double) 2*Math.PI*j)/ (double) nodosize)) + cy;
				
	    // 	    nodos.add(nodo);
	    // 	    for(int k=0; k<nodosize; k++)
	    // 		if(matriz[j][k] != 0 && k != j)
	    // 		    nodo.list.add(k);
	    // 	}
	    // 	repaint();
	    // }
	}
    }    
    
    public static void main (String[ ] args) {
        BookDrawings inter = new BookDrawings ( );
        JFrame jf = new JFrame ( );
        jf.setTitle ("Book Drawings");
        jf.setSize (800, 800);
        jf.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        jf.setContentPane (inter);
        jf.setVisible(true);
    }
}
