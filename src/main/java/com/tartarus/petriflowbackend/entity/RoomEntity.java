package com.tartarus.petriflowbackend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Room")
public class RoomEntity implements Serializable {

    private Long id;

    private String urlPassword;

    private byte[] data;

    public RoomEntity() {
    }

    public RoomEntity(Long id, String urlPassword, byte[] data) {
        this.id = id;
        this.urlPassword = urlPassword;
        this.data = data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    //    @Column(unique = true)
    public String getUrlPassword() {
        return urlPassword;
    }

    public void setUrlPassword(String password) {
        this.urlPassword = password;
    }

    @Lob
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] dataXML) {
        this.data = dataXML;
    }
}
