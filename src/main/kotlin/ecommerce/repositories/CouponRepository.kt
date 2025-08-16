package ecommerce.repositories

import ecommerce.entities.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<CouponEntity, Long> {
    fun findByMemberId(memberId: Long): List<CouponEntity>
}
