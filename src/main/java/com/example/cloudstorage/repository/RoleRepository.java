package com.example.cloudstorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.example.cloudstorage.entity.Role;

@Service
public interface RoleRepository extends JpaRepository<Role, Long> {

}