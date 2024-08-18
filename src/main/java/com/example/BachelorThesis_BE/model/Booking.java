package com.example.BachelorThesis_BE.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Date startDate;
    Date endDate;
    float pricePerNight;
    int noDays;
    float totalPrice = pricePerNight * noDays;

    //Testing the new branch!
    //Test again.
}
