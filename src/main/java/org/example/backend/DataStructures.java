package org.example.backend;

import org.example.info.Cliente;
import org.example.info.Movimientos;
import org.example.info.Reporte;

import java.sql.Connection;
import java.util.*;

public class DataStructures {
    /**Métodos propios del desarrollador*/
    public TreeSet<Movimientos> ordenarMovimientos(List<Movimientos> movements){
        TreeSet<Movimientos> filter = new TreeSet<>((m1, m2) -> {
            int compare = m2.getFecha().compareTo(m1.getFecha());
            if (compare == 0){
                return Integer.compare(m2.getIdMovimiento(), m1.getIdMovimiento());
            }
            return compare;
        });
        filter.addAll(movements);
        return filter;
    }

    public Map<String, Double>  calculateDistribution(Cliente user, List<Movimientos> movements){
        Map<String, Double> distribution = new HashMap<>();
        double totalIncomes = user.getInitialSalary();
        double totalExpenses = 0;

        for (Movimientos mov : movements){
            if ("INGRESO".equalsIgnoreCase(mov.getTipoMovimiento())){
                totalIncomes +=mov.getMonto();
            } else if ("GASTO".equalsIgnoreCase(mov.getTipoMovimiento())){
                totalExpenses += mov.getMonto();
            }
        }

        double savings = totalIncomes - totalExpenses;
        if (savings < 0){
            savings = 0;
        }

        distribution.put("Ahorros Disponibles", savings);
        distribution.put("Gastos", totalExpenses);
        return distribution;
    }

    public Reporte savingsUser (Cliente user, List<Movimientos> movements){
        Reporte data = new Reporte();
        data.setSueldoInicial(user.getInitialSalary());
        HashMap<String, Double> total = (HashMap<String, Double>) calculateDistribution(user, movements);

        TreeMap<String, Double> filter = new TreeMap<>();
        double extraIncome = 0;
        for (Movimientos mov : movements){
            if ("GASTO".equalsIgnoreCase(mov.getTipoMovimiento())){
                String expenseCategory = mov.getCategoria();
                filter.put(expenseCategory, filter.getOrDefault(expenseCategory, 0.0) + mov.getMonto());
            } else if ("INGRESO".equalsIgnoreCase(mov.getTipoMovimiento())){
                extraIncome+= mov.getMonto();
            }
        }

        data.setTotalIngresos(extraIncome);
        data.setTotalGastos(total.get("Gastos"));
        data.setAhorroDisponible(total.get("Ahorros Disponibles"));


        int size = filter.size();
        data.categorias = new String[size];
        data.montos = new double[size];

        int index = 0;
        for(Map.Entry<String, Double> entrada : filter.entrySet()){
            data.categorias[index] = entrada.getKey();
            data.montos[index] = entrada.getValue();
            index++;
        }

        return data;
    }
}
