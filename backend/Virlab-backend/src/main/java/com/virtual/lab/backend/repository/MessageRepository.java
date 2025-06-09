package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find all messages for a specific product ordered by timestamp
     * @param productId the product identifier
     * @return list of messages
     */
    List<Message> findByProductIdOrderByTimestampAsc(Long productId);

    /**
     * Find all messages between two users for a specific product
     * @param senderId the sender identifier
     * @param receiverId the receiver identifier
     * @param productId the product identifier
     * @return list of messages
     */
    List<Message> findBySenderIdAndReceiverIdAndProductIdOrderByTimestampAsc(
            Long senderId, Long receiverId, Long productId);

    /**
     * Find all messages for a specific user (either as sender or receiver)
     * @param userId the user identifier
     * @return list of messages
     */
    List<Message> findBySenderIdOrReceiverIdOrderByTimestampDesc(Long userId, Long sameUserId);

    /**
     * Count unread messages for a user
     * @param userId the user identifier
     * @param isRead the read status
     * @return count of unread messages
     */
    Long countByReceiverIdAndIsRead(Long userId, boolean isRead);
}