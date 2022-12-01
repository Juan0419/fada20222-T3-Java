package rojinegros;

import lombok.Getter;
import lombok.Setter;

import javax.naming.OperationNotSupportedException;
import java.util.LinkedList;
import java.util.Queue;

/*
    Integrantes del Equipo:
    Dahian Alexandra Sanchez (1968236)
    Juan Steban Veloza (1968025)
 */

public class ArbolRojinegro {
    @Getter
    @Setter
    private ArbolRojinegro izq;

    @Getter
    @Setter
    private ArbolRojinegro der;

    @Getter
    @Setter
    private int valor;

    @Getter
    @Setter
    private boolean black; //Si es negro True, en otro caso rojo

    public ArbolRojinegro(ArbolRojinegro izq,
                          ArbolRojinegro der,
                          int valor,
                          boolean black) {
        this.izq = izq;
        this.der = der;
        this.valor = valor;
        this.black = black;
    }

    public ArbolRojinegro() {
        this.izq = null;
        this.der = null;
        this.black = true;
    }
    /*
     * Metodos a implementar
     */

    public void insertar(int x) throws Exception {
        throw new OperationNotSupportedException();
    }

    public int maximo() throws Exception {
        if (this.getDer() != null) {
            return  this.getDer().maximo();
        } else {
            return this.valor;
        }
    }

    public int minimo() throws Exception {
        if (this.getIzq() != null){
          return this.getIzq().minimo();
        } else {
            return this.valor;
        }
    }

    public ArbolRojinegro search(int x) throws Exception {
        throw new OperationNotSupportedException();
    }

    public void rotacionIzquierda(int x) throws Exception {
        throw new OperationNotSupportedException();
    }

    public void rotacionDerecha(int nodeRotate) throws  OperationNotSupportedException {
        ArbolRojinegro nodefound = this.search(nodeRotate);
        ArbolRojinegro x = nodefound.izq;
        nodefound.izq = x.der;
        x.der = nodefound;

    }

    /*
     *  Area de pruebas, no modificar.
     */
    //Verificaciones
    /*
     * Busqueda por amplitud para verificar arbol.
     */
    public String bfs() {
        String salida = "";
        String separador = "";
        Queue<ArbolRojinegro> cola = new LinkedList<>();
        cola.add(this);
        while (cola.size() > 0) {
            ArbolRojinegro nodo = cola.poll();
            salida += separador + String.valueOf(nodo.getValor());
            separador = " ";
            if (nodo.getIzq() != null) {
                cola.add(nodo.getIzq());
            }
            if (nodo.getDer() != null) {
                cola.add(nodo.getDer());
            }
        }
        return salida;
    }

    /*
     * Recorrido inorder.
     * Verifica propiedad de orden.
     */
    public String inorden() {
        String recorrido = "";
        String separador = "";
        if (this.getIzq() != null) {
            recorrido += this.getIzq().inorden();
            separador = " ";
        }
        recorrido += separador + String.valueOf(this.getValor());
        if (this.getDer() != null) {
            recorrido += " " + this.getDer().inorden();
        }
        return recorrido;
    }

}