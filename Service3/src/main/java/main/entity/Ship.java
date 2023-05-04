package main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDateTime;
import java.util.Random;

public class Ship
{
    public enum Type_of_cargo
    {
        BULK,
        LIQUID,
        CONTAINER;

        public static Type_of_cargo getRandomType()
        {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    private String name_;
    private LocalDateTime arrivalTime_;
    private LocalDateTime modelingArrivalTime_;
    private LocalDateTime startDateTime_;
    private LocalDateTime realArrivalTime_;

    private Type_of_cargo type_;
    private int weight_;

    private int unloadMinutes_;
    private int delayUnload_;
    private double remainingWeight_;
    private double penalty_;

    public void setName(String name) {
        this.name_ = name;
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime_ = arrivalTime;
    }

    public void setType(Type_of_cargo type) {
        this.type_ = type;
    }
    public void setWeight(int weight) {
        this.weight_ = weight;
    }

    @JsonIgnore
    public void setModelingArrivalTime(LocalDateTime realArrivalTime) {
        this.modelingArrivalTime_ = realArrivalTime;
    }
    @JsonIgnore
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime_ = startDateTime;
    }

    public void setUnloadMinutes(int unloadMinutes) {
        this.unloadMinutes_ = unloadMinutes;
    }

    public void setDelayUnload(int delayUnload) {
        this.delayUnload_ = delayUnload;
    }

    public void setRemainingWeight(double remainingWeight) {
        this.remainingWeight_ = remainingWeight;
    }

    public void setPenalty(double penalty) {
        this.penalty_ = penalty;
    }

    @JsonIgnore
    public void setRealArrivalTime(LocalDateTime realArrivalTime_) {
        this.realArrivalTime_ = realArrivalTime_;
    }

    @JsonIgnore
    public LocalDateTime getRealArrivalTime() {
        return realArrivalTime_;
    }

    public String getName() {
        return name_;
    }

    public LocalDateTime getModelingArrivalTime()
    {
        return modelingArrivalTime_;
    }

    public LocalDateTime getArrivalTime()
    {
        return arrivalTime_;
    }

    public LocalDateTime getStartDateTime()
    {
        return startDateTime_;
    }

    public Type_of_cargo getType()
    {
        return type_;
    }

    public int getWeight()
    {
        return weight_;
    }

    public int getUnloadMinutes()
    {
        return unloadMinutes_;
    }

    public int getDelayUnload()
    {
        return delayUnload_;
    }

    public double getPenalty()
    {
        return penalty_;
    }

    public double getRemainingWeight()
    {
        return remainingWeight_;
    }

    @JsonIgnore
    public LocalDateTime getTime(){
        return  arrivalTime_;
    }

    public Ship(String name, LocalDateTime time, Type_of_cargo type, int weight)
    {
        name_ = name;
        arrivalTime_ = time;
        modelingArrivalTime_ = time;
        weight_ = weight;
        type_ = type;
    }

    public Ship()
    {
        name_ = "";
        weight_ = 0;
        type_ = Type_of_cargo.BULK;
    }

}
