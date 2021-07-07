package servertcp;

import java.net.*;
import java.io.*;

class ServerTCP {

    public static void main(String[] args) {
        ServerSocket server;
        Socket conexion;

        DataOutputStream salida;
        DataInputStream entrada;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        int in,cent2 = 1;
        byte[] bytes;

        String archivo = "/home/samuel/Descargas/";//direcotorio al cual se le agregara el nombre del archivo

        try {
            server = new ServerSocket(5000);//se crea un socket para el servidor
            conexion = server.accept();//esperando una conexion con el cliente
            System.out.println("Cliente conectado\n");
            entrada = new DataInputStream(conexion.getInputStream());
            salida = new DataOutputStream(conexion.getOutputStream());

            String nombre = entrada.readUTF();//se recibe el nombre del archivo que desea el cliente
            File directorio = new File("/home/samuel/Descargas/");//se crea un directorio
            String[] lista = directorio.list();//se crea una lista de los archivos del directorio

            int cent = 0;//variable centinela
            for (int i = 0; i < lista.length; i++) {
                if (nombre.equals(lista[i])) {//comparamos archivo por archivo si es el que el cliente desea
                    archivo = archivo.concat(nombre);//si lo encuentra lo concatena con el directorio llamado archivo
                } else {
                    cent++;//si no lo encuentra la variable centinela se actualiza
                }
            }

            if (cent == lista.length) {//si la variable centinela es igual a la cantidad de archivos del directorio 
                System.out.println("No se encontro el archivo "+nombre+" dentro del directorio");
                salida.writeUTF("No");//envia un mensaje al cliente de que no encontro el archivo
                conexion.close();//cerramos la conexion con el cliente
            } else {
                salida.writeUTF("Si");//envia un mensaje al cliente de que si encontro el archivo
                System.out.println("Se encontro el archivo: "+nombre+"\n");
                final File localFile = new File(archivo);//se crea una variable de tipo file

                bis = new BufferedInputStream(new FileInputStream(localFile));
                bos = new BufferedOutputStream(conexion.getOutputStream());//se confirma la conexion con el cliente

                DataOutputStream dos = new DataOutputStream(conexion.getOutputStream());//se envia el nombre del archivo
                dos.writeUTF(localFile.getName());//

                bytes = new byte[8000];//se envia el archivo
                while ((in = bis.read(bytes)) != -1) {//se empaqueta el archivo en partes dependiendo del buffer
                    if(cent2 == 1){
                        System.out.println("Enviando...\n");
                        cent2 = 2;
                    }
                    bos.write(bytes, 0, in);//se envian las partes al cliente
                }
                System.out.println("Se envio el archivo al cliente");
                bis.close();
                bos.close();
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
