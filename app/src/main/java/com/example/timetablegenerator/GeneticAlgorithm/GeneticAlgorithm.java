package com.example.timetablegenerator.GeneticAlgorithm;

import android.content.Context;

import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {

    Context c;
    public GeneticAlgorithm(Context c) {
        this.c = c;
    }

    public Population evolve(Population population){
        return mutate_population(crossover_population(population));
    }

    /*
    * crossover population takes two schedules and crosses it and makes a group of schedules/population from the crosses
    * */
    public Population crossover_population(Population pop){
        Population crossoverPop = new Population(0, c);
        for(int i=0;i<new Variables().NUMB_OF_ELITE_SCHEDULES; i++){
            crossoverPop.getSchedules().add(pop.getSchedules().get(i));
        }

        int i = new Variables().NUMB_OF_ELITE_SCHEDULES;
        while (i<new Variables().POPULATION_SIZE){
            Schedule schedule1 = select_tournament_population(pop).getSchedules().get(0);
            Schedule schedule2 = select_tournament_population(pop).getSchedules().get(0);
            crossoverPop.getSchedules().add(crossover_schedule(schedule1, schedule2));
            i+=1;
        }
        return crossoverPop;
    }

    /*
    * generates a population of mutated schedules
    * */
    public Population mutate_population(Population population){
        for(int i=new Variables().NUMB_OF_ELITE_SCHEDULES; i<new Variables().POPULATION_SIZE; i++){
            this.mutate_schedule(population.getSchedules().get(i));
        }
        return population;
    }

    /*
    *
    * crossover schedule takes two schedules and changes the gene of the variable crossover schedule at a specific index
    * it changes the index position value by getting the same index value from either of the two schedules
    *
    */
    public Schedule crossover_schedule(Schedule schedule1, Schedule schedule2){
        Schedule crossoverSchedule = new Schedule(c).initialize();
        for(int i=0; i<crossoverSchedule.getClasses().size(); i++){
            if(Math.random()>0.5){
                crossoverSchedule.getClasses().set(i, schedule1.getClasses().get(i));
            }
            else {
                crossoverSchedule.getClasses().set(i, schedule2.getClasses().get(i));
            }
        }
        return crossoverSchedule;
    }

    /*
     * takes one schedule and changes the value at an index of the variable mutateSchedule by setting the value of the same index of schedule
     * */
    public Schedule mutate_schedule(Schedule mutateSchedule){
        Schedule schedule = new Schedule(c).initialize();
        for(int i=0; i<mutateSchedule.getClasses().size(); i++){
            if(new Variables().MUTATION_RATE > Math.random()) {
                mutateSchedule.getClasses().set(i, schedule.getClasses().get(i));
            }
        }
        return mutateSchedule;
    }

    /*
    * select tournament population gets random schedules from the population
    * */
    public Population select_tournament_population(Population pop){
        Population tournamentPop = new Population(0, c);
        int i = 0;
        while(i<new Variables().TOURNAMENT_SELECTION_SIZE){
            Random random = new Random();
            int randomPopSize = random.nextInt(new Variables().POPULATION_SIZE);

            tournamentPop.getSchedules().add(pop.getSchedules().get(randomPopSize));
            i +=1;
        }
        tournamentPop.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
        return tournamentPop;
    }
}
