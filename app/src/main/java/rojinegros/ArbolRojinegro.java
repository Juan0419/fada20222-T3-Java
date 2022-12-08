package rojinegros;

import lombok.Getter;
import lombok.Setter;

import javax.naming.OperationNotSupportedException;
import java.util.LinkedList;
import java.util.Queue;

/*
    Integrantes del Equipo:
    Juan Steban Veloza (1968025)
    Dahian Alexandra Sanchez (1968236)
    Isabella Marin
    Eduardo Paez
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
            node.setBlack(true);
            return;
        }
        if (padre.isBlack()) {
            return;
        }

        // Padre rojo
        ArbolRojinegro abuelo = padre.getFather();

        // Caso 2: No existe padre del padre (abuelo), por ende el padre es la raiz
        if (abuelo == null) {
            padre.setBlack(true);
            return;
        }
        ArbolRojinegro tio = getTio(padre);

        //Caso 3: Si el hermano del padre es rojo, hay que cambiar el color del padre, abuelo y hermano del padre
        if (tio != null && !tio.isBlack()) {
            padre.setBlack(true);
            abuelo.setBlack(false);
            tio.setBlack(true);
        } else if (padre == abuelo.getIzq()){

            if (node == padre.getDer()) {
                rotacionIzquierda(padre.getValor());
                padre = node;
            }

            rotacionDerecha(abuelo.getValor());

            padre.setBlack(true);
            abuelo.setBlack(false);
        } else {
            if (node ==padre.getIzq()) {
                rotacionDerecha(padre.getValor());
                padre = node;
            }
            rotacionIzquierda(abuelo.getValor());
            padre.setBlack(true);
            abuelo.setBlack(false);
        }
    }

    private ArbolRojinegro getTio(ArbolRojinegro nodefather) throws Exception { // Hace referencia al hermano del papa
        ArbolRojinegro abuelo = nodefather.getFather();
        if (abuelo.getIzq() == nodefather) {
            return abuelo.getDer();
        } else if (abuelo.getDer() == nodefather) {
            return abuelo.getIzq();
        } else {
            throw new Exception("Padre sin padre");
        }
    }

    public void replaceRoot() {
        if (this.root != null) {
            this.setDer(this.root.getDer());
            this.setIzq(this.root.getIzq());
            this.setFather(this.root.getFather());
        }
    }
    //METEODO INSERTAR NO FUNCIONAL DEL TODO NO PASA PRUEBA
    public void insertar(int x) throws Exception {
        if (this.root == null) {
            this.valor = x;
            this.black = true;
            this.root = this;
        } else {
            ArbolRojinegro node = this;
            ArbolRojinegro dad = null;

            while (node != null) {
                dad = node;
                if (x < node.getValor()) {
                    node = node.getIzq();
                } else {
                    node = node.getDer();
                }
            }
            ArbolRojinegro newnode = new ArbolRojinegro();
            newnode.setValor(x);
            newnode.setBlack(false);
            if (x < dad.getValor()) {
                dad.setIzq(newnode);
            } else {
                dad.setDer(newnode);
            }
            newnode.setFather(dad);
            organize(newnode);
        }
    }


    // PASAN PRUEBAS COMPLETAMENTE
    public int maximo() throws Exception {
        if (this.getDer() != null) {
            return this.getDer().maximo();
        } else {
            return this.valor;
        }
    }

    // PASAN PRUEBAS COMPLETAMENTE
    public int minimo() throws Exception {
        if (this.getIzq() != null) {
            return this.getIzq().minimo();
        } else {
            return this.valor;
        }
    }

    // PASAN PRUEBAS COMPLETAMENTE
    public ArbolRojinegro search(int valueSearch) throws Exception {
        if (this.valor == valueSearch) {
            return this;
        } else {
            if (valueSearch >= this.valor) {
                if (this.getDer() != null) {
                    return this.getDer().search(valueSearch);
                } else {
                    return null;
                }
            } else {
                if (this.getIzq() != null) {
                    return this.getIzq().search(valueSearch);
                } else {
                    return null;

                }
            }
        }
    }
    // Implementacion de Rotaciones

    public ArbolRojinegro root() { //Raiz de forma conceptual
        if (father == null) {
            return this;
        } else {
            return father.root();
        }
    }

    public void replaceIzq() { // Reemplazo de ramales izquierdos en el momento de la rotacion
        ArbolRojinegro raiz = this.root();

        ArbolRojinegro fatherI = this.father;
        ArbolRojinegro izqI = this.izq;
        ArbolRojinegro derI = this.der;
        int valueI = this.valor;

        this.father = null;
        this.izq = raiz;
        this.der = raiz.getDer();
        this.valor = raiz.getValor();
        //this.black = raiz.getBlack();

        raiz.setFather(fatherI);
        raiz.setIzq(izqI);
        raiz.setDer(derI);
        raiz.setValor(valueI);
    }

    public void replaceDer() { // Reemplazo de ramales derechos en el momento de la rotacion
        ArbolRojinegro raiz = this.root();

        ArbolRojinegro fatherD = this.father;
        ArbolRojinegro izqD = this.izq;
        ArbolRojinegro derD = this.der;
        int valueD = this.valor;

        this.father = null;
        this.izq = raiz.getIzq();
        this.der = raiz;
        this.valor = raiz.getValor();

        raiz.setFather(fatherD);
        raiz.setIzq(izqD);
        raiz.setDer(derD);
        raiz.setValor(valueD);
    }

    // PASAN PRUEBAS COMPLETAMENTE
    public void rotacionIzquierda(int nodeRotate) throws Exception {
        if (this.valor == nodeRotate) {
            ArbolRojinegro nodDer = this.der;
            this.der = this.der.getIzq();
            nodDer.setIzq(this);
            nodDer.setFather(this.father);

            if (this.father != null) {
                if (this.father.getIzq() == this) {
                    this.father.setIzq(nodDer);
                } else {
                    this.father.setDer(nodDer);
                }
                this.father = nodDer;
            } else {
                this.father = nodDer;
                replaceIzq();
            }

        } else if (this.valor < nodeRotate && this.der != null) {
            this.der.rotacionIzquierda(nodeRotate);
        } else if (this.valor > nodeRotate && this.izq != null) {
            this.izq.rotacionIzquierda(nodeRotate);
        }
    }

    // PASAN PRUEBAS COMPLETAMENTE
    public void rotacionDerecha(int nodeRotate) throws Exception {
        if (this.valor == nodeRotate) {
            ArbolRojinegro nodIzq = this.izq;
            this.izq = this.izq.getDer();
            nodIzq.setDer(this);
            nodIzq.setFather(this.father);

            if (this.father != null) {
                if (this.father.getDer() == this) {
                    this.father.setDer(nodIzq);
                } else {
                    this.father.setIzq(nodIzq);
                }
                this.father = nodIzq;
            } else {
                this.father = nodIzq;
                replaceDer();
            }

        } else if (this.valor < nodeRotate && this.izq != null) {
            this.izq.rotacionDerecha(nodeRotate);
        } else if (this.valor > nodeRotate && this.der != null) {
            this.der.rotacionDerecha(nodeRotate);
        }
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