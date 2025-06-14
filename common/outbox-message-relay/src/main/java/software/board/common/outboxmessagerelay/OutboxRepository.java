package software.board.common.outboxmessagerelay;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
	
	// 아직 카프카로 전송되지 않은 이벤트들에 대해 .. 주기적으로 polling 해서 전송할때 쓰는 메서드
	List<Outbox> findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
		Long shardKey,
		LocalDateTime from,
		Pageable pageable
	);
}
