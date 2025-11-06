package com.devops.projeto_ac2.dto;

import com.devops.projeto_ac2.entity.User;

public class UserDTO {

    private Long id;
    private String nome;
    private String ra;

    public UserDTO() {}

    public UserDTO(Long id, String nome, String ra) {
        this.id = id;
        this.nome = nome;
        this.ra = ra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public static UserDTO fromEntity(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNome(user.getNome());

        if (user.getUser_RA() != null)
        {
            userDTO.setRa(user.getUser_RA().getRegistroMatricula());
        }

        return userDTO;
    }



}
