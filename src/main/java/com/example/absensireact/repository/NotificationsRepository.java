package com.example.absensireact.repository;


import com.example.absensireact.model.Notifcations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifcations , Long> {
}
