package bass.services.coupon

import bass.controller.coupon.usecase.ManageCouponUseCase
import bass.dto.coupon.CouponDTO
import bass.exception.NotFoundException
import bass.mappers.toDTO
import bass.repositories.CouponRepository
import bass.repositories.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
) : ManageCouponUseCase {
    override fun findAll(memberId: Long): List<CouponDTO> {
        val coupons = couponRepository.findByMemberId(memberId)
        val couponDTOs = coupons.map { it.toDTO() }
        return couponDTOs
    }

    override fun validateUsability(couponId: Long): Boolean {
        val coupon =
            couponRepository.findByIdOrNull(couponId)
                ?: throw NotFoundException("Coupon not found")
        return coupon.expiresAt > Instant.now()
    }

    override fun delete(couponId: Long) {
        couponRepository.deleteById(couponId)
    }
}
