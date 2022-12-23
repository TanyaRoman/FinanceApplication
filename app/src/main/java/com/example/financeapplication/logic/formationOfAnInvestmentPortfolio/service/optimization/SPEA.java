package com.example.financeapplication.logic.formationOfAnInvestmentPortfolio.service.optimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SPEA implements Optimization {

    List<Double> expectedReturn;
    List<List<Double>> cov;

    Integer archiveSize = 20; // максимальное число решений в архиве
    Integer populationSize = 100; // размер популяции
    Double mutation = 0.4; // вероятность мутации
    Double crossing = 0.8; // вероятность скрещивания
    Integer generationCount = 40; // число поколений
    Integer maxPriority = 50; // максимальное значение гена
    Double maxReturn;

    List<List<Integer>> population;

    public SPEA(List<Double> expectedReturn, List<List<Double>> cov) {
        this.expectedReturn = expectedReturn;
        this.cov = cov;
        this.population = generatePopulation();
        this.maxReturn = findMax(expectedReturn);
    }

    //    coin_returns - вектор ожидаемых доходностей активов
//    coin_covs - матрица ковариаций
    public List<List<Double>> count (){
        List<List<Integer>> archive = new ArrayList<>();
        int generation = 0;
//        System.out.println("population: " + population);

        while (generation < generationCount){

//            Добавляем недоминируемые решения из популяции в архив
            List<List<Integer>> nondom = nondominated(population);
            for (List<Integer> list: nondom) {
                archive.add(list);
            }
            System.out.println("archive_add: " + archive);

//            Оставляем в архиве только недоминируемые
            archive = nondominated(archive);
            System.out.println("archive_nondominate: " + archive);

//            Если размер архива превышен - урезаем кластеризацией
            if (archive.size() > archiveSize) {
                archive = shrinkArchive(archive);
            }

//            селекция
            List<List<Double>> fitnesses = new ArrayList<>();
            List<List<Double>> archiveFitnesses = new ArrayList<>();
            for (List<Integer> list: population) {
                fitnesses.add(fitness(decode(list)));  // считает приспособленность вида [return, risk] для каждого значения
            }
//            System.out.println("fitnesses");
//            System.out.println(fitnesses);
//            System.out.println();

            for (List<Integer> list: archive) {
                archiveFitnesses.add(fitness(decode(list)));
            }
//            System.out.println("archiveFitnesses");
//            System.out.println(archiveFitnesses);
//            System.out.println();

            population.addAll(archive);
//            System.out.println("population.addAll(archive)");
//            System.out.println(population);
//            System.out.println();
//            List<List<Double>> strength = calculateStrength(fitnesses, archiveFitnesses);

            List<Double> ranks = calculateStrength(fitnesses, archiveFitnesses);
//            System.out.println("ranks");
//            System.out.println(ranks);
//            System.out.println();

//            List<Double> ranks = new ArrayList<>();
//            for (List<Double> list: strength) {
//                ranks.add(list.get(0)+list.get(1));
//            }

            List<List<Integer>> p = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                p.add(tournament(population, ranks));
            }
            population = p;
//            System.out.println("population_after_tournament");
//            System.out.println(population);
//            System.out.println();

//            скрещивание
            for (int i = 1; i < population.size(); i += 2) {
                if (Math.random() < crossing){
                    List<List<Integer>> list = crossover(population.get(i-1), population.get(i));
                    population.set(i-1, list.get(0));
                    population.set(i, list.get(1));
                }
            }
//            System.out.println("crossover -------------/");
//            System.out.println("population_crossover");
//            System.out.println(population);

//            мутация
            for (int i = 1; i < population.size(); i += 2) {
                if (Math.random() < mutation) {
                    List<Integer> list = mutate(population.get(i));
                    population.set(i, list);
                }
            }
//            System.out.println("mutation------------------/");
//            System.out.println("population_mutation");
//            System.out.println(population);

            generation += 1;
        }
        List<List<Double>> res = new ArrayList<>();
        for (List<Integer> list: archive) {
            res.add(decode(list));
        }
        return res;
    }

    //    Генерация случайной популяции
    private List<List<Integer>> generatePopulation(){
        List<List<Integer>> p = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> pp = new ArrayList<>();
            for (int j = 0; j < expectedReturn.size(); j++) {
                pp.add(random.nextInt(maxPriority));
            }
            p.add(pp);
        }
        return p;
    }

    private Double findMax(List<Double> list){
        Double max = list.get(0);
        for (Double d: list) {
            max = Math.max(max, d);
        }
        return max;
    }

    //    Получение списка недоминируемых решений из популяции
    private List<List<Integer>> nondominated(List<List<Integer>> population){

        List<List<Double>> fitnesses = new ArrayList<>();
        for (List<Integer> list: population) {
            fitnesses.add(fitness(decode(list)));
        }

        List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < population.size(); i++) {
            Boolean dominated = false;
            for (int j = 0; j < population.size(); j++) {
                if ((i != j)&&(dominates(fitnesses.get(j), fitnesses.get(i)))){
                    dominated = true;
                    break;
                }
            }
            if (!dominated){
                res.add(population.get(i));
            }
        }
        return res;
    }

    //    Определение вектора приспособленности декодированного решения
    private List<Double> fitness(List<Double> decodedSolution){

        Integer count = decodedSolution.size();

        Double sum = Double.valueOf(0);
        for (int i = 0; i < count; i++) {
            sum += decodedSolution.get(i)*expectedReturn.get(i);
        }
        Double returnFithess = maxReturn - sum;

        Double riskFitness = Double.valueOf(0);
        sum = Double.valueOf(0);
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                sum += cov.get(i).get(j) * decodedSolution.get(i) * decodedSolution.get(j);
            }
        }
        riskFitness = sum;
        List<Double> list = new ArrayList<>();
        list.add(returnFithess);
        list.add(riskFitness);
        return list;
    }

    //    Декодирование решения
//    Примеры:
//      (5, 5, 5) => (0.33, 0.33, 0.33)
//      (1, 3, 4) => (0.125, 0.375, 0.5)
    private List<Double> decode(List<Integer> solution){
        int total = 0;
        for (Integer i: solution) {
            total += i;
        }
        List<Double> list = new ArrayList<>();
        if (total != 0){
            for (Integer i: solution) {
                list.add( (double)i/(double)total);
            }
        } else {
            for (Integer i: solution) {
                list.add((double)1/(double)solution.size());
            }
        }
        return list;
    }

    //    Проверка, что вектор приспособленности a доминирует над b
    private Boolean dominates(List<Double> a, List<Double> b){
        for (int i = 0; i < a.size(); i++) {
            if (b.get(i) < a.get(i)){
                return false;
            }
        }
        return true;
    }

    //    Уменьшение размера архива
    private List<List<Integer>> shrinkArchive (List<List<Integer>> archive){

        List<List<List<Integer>>> clusters = new ArrayList<>();
        for (List<Integer> list: archive) {
            List<List<Integer>> l = new ArrayList<>();
            l.add(list);
            clusters.add(l);
        }

        while (clusters.size() > archiveSize) {
            Double minDistance = Double.valueOf(1000000);
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < clusters.size()-1; i++) {
                for (int j = i+1; j < clusters.size(); j++) {
                    Double d = clusterDistance(clusters.get(i), clusters.get(j));
                    if (d < minDistance){
                        minDistance = d;
                        indexes.clear();
                        indexes.add(i);
                        indexes.add(j);
                    }
                }
            }
//            Два минимально отстоящих кластера сливаем в один
            List<List<Integer>> newCluster = new ArrayList<>();
            for (int i = 0; i < clusters.size(); i++) {
                if ((i == indexes.get(0))||(i == indexes.get(1))){
                    for (List<Integer> list: clusters.get(i)) {
                        newCluster.add(list);
                    }
                }
            }
            List<List<List<Integer>>> newClusters = new ArrayList<>();
            for (int i = 0; i < clusters.size(); i++) {
                if (i == indexes.get(0)){
                    continue;
                } else if (i == indexes.get(1)){
                    continue;
                }
                newClusters.add(clusters.get(i));
            }
            newClusters.add(newCluster);
            clusters = newClusters;
        }

//        Оставляем из кластеров только их центроиды (решения,
//        расположенные ближе к центру)
        List<List<Integer>> res = new ArrayList<>();
        for (List<List<Integer>> cluster: clusters) {
            if (cluster.size() == 1){
                res.add(cluster.get(0));
            } else {
                int size = cluster.size();
                int dims = cluster.get(0).size();
                List<List<Double>> decodedCluster = new ArrayList<>();
                for (List<Integer> list: cluster) {
                    decodedCluster.add(decode(list));
                }
                List<Double> avg = new ArrayList<>();
                for (int i = 0; i < dims; i++) {
                    Double sum = Double.valueOf(0);
                    for (int j = 0; j < size; j++) {
                        sum += decodedCluster.get(j).get(i);
                    }
                    avg.add(sum/(double) size);
                }

                Double min = Double.valueOf(100000);
                int index = 0;
                for (int i = 0; i < decodedCluster.size(); i++) {

                    if (euclid(decodedCluster.get(i), avg) < min){
                        min = euclid(decodedCluster.get(i), avg);
                        index = i;
                    }
                }
                res.add(cluster.get(index));
            }
        }
        return res;
    }

    //    Расстояние между кластерами решений
    private Double clusterDistance(List<List<Integer>> c1, List<List<Integer>> c2){
        Double res = Double.valueOf(0);
        for (List<Integer> i: c1) {
            List<Double> a = decode(i);
            for (List<Integer> j: c2) {
                List<Double> b = decode(j);
                res += euclid(a, b);
            }
        }
        return res / (double) (c1.size() * c2.size());
    }

    //    Эвклидово расстояние между точками
    private Double euclid(List<Double> a, List<Double> b){
        Double d = Double.valueOf(0);
        for (int i = 0; i < a.size(); i++) {
            d += Math.pow(a.get(i) - b.get(i), 2);
        }
        return Math.pow(d, (double) 1/2);
    }

    //    Вычисление параметров "силы" для векторов приспособленности особей
//    популяции (fitnesses) и архива (archive_fitnesses)
    private List<Double> calculateStrength (List<List<Double>> fitnesses, List<List<Double>> archiveFitnesses){

        List<Double> archiveStrengths = new ArrayList<>();
        List<Double> strengths = new ArrayList<>();
        List<Double> archive_strengths = new ArrayList<>();
        for (int i = 0; i < fitnesses.size(); i++) {
            strengths.add(1.0);
        }

        for (List<Double> archiveItem: archiveFitnesses) {
            List<Integer> dominated = new ArrayList<>();  // индексы доминирующих значений
            for (int i = 0; i < fitnesses.size(); i++) {
                if (dominates(archiveItem, fitnesses.get(i))){
                    dominated.add(i);
                }
            }
            Double strength = (double)dominated.size() / (double) (1 + fitnesses.size());

            archive_strengths.add(strength);

            for (int i = 0; i < dominated.size(); i++) {
                strengths.add(i, 1 + strength);
            }
        }

        for (Double d:strengths) {
            archiveStrengths.add(d);
        }
        for (Double d:archive_strengths) {
            archiveStrengths.add(d);
        }
        return archiveStrengths;
    }

    //    Турнирная селекция
    private List<Integer> tournament(List<List<Integer>> population, List<Double> ranks){
        int size = 2;
        List<Integer> indexes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            indexes.add(random.nextInt(population.size() - 1));
        }
        Double min = Double.valueOf(1000000);
        int index = 0;
        for (int i = 0; i < indexes.size(); i++) {
            if (ranks.get(i) < min){
                min = ranks.get(i);
                index = i;
            }
        }
        return population.get(indexes.get(index));
    }

    //    Одноточечное скрещивание
    private List<List<Integer>> crossover(List<Integer> a, List<Integer> b){
        Random random = new Random();
        Integer p = random.nextInt(a.size() - 1);
        List<Integer> child1 = new ArrayList<>();
        List<Integer> child2 = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (i < p){
                child1.add(a.get(i));
                child2.add(b.get(i));
            } else {
                child1.add(b.get(i));
                child2.add(a.get(i));
            }
        }
        List<List<Integer>> list = new ArrayList<>();
        list.add(child1);
        list.add(child2);
        return list;
    }

    //    Мутация
    private List<Integer> mutate(List<Integer> solution){
        Random random = new Random();
        Integer i = random.nextInt(solution.size() - 1);
        Integer change = 0;
        if (solution.get(i).equals(maxPriority)){
            change = -1;
        } else if (solution.get(i).equals(0)){
            change = 1;
        } else {
            if (Math.random() < 0.5){
                change = 1;
            } else {
                change = -1;
            }
        }
        Integer s = solution.get(i);
        solution.set(i, s + change);
        return solution;
    }
}
