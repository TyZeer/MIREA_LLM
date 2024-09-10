package com.project.chat_api.config;

import com.project.chat_api.models.Role;
import com.project.chat_api.models.RoleName;
import com.project.chat_api.repos.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли данные в таблице
        if (roleRepository.count() == 0) {
            // Добавляем данные, если таблица пуста
            Role entity1 = new Role(1L, RoleName.ROLE_USER);


            roleRepository.save(entity1);
        }
    }
}

