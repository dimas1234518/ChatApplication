package net.zhevakin.repository;

import net.zhevakin.models.Chat;
import net.zhevakin.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


      @Query("SELECT m FROM Message m where m.chat=:chat ORDER BY m.dateSend Desc")
    List<Message> findTopByChatsIs(Pageable pageable, Chat chat);

}
