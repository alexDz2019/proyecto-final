package com.example.sqlitesis21b;

import java.io.Serializable;


    //para enviar objetos entre actividades(como parametro) se coloca el implements
    public class Dto implements Serializable {
        int codigo;
        String descripcion;
        double precio;

        public Dto() {
        }

        public Dto(int codigo, String descripcion, double precio) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.precio = precio;
        }


        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public double getPrecio() {
            return precio;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
        }
    }

