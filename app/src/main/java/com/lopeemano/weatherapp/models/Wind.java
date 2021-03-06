package com.lopeemano.weatherapp.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lope on 6/3/15.
 */
public class Wind {

    private Double speed;
    private Double deg;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The speed
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * @param speed The speed
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}