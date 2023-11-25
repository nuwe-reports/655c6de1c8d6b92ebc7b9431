package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.example.demo.repositories.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp(){
        formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        r1 = new Room("Dermatology");
    }

    @Test
    void shouldCreateAnAppointment() {

        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);

        Appointment saved= entityManager.persist(a1);
        assertThat(saved)
                .hasFieldOrPropertyWithValue("patient", p1)
                .hasFieldOrPropertyWithValue("doctor",d1)
                .hasFieldOrPropertyWithValue("room", r1)
                .hasFieldOrPropertyWithValue("startsAt", startsAt)
                .hasFieldOrPropertyWithValue("finishesAt", finishesAt);
    }

    @Test
    void shouldCreateSeveralAppointment() {

        LocalDateTime startsAt1= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt1 = LocalDateTime.parse("20:30 24/04/2023", formatter);

        LocalDateTime startsAt2= LocalDateTime.parse("20:30 24/04/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("21:30 24/04/2023", formatter);

        LocalDateTime startsAt3= LocalDateTime.parse("21:30 24/04/2023", formatter);
        LocalDateTime finishesAt3 = LocalDateTime.parse("22:30 24/04/2023", formatter);

        a1 = new Appointment(p1, d1, r1, startsAt1, finishesAt1);
        a2 = new Appointment(p1, d1, r1, startsAt2, finishesAt2);
        a3 = new Appointment(p1, d1, r1, startsAt3, finishesAt3);

        Appointment saved1= entityManager.persist(a1);
        Appointment saved2= entityManager.persist(a2);
        Appointment saved3= entityManager.persist(a3);

        Iterable appointments = appointmentRepository.findAll();

        assertThat(appointments).hasSize(3).contains(saved1, saved2, saved3);

    }

    @Test
    void shouldNotCreateOverlappedAppointment()  {

        appointmentRepository.deleteAll();
        LocalDateTime startsAt1= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt1 = LocalDateTime.parse("20:30 24/04/2023", formatter);

        LocalDateTime startsAt2= LocalDateTime.parse("19:50 24/04/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:50 24/04/2023", formatter);

        LocalDateTime startsAt3= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt3 = LocalDateTime.parse("20:30 24/04/2023", formatter);

        a1 = new Appointment(p1, d1, r1, startsAt1, finishesAt1);
        a2 = new Appointment(p1, d1, r1, startsAt2, finishesAt2);
        a3 = new Appointment(p1, d1, r1, startsAt3, finishesAt3);
        AtomicInteger counter= new AtomicInteger();
        List<Appointment> appointments=Arrays.asList(new Appointment[]{a1,a2,a3});
        appointments.stream().forEach((p)->{
            if(saveNextAppointment(p)){
                counter.getAndIncrement();
            }
        });

        Iterable saved = appointmentRepository.findAll();

        assertThat(saved).hasSize(counter.get()).contains(a1);

    }

    private boolean isOverlapped(Appointment appointment){
        List<Appointment> savedList=appointmentRepository.findAll();
        List<Appointment> overlapped= savedList.stream().filter(x->x.overlaps(appointment)).collect(Collectors.toList());
        if(!overlapped.isEmpty()){
            return true;
        }
        return false;
    }

    private boolean saveNextAppointment(Appointment appointment){
        if(isOverlapped(appointment)){
            return false;
        }

        entityManager.persist(appointment);
        return true;
    }




}