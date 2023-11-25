package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;



/** TODO
 * Implement all the unit test in its corresponding class.
 * Make sure to be as exhaustive as possible. Coverage is checked ;)
 */

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateADoctor() throws Exception{

        Doctor doctor = getDoctor();

        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());

    }

    private static Doctor getDoctor() {
        return new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

    }

    @Test
    void shouldNotFindDoctorGetAll() throws Exception{

        when(doctorRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());

    }
    @Test
    void shouldFindAlmostOneDoctor() throws Exception{

        Doctor doctor = getDoctor();
        List<Doctor> doctors=new ArrayList<>();
        doctors.add(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void doctorNotFoundById() throws Exception{
        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isNotFound());
    }

    @Test void doctorFoundById() throws Exception{
        Long value=1L;
        Doctor doctor = getDoctor();
        doctor.setId(value);

        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/doctors/"+value))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(value));

    }

    @Test
    void deleteANotFoundDoctor() throws Exception{

        long id = 31;
        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteADoctorSuccess() throws Exception {
        Long value=1L;
        Doctor doctor = getDoctor();
        doctor.setId(value);
        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.of(doctor));

        mockMvc.perform(delete("/api/doctors/" + value))
                .andExpect(status().isOk());

    }

    @Test void deleteAllDoctors() throws Exception{
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }


}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAPatient() throws Exception{

        Patient patient = getPatient();

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());

    }

    private static Patient getPatient() {
        return new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
    }

    @Test
    void shouldNotFindPatientGetAll() throws Exception{

        when(patientRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());

    }
    @Test
    void shouldFindAlmostOneDoctor() throws Exception{

        Patient patient = getPatient();
        List<Patient> patients=new ArrayList<>();
        patients.add(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void patientsNotFoundById() throws Exception{
        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isNotFound());
    }

    @Test void patientsFoundById() throws Exception{
        Long value=1L;
        Patient patient = getPatient();
        patient.setId(value);

        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/"+value))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(value));

    }

    @Test
    void deleteANotFoundDoctor() throws Exception{

        long id = 31;
        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/patients/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteADoctorSuccess() throws Exception {
        Long value=1L;
        Patient patient = getPatient();
        patient.setId(value);
        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patient));

        mockMvc.perform(delete("/api/patients/" + value))
                .andExpect(status().isOk());

    }

    @Test void deleteAllDoctors() throws Exception{
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateARoom() throws Exception{

        Room room = getRoom();

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());

    }

    private static Room getRoom() {
        return new Room("Dermatology");
    }

    @Test
    void shouldNotRoomsGetAll() throws Exception{

        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isNoContent());

    }
    @Test
    void shouldFindAlmostOneRoom() throws Exception{

        Room room = getRoom();
        List<Room> rooms=new ArrayList<>();
        rooms.add(room);
        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void roomsNotFoundByName() throws Exception{
        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/Oncologia"))
                .andExpect(status().isNotFound());
    }

    @Test void patientsRoomsName() throws Exception{
        Room room = getRoom();


        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/"+room.getRoomName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomName").value(room.getRoomName()));

    }

    @Test
    void deleteANotFoundDoctor() throws Exception{

        long id = 31;
        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/rooms/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteADoctorSuccess() throws Exception {

        Room room = getRoom();

        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.of(room));

        mockMvc.perform(delete("/api/rooms/" + room.getRoomName()))
                .andExpect(status().isOk());

    }

    @Test void deleteAllDoctors() throws Exception{
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }


}