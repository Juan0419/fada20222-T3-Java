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

    @Getter
    @Setter
    private ArbolRojinegro root; //Nodo sin padre

    @Getter
    @Setter
    private ArbolRojinegro father; //Nodo padre

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

    public void organize (ArbolRojinegro node) throws Exception {
        ArbolRojinegro padre = node.getFather();

        // 1 Caso: La raiz
        if (padre == null) {
            // Raiz siempre negra
            node.black = true;
            return;
        }
        if (padre.isBlack()) {
            return;
        }

        // Padre rojo
        ArbolRojinegro abuelo = padre.getFather();
        ArbolRojinegro Hpadre = getHerPadre(padre);

        // Caso 3: Si el hermano del padre es rojo, hay que cambiar el color del padre, abuelo y hermano del padre
        if (Hpadre != null && !Hpadre.isBlack()) {
            padre.setBlack(true);
            abuelo.setBlack(false);
            Hpadre.setBlack(true);

            organize(abuelo);
        }

        // Si el padre es el hijo izquierdo del abuelo
        else if (padre == abuelo.getIzq()) {

            if (node == padre.getDer()) {
                rotacionIzquierda(padre.getValor());
                padre = node;
        }

        rotacionDerecha(abuelo.getValor());

        padre.setBlack(true);
        abuelo.setBlack(false);
    }

    // Si el padre es hijo derecho del abuelo
        else {
        // Caso 4,2: El tio es negro y el nodo es "hijo interno" derecho->izquierdo del
        // abuelo
        if (node == padre.getIzq()) {
            rotacionDerecha(padre.getValor());

            padre = node;
        }

        rotacionIzquierda(abuelo.getValor());

        padre.setBlack(true);
        abuelo.setBlack(false);
        }
    }

    private ArbolRojinegro getHerPadre(ArbolRojinegro nodefather) throws Exception { // Hace referencia al hermano del papa
        ArbolRojinegro abuelo = nodefather.getFather();
        if (abuelo.getIzq() == nodefather) {
            return abuelo.getDer();
        } else if (abuelo.getDer() == nodefather) {
            return abuelo.getIzq();
        } else {
            throw new Exception("Padre sin padre");
        }
    }

    public void insertar(int x) throws Exception {
        ArbolRojinegro node = root;
        ArbolRojinegro father = null;

        while (node != null) {
            father = node;
            if (x < node.getValor()) {
                node = node.getIzq();
            } else if (x > node.getValor()) {
                node = node.getDer();
            } else {
                throw new Exception("Valor " + x + "Repetido...");
            }
        }

        ArbolRojinegro newNode = new ArbolRojinegro();
        newNode.setValor(x);
        newNode.setBlack(false);
        if (father == null) {
            root = newNode;
        } else if (x < father.getValor()) {
            father.setIzq(newNode);
        } else {
            father.setDer(newNode);
        }
        newNode.setFather(father);

        organize(newNode);
        replaceRoot();
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

    public ArbolRojinegro search(int valueSearch) throws Exception {
        if(this.valor == valueSearch){
            return this;
        } else {
            if (valueSearch >= this.valor) {
                if (this.getDer() != null) {
                    return this.getDer().search(valueSearch);
                } else {
                    return null;
                }
            } else {
                if(this.getIzq() != null) {
                    return this.getIzq().search(valueSearch);
                } else {
                    return null;
                }
            }
        }
    }

    public void replaceRoot() {
        if (this.root != null) {
            this.setDer(this.root.getDer());
            this.setIzq(this.root.getIzq());
            this.setFather(this.root.getFather());
            this.setBlack(this.root.isBlack());
            this.setValor(this.root.getValor()); //Hace el cambio de raiz en caso que al momento de insertar se encuentre un numero para que sea raiz
        }
    }

    public void replace (ArbolRojinegro dad, ArbolRojinegro antChildren, ArbolRojinegro actChildren) throws OperationNotSupportedException {
        if (dad == null) {
            root = actChildren;
        } else  if (dad.der == antChildren) {
            dad.der = actChildren;
        } else if (dad.izq == antChildren) {
            dad.izq = actChildren;
        } else {
            throw new OperationNotSupportedException();
        }

        if (actChildren != null) {
            actChildren.setFather(father);
        }
    }

    public void rotacionIzquierda(int x) throws Exception {
        ArbolRojinegro node = search(x); //Node
        ArbolRojinegro father = node.getFather(); //Padre
        ArbolRojinegro rightChildren = node.getDer(); //Hijo derecho


        node.der = rightChildren.izq;
        if (rightChildren.izq != null) {
            rightChildren.izq.setFather(node);
        }

        rightChildren.izq = node;
        node.setFather(rightChildren);

        replace(father, node, rightChildren);
        replaceRoot();
    }

    public void rotacionDerecha(int x) throws  Exception {
        ArbolRojinegro node = this.search(x); //Nodo
        ArbolRojinegro father = node.getFather(); //Padre
        ArbolRojinegro leftChildren = node.getIzq(); //Hijo derecho

        node.izq = leftChildren.der;
        if (leftChildren.der != null) {
            leftChildren.der.setFather(node);
        }
        leftChildren.setDer(node);
        node.setFather(leftChildren);

        replace(father, node, leftChildren);
        replaceRoot();
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