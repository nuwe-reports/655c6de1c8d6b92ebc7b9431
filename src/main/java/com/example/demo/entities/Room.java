package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Room {

    @Id
    private String roomName;

    public Room(){
        super();
    }

    public Room( String roomName){
        super();
        this.roomName = roomName;
    }


    public String getRoomName(){
        return this.roomName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(getRoomName(), room.getRoomName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomName());
    }
}
