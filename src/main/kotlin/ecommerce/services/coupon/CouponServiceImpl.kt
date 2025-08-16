package ecommerce.services.coupon

import ecommerce.controller.coupon.usecase.ManageCouponUseCase
import ecommerce.dto.CouponDTO
import ecommerce.entities.CouponEntity
import ecommerce.exception.NotFoundException
import ecommerce.mappers.toDTO
import ecommerce.repositories.CouponRepository
import ecommerce.repositories.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
) : ManageCouponUseCase {
    override fun create(couponDTO: CouponDTO): CouponDTO {
        val member =
            memberRepository.findByIdOrNull(couponDTO.memberId)
                ?: throw NotFoundException("Member not found")
        val coupon = CouponEntity.createFrom(couponDTO.name, member)
        val savedCoupon = couponRepository.save(coupon)
        return savedCoupon.toDTO()
    }

    override fun findAll(memberId: Long): List<CouponDTO> {
        val coupons = couponRepository.findByMemberId(memberId)
        val couponDTOs = coupons.map { it.toDTO() }
        return couponDTOs
    }

    override fun validateUsability(couponId: Long): Boolean {
        val coupon =
            couponRepository.findByIdOrNull(couponId)
                ?: throw NotFoundException("Coupon not found")
        return coupon.expiresAt > LocalDateTime.now()
    }

    override fun delete(couponId: Long) {
        couponRepository.deleteById(couponId)
    }
}
