package com.devops.projeto_ac2;

import com.devops.projeto_ac2.entity.User;
import com.devops.projeto_ac2.entity.User_RA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class ProjetoAc2Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoAc2Application.class, args);

        User_RA raAna = new User_RA("543210");

        User user1 = new User();
        user1.setId(1L);
        user1.setNome("Ana Silva");
        user1.setUser_RA(raAna);

        User_RA raBruno = new User_RA("987654");

        User user2 = new User();
        user2.setId(2L);
        user2.setNome("Bruno Mendes");
        user2.setUser_RA(raBruno);

        User_RA raCarla = new User_RA("101010");

        User user3 = new User();
        user3.setId(3L);
        user3.setNome("Carla Santos");
        user3.setUser_RA(raCarla);
	}

    @RequestMapping ("/test")
    @ResponseBody
    String home()
    {
        return "Hello World";
    }
}
