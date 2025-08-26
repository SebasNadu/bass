package bass.config

import bass.entities.AchievementEntity
import bass.entities.CartItemEntity
import bass.entities.MealEntity
import bass.entities.MemberEntity
import bass.entities.TagEntity
import bass.enums.CouponType
import bass.repositories.AchievementRepository
import bass.repositories.CartItemRepository
import bass.repositories.MealRepository
import bass.repositories.MemberRepository
import bass.repositories.TagRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Instant

@Configuration
@Profile("dev")
class DatabaseConfig(
    private val memberRepository: MemberRepository,
    private val mealRepository: MealRepository,
    private val cartItemRepository: CartItemRepository,
    private val tagRepository: TagRepository,
    private val achievementRepository: AchievementRepository,
) {
    @Bean
    fun databaseInitializer(): CommandLineRunner =
        CommandLineRunner {
            val tags =
                listOf(
                    TagEntity(name = "Healthy"),
                    TagEntity(name = "Vegan"),
                    TagEntity(name = "High-Protein"),
                    TagEntity(name = "Salad"),
                    TagEntity(name = "Bowl"),
                )
            val savedTags = tagRepository.saveAll(tags)

            val meals =
                listOf(
                    MealEntity(
                        name = "Salmon Poke Bowl",
                        price = 14.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1604259596863-" +
                                "57153177d40b?q=80&w=687&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Sushi rice, raw salmon, avocado, edamame, and mango.",
                    ).apply {
                        addTag(savedTags[0])
                        addTag(savedTags[4])
                    },
                    MealEntity(
                        name = "Quinoa Buddha Bowl",
                        price = 12.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1679279726937-122c49626802?" +
                                "q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=" +
                                "M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Quinoa, roasted chickpeas, sweet potato, and kale.",
                    ).apply {
                        addTag(savedTags[0])
                        addTag(savedTags[4])
                    },
                    MealEntity(
                        name = "Greek Salad",
                        price = 10.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1599021419847-" +
                                "d8a7a6aba5b4?q=80&w=979&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Feta cheese, olives, cucumber, tomatoes, and red onion.",
                    ).apply {
                        addTag(savedTags[0])
                        addTag(savedTags[1])
                        addTag(savedTags[3])
                    },
                    MealEntity(
                        name = "Lentil Salad with Goat Cheese",
                        price = 11.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://plus.unsplash.com/premium_photo-1699881082655-" +
                                "ce3f5590b380?q=80&w=1170&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Brown lentils, goat cheese, walnuts, and balsamic vinaigrette.",
                    ),
                    MealEntity(
                        name = "Chicken Caesar Salad",
                        price = 12.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1580013759032-" +
                                "c96505e24c1f?q=80&w=909&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Grilled chicken, romaine lettuce, parmesan, and croutons.",
                    ),
                    MealEntity(
                        name = "Vegan Tofu Scramble Bowl",
                        price = 11.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1631255325918-" +
                                "1ca2886508a5?q=80&w=1170&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Turmeric-spiced tofu with black beans and avocado.",
                    ),
                    MealEntity(
                        name = "Teriyaki Chicken Bowl",
                        price = 13.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1706703200723-" +
                                "822e740c7e6b?q=80&w=735&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Grilled chicken in teriyaki sauce with rice and broccoli.",
                    ),
                    MealEntity(
                        name = "Halloumi & Couscous Salad",
                        price = 12.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl =
                            "https://images.unsplash.com/photo-1673960802455-" +
                                "ec189a6207e5?q=80&w=1170&auto=format&fit=crop&ixlib=" +
                                "rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        description = "Grilled halloumi, couscous, mint, and pomegranate seeds.",
                    ),
                )

            val savedMeals = mealRepository.saveAll(meals)

            val admin =
                MemberEntity(
                    name = "sebas",
                    email = "sebas@sebas.com",
                    password = "123456",
                    role = MemberEntity.Role.ADMIN,
                )

            val customers =
                (1..10).map { i ->
                    MemberEntity(
                        name = "User $i",
                        email = "user$i@example.com",
                        password = "pass",
                        role = MemberEntity.Role.CUSTOMER,
                    )
                }

            val savedMembers = memberRepository.saveAll(listOf(admin) + customers)

            val cartItems =
                listOf(
                    CartItemEntity(
                        member = savedMembers[1],
                        meal = savedMeals[0],
                        quantity = 1,
                        addedAt = Instant.now(),
                    ),
                    CartItemEntity(
                        member = savedMembers[1],
                        meal = savedMeals[1],
                        quantity = 2,
                        addedAt = Instant.now(),
                    ),
                )

            cartItemRepository.saveAll(cartItems)

            val achievements =
                listOf(
                    AchievementEntity(
                        name = "Kickoff!",
                        description = "Kickoff! You're on fire!",
                        streaksRequired = 5,
                        imageUrl = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/bass-images/1.png",
                        couponType = CouponType.WELCOME_BONUS,
                    ),
                    AchievementEntity(
                        name = "Cruisin!",
                        description = "Cruisin! Double digits baby!",
                        streaksRequired = 10,
                        imageUrl = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/bass-images/2.png",
                        couponType = CouponType.FIRST_RANK,
                    ),
                    AchievementEntity(
                        name = "Locked in!",
                        description = "Locked in! Streak mode activated!",
                        streaksRequired = 10,
                        imageUrl = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/bass-images/3.png",
                        couponType = CouponType.SECOND_RANK,
                    ),
                    AchievementEntity(
                        name = "Trailblazer!",
                        description = "Trailblazer! Nothing can stop you!",
                        streaksRequired = 15,
                        imageUrl = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/bass-images/4.png",
                        couponType = CouponType.THIRD_RANK,
                    ),
                    AchievementEntity(
                        name = "Legend unlocked!",
                        description = "Legend unlocked! Champion status!",
                        streaksRequired = 20,
                        imageUrl = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/bass-images/5.png",
                        couponType = CouponType.FOURTH_RANK,
                    ),
                )
            achievementRepository.saveAll(achievements)
        }
}
