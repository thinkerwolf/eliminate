package com.thinkerwolf.eliminate.chat.repository;

import com.thinkerwolf.eliminate.chat.entity.Chat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends PagingAndSortingRepository<Chat, String> {

}
