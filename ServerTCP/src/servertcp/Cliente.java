package servertcp;

import java.net.*;
import java.io.*;
import java.util.Scanner;

class cliente {

    public static void main(String[] args) {
        DataOutputStream salida;
        DataInputStream entrada;
        BufferedInputStream bis;
        BufferedOutputStream bos;

        byte[] bytes;
        int in, cent2 = 1;
        String archivo;

        Scanner sc = new Scanner(System.in); //Se crea el lector

        try {
            Socket cliente = new Socket("localhost", 5000);//creaomos la conexion con el servidor
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            System.out.println("Servidor conectado");
            System.out.println("Ingrese el nombre del archivo: ");
            String nombe = sc.nextLine();//leemos el nombre del archivo que desea el cliente
            salida.writeUTF(nombe);//le enviamos el nombre al servidor

            String cent = entrada.readUTF();//recibimos una respuesta del servidor si lo encontro o no
            if (cent.equals("No")) {//si no lo encuentra

                System.out.println("\nNo se encontro el archivo dentro del directorio del servidor");
                cliente.close();//cerramos la conexion con el servidor

            } else {//si lo encuentra
                System.out.println("\nArchivo encontrado");
                bytes = new byte[1024];//buffer de 1024 bytes

                bis = new BufferedInputStream(cliente.getInputStream());//preparamos la variable para recibir el archivo
                DataInputStream dis = new DataInputStream(cliente.getInputStream());

                archivo = dis.readUTF();//en la variable archivo se guarda el nombre del archivo
                archivo = archivo.substring(archivo.indexOf('\\') + 1, archivo.length());

                bos = new BufferedOutputStream(new FileOutputStream(archivo));//variable que ocuparemos para descargar el archivo

                while ((in = bis.read(bytes)) != -1) {//lo desempaquetamos
                    bos.write(bytes, 0, in);//se reciben las partes desde el servidor
                    if (cent2 == 1) {
                        System.out.println("\nDescargando...");
                        cent2 = 2;
                    }
                }
                System.out.println("\nArchivo descargado");
                bis.close();
                bos.close();
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
