import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class main {
    
    public static final Integer EOF = -1;
    
    public static void main(String[] args){
        Scanner leer = new Scanner(System.in);
        
        System.out.println("Asegúrese de que operadores y números estén separados por espacios,");
        System.out.println("excepto cuando sea un número negativo, en ese caso escriba el signo negativo junto el número");
        System.out.println("Ejemplo: '1 + ( -4 ) + 2 - 2 / ( 1 / 1 )'");
        System.out.println("\nIntroduzca el número de expresiones en el archivo");
       

        
        int tam = leer.nextInt();
        leer.nextLine();
        
        System.out.println("Introduzca la dirección del archivo, incluyendo el nombre del archivo, ejemplo:  "); //se pide la dirección del archivo
        System.out.println("/Users/macbook/desktop/input.txt");
        String direccion = leer.nextLine();
        lista colita = new lista();
        
        try{  //con este try se abre el doc que contiene las expresiones
            File file = new File(direccion);        
            try (FileReader reader = new FileReader(file)) {
                BufferedReader origen = new BufferedReader(new FileReader(file));
                String linea;
                for(int i = 0; i<tam; i++){ //se hará un ciclo que se repetirá el número de expresiones que se introduzcan
                    while((linea =  origen.readLine()) != null){//con ese while se tomará los caracteres del doc y se insertarán a una cola
                        StringTokenizer cola = new StringTokenizer(linea);
                        while (cola.hasMoreTokens()) {
                            String car = cola.nextToken();
                            colita.insertar_1_c(car);   
                        }
                    System.out.println("\n");
                    colita.impr_c();    //imprime la expresión original
                    colita.pasar_polaca_inv_c();//se pasará a polaca la cola actual
                    colita.resolver();
                    colita.impr_p();    //imprime el resultado
                    colita.hacer_null();//se reiniciará la cola en la que se insertarán 
                    }
                }
            }
        }catch (IOException ex) {
            System.out.println("Error al abrir archivo");           
        }
    }
}

class lista{
    nodo_c inicio = null;//se inicializan las referencias utilizadas
    nodo_c inicio_1 = null, inicio_2 = null, fin_1 = null, fin_2 = null;
    nodo_p inicio_p = null, inicio_c2 =null;
    
    public void insertar_p(String dato){ //función para insertar a pila
        nodo_c nuevo = new nodo_c();
        nuevo.dato = dato;
        nuevo.prev = null;
        nuevo.sig = null;
        
        if(inicio == null){
            inicio = nuevo;
        }else {
            nuevo.sig = inicio;
            inicio.prev=nuevo;
            inicio = nuevo;
        }
    }
    
    public boolean checar_vacia_p(){ //función que checa si la pila está vacia
        return(inicio ==null);
    }
    
    public int asignar_valor_p(String dato){//esta función toma un operador y devuevle un número correspondiente a su valor de prioridad o precedencia
        int valor_op =0;
        switch(dato){
            case "^":
                valor_op=3;
            break;
            case "*":
                valor_op=2;
            break;
            case "/":
                valor_op=2;
            break;
            case "+":
                valor_op=1;
            break;
            case "-":
                valor_op=1;
            break;
        }
        return valor_op;
    }
    
    public void metodo_operador_p(String operador){ //esta función toma un operador como parámetro y dependiendo de su precedencia se hacen diferentes operaciones       
        int valor_op = asignar_valor_p(operador);
        int valor_pila = asignar_valor_p(inicio.dato);
        
        if("(".equals(operador)){ //si es paréntesis de apertura se inserta sin más
            insertar_p(operador);
        }else if("(".equals(inicio.dato)){//si lo que se tiene el princio de la pila es un paréntesis de apertura se insertará el operador sin importar su precedencia
            insertar_p(operador);
        }else if(valor_op==valor_pila){//si es de la misma precedencia se sacará lo que tiene la pila y se insertará a una cola y el operadores se meterá a la pila
            insertar_2_c(inicio.dato);            
            inicio = inicio.sig;
            insertar_p(operador);
        }else if(valor_op<valor_pila){ //si es de menor precedencia, sacará el elemento superior de la pila y lo meterá en una cola, posteriormente volverá a llamar a esta función
            insertar_2_c(inicio.dato);
            inicio = inicio.sig;
            if(inicio!=null&&valor_op<=asignar_valor_p(inicio.dato)){
                metodo_operador_p(operador);
            }else{
                insertar_p(operador);
            }
        } else if(valor_op>valor_pila){
            insertar_p(operador);
        }
    }
    
    public void sacar_parentesis_p(){//esta función es llamada sacará todo lo que tenga un paréntesis cuando se encuentre uno de cierre
        while(!"(".equals(inicio.dato)){
            insertar_2_c(inicio.dato);
            inicio=inicio.sig;
        }
        inicio=inicio.sig;
    }
    
    public void sacar_todo_p(){//está función sacará todos los elementos de la pila
        while(inicio!=null){
            insertar_2_c(inicio.dato);
            inicio=inicio.sig;
        }
    }
    
    public void insertar_1_c(String dato){//aquí se insertan las expresiones obtenidas
        nodo_c nuevo = new nodo_c();
        nuevo.sig = null;
        nuevo.prev = null;
        nuevo.dato = dato;
        if(inicio_1 == null){
            inicio_1 = nuevo;
            fin_1  = nuevo;
        }else{
            fin_1.sig = nuevo;
            nuevo.prev = fin_1;
            fin_1 = nuevo;
        }
    }
    
    public void insertar_2_c(String dato){ //aquí se inserta la expresión en notación polaca
        nodo_c nuevo = new nodo_c();
        nuevo.sig = null;
        nuevo.prev = null;
        nuevo.dato = dato;
        if(inicio_2 == null){
            inicio_2 = nuevo;
            fin_2  = nuevo;
        }else{
            fin_2.sig = nuevo;
            nuevo.prev = fin_2;
            fin_2 = nuevo;
            fin_2.sig = null;
        }
    }
    
    public void hacer_null(){ //hace null las referencias para que se vuelvan a insertar valores
        inicio_1 = null;
        fin_1 = null;
    }
    
    public void recorrer_c(){ //imprime la expresión en polaca
        while(inicio_2!=null){
            System.out.print(inicio_2.dato);
            inicio_2 = inicio_2.sig;
        }
        inicio_2 = null;
        fin_2 = null;
    }
    
    public void pasar_polaca_inv_c(){//este método sortea si es un número u operador y dependiendo llama a otros métodos
        if(inicio_1!=null){          
            nodo_c aux = inicio_1;
            while(aux!=null){
                if(si_operador_c(aux.dato)){
                    if(checar_vacia_p()){
                        insertar_p(aux.dato);
                    }else if(")".equals(aux.dato)){
                        sacar_parentesis_p();
                    }else {
                        metodo_operador_p(aux.dato);
                    }
                    if(aux.sig ==null){
                        sacar_todo_p();
                    }
                    aux=aux.sig;
                    
                }else{
                    insertar_2_c(aux.dato);
                    if(aux.sig ==null){
                        sacar_todo_p();
                    }
                    aux = aux.sig;
                }
            }            
        }
    }
    
    public boolean si_operador_c(String dato){ //esta función es utilizada para checar si es operador o no
        return ("+".equals(dato)||"*".equals(dato)||"-".equals(dato)||"^".equals(dato)||"/".equals(dato)||"(".equals(dato)||")".equals(dato));
    }
    
    public void impr_p(){ //se imprime el resultado
        System.out.print("= "+inicio_p.dato);
        
    }
    public void impr_c(){//se imprime la cola antes de resolver
        nodo_c aux =inicio_1 ;
        while(aux != null){
        System.out.print(aux.dato+" ");
        aux= aux.sig;
        }    
    }
    
    public int sacar_p(){//se saca de la pila, dato por dato
        if(inicio_p!=null){
            int num = inicio_p.dato;
            inicio_p = inicio_p.sig;
            return num;
        }
        return 0;
    }
    
    public void insertar_p1(int dato){//se insertan los números a la pila
        nodo_p nuevo = new nodo_p();
        nuevo.dato=dato;
        if(inicio_p == null){
            inicio_p = nuevo;
        }else{
            nuevo.sig = inicio_p;
            inicio_p = nuevo;
        }
    }
    
    public void operar(int a, int b, String car){//aquí opera ambos números dependiendo del operador
        int res;
        switch(car){
            case "+":
                 res = a+b;
                insertar_p1(res);
            break;
            case "-":
                res = b-a;
                insertar_p1(res);
            break;
            case "*":
                res = a*b;
                insertar_p1(res);
            break;
            case "/":
                res = b/a;
                insertar_p1(res);
            break;
            case "^":
                res = (int) Math.pow(b, a);
                insertar_p1(res);
            break;
            
        }
    }
    
    public void resolver(){//esta función determina si es operador, en caso de que lo sea toma dos datos de la pila y los opera dependiendo del operador
        while(inicio_2 != null){
            if("/".equals(inicio_2.dato)||"+".equals(inicio_2.dato)||"*".equals(inicio_2.dato)||"-".equals(inicio_2.dato)||"^".equals(inicio_2.dato)){
                int a = sacar_p();                     
                int b = sacar_p(); 
                operar(a, b, inicio_2.dato);
            }else{   
                int num = negativos(inicio_2.dato);
                insertar_p1(num);
            }
            inicio_2=inicio_2.sig;
        }
    }
    
    public int negativos(String num){
        
        if("-".equals(num.substring(0,1))){
            int res = Integer.parseInt(inicio_2.dato);
            
            return res;
        }else{
            return Integer.parseInt(inicio_2.dato);
        }
    }
}

class nodo_c{//la clase nodo para realizar una lista doblemente ligada o una pila o una cola
    String dato;
    nodo_c sig;
    nodo_c prev;
}

class nodo_p{ //se cra una nuevo nodo que sea tenga un dato de tipo int
    int dato;
    nodo_p sig;
}
