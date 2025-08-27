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
                    TagEntity(name = "Keto"),
                    TagEntity(name = "Low-Carb"),
                    TagEntity(name = "Spicy"),
                    TagEntity(name = "Asian"),
                    TagEntity(name = "Mediterranean"),
                    TagEntity(name = "Quick"),
                    TagEntity(name = "Comfort"),
                    TagEntity(name = "Seasonal"),
                    TagEntity(name = "Breakfast"),
                    TagEntity(name = "Dinner"),
                )
            val savedTags = tagRepository.saveAll(tags)

            val meals =
                listOf(
                    MealEntity(
                        name = "Salmon Poke Bowl",
                        price = 14.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1604259596863-57153177d40b",
                        description = "Sushi rice, raw salmon, avocado, edamame, and mango.",
                    ).apply {
                        addTag(savedTags[0])
                        addTag(savedTags[4])
                    },
                    MealEntity(
                        name = "Quinoa Buddha Bowl",
                        price = 12.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1679279726937-122c49626802",
                        description = "Quinoa, roasted chickpeas, sweet potato, and kale.",
                    ).apply {
                        addTag(savedTags[0])
                        addTag(savedTags[1])
                    },
                    MealEntity(
                        name = "Greek Salad",
                        price = 10.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1599021419847-d8a7a6aba5b4",
                        description = "Feta, olives, cucumber, tomatoes, onion.",
                    ).apply {
                        addTag(savedTags[3])
                        addTag(savedTags[9])
                    },
                    MealEntity(
                        name = "Lentil Salad with Goat Cheese",
                        price = 11.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://plus.unsplash.com/premium_photo-1699881082655-ce3f5590b380",
                        description = "Lentils, goat cheese, walnuts, vinaigrette.",
                    ).apply {
                        addTag(savedTags[3])
                        addTag(savedTags[0])
                    },
                    MealEntity(
                        name = "Chicken Caesar Salad",
                        price = 12.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1580013759032-c96505e24c1f",
                        description = "Grilled chicken, romaine, parmesan, croutons.",
                    ).apply {
                        addTag(savedTags[2])
                        addTag(savedTags[3])
                    },
                    MealEntity(
                        name = "Vegan Tofu Scramble Bowl",
                        price = 11.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1631255325918-1ca2886508a5",
                        description = "Tofu, beans, avocado, turmeric.",
                    ).apply {
                        addTag(savedTags[1])
                        addTag(savedTags[14])
                    },
                    MealEntity(
                        name = "Teriyaki Chicken Bowl",
                        price = 13.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1706703200723-822e740c7e6b",
                        description = "Chicken, rice, teriyaki sauce, broccoli.",
                    ).apply {
                        addTag(savedTags[7])
                        addTag(savedTags[8])
                    },
                    MealEntity(
                        name = "Halloumi & Couscous Salad",
                        price = 12.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1673960802455-ec189a6207e5",
                        description = "Grilled halloumi, couscous, mint, pomegranate.",
                    ).apply {
                        addTag(savedTags[9])
                        addTag(savedTags[0])
                    },
                    MealEntity(
                        name = "Spicy Thai Noodles",
                        price = 12.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1621996346565-4cf3f8e0a68b",
                        description = "Rice noodles, chili, lime, peanuts.",
                    ).apply {
                        addTag(savedTags[7])
                        addTag(savedTags[8])
                    },
                    MealEntity(
                        name = "Avocado Toast",
                        price = 9.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1584515933487-779824d29309",
                        description = "Sourdough toast, avocado, chili flakes.",
                    ).apply {
                        addTag(savedTags[13])
                        addTag(savedTags[0])
                    },
                    MealEntity(
                        name = "Zucchini Noodles",
                        price = 11.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1623268476664-0bb5203e671c",
                        description = "Zoodles, pesto, cherry tomatoes.",
                    ).apply {
                        addTag(savedTags[5])
                        addTag(savedTags[1])
                    },
                    MealEntity(
                        name = "BBQ Jackfruit Bowl",
                        price = 13.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1590080876212-203d6c4d9f41",
                        description = "Jackfruit, slaw, pickled onions.",
                    ).apply {
                        addTag(savedTags[1])
                        addTag(savedTags[11])
                    },
                    MealEntity(
                        name = "Egg White Frittata",
                        price = 10.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1512058564366-c9e1edb3b4db",
                        description = "Spinach, feta, egg whites.",
                    ).apply {
                        addTag(savedTags[13])
                        addTag(savedTags[0])
                    },
                    MealEntity(
                        name = "Bulgur & Roasted Veg Bowl",
                        price = 11.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1612197523612-6f5c3e52bdf7",
                        description = "Bulgur wheat, eggplant, peppers.",
                    ).apply {
                        addTag(savedTags[12])
                        addTag(savedTags[1])
                    },
                    MealEntity(
                        name = "Protein Power Bowl",
                        price = 14.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1589308078055-3caa2f1a4385",
                        description = "Quinoa, chicken, eggs, greens.",
                    ).apply {
                        addTag(savedTags[2])
                        addTag(savedTags[0])
                    },
                    MealEntity(
                        name = "Miso Ramen",
                        price = 13.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1589927986089-35812388d1f4",
                        description = "Broth, noodles, tofu, mushrooms.",
                    ).apply {
                        addTag(savedTags[8])
                        addTag(savedTags[10])
                    },
                    MealEntity(
                        name = "Crispy Tofu Bowl",
                        price = 12.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1568605114967-8130f3a36994",
                        description = "Crispy tofu, sesame veggies.",
                    ).apply {
                        addTag(savedTags[1])
                        addTag(savedTags[4])
                    },
                    MealEntity(
                        name = "Pesto Pasta Salad",
                        price = 10.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e",
                        description = "Fusilli, pesto, cherry tomatoes.",
                    ).apply {
                        addTag(savedTags[3])
                        addTag(savedTags[12])
                        addTag(savedTags[0])
                    },
                    MealEntity(
                        name = "Chickpea Shawarma Bowl",
                        price = 12.00.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1576866209830-4e3fa4f21480",
                        description = "Spiced chickpeas, couscous, tahini.",
                    ).apply {
                        addTag(savedTags[1])
                        addTag(savedTags[9])
                    },
                    MealEntity(
                        name = "Beef & Broccoli Bowl",
                        price = 13.50.toBigDecimal(),
                        quantity = 50,
                        imageUrl = "https://images.unsplash.com/photo-1617196034804-60c",
                        description = "Broccoli, broccoli, broccoli.",
                    ).apply {
                        addTag(savedTags[4])
                        addTag(savedTags[2])
                    },
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
