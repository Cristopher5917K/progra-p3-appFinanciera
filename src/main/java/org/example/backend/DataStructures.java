package org.example.backend;

import org.example.info.Cliente;
import org.example.info.Movimientos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
            } else if ("GASTOS".equalsIgnoreCase(mov.getTipoMovimiento())){
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

    
}
