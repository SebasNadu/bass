package ecommerce.config

import ecommerce.entities.CartItemEntity
import ecommerce.entities.MealEntity
import ecommerce.entities.MemberEntity
import ecommerce.repositories.CartItemRepository
import ecommerce.repositories.MealRepository
import ecommerce.repositories.MemberRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class DatabaseConfig(
    private val memberRepository: MemberRepository,
    private val mealRepository: MealRepository,
    private val cartItemRepository: CartItemRepository,
) {
    @Bean
    fun databaseInitializer(): CommandLineRunner =
        CommandLineRunner {
            val meals =
                listOf(
                    MealEntity(
                        name = "Car",
                        price = 1000.0,
                        quantity = 100,
                        imageUrl = "https://images.unsplash.com/photo-1494905998402-395d579af36f?w=400&h=400&fit=crop",
                    ),
                    MealEntity(
                        name = "Bike",
                        price = 200.0,
                        quantity = 100,
                        imageUrl = "https://images.unsplash.com/photo-1571068316344-75bc76f77890?w=400&h=400&fit=crop",
                    ),
                    MealEntity(
                        name = "Truck",
                        price = 30000.0,
                        quantity = 100,
                        imageUrl = "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?w=400&h=400&fit=crop",
                    ),
                    MealEntity(
                        name = "Laptop",
                        price = 1500.0,
                        quantity = 100,
                        imageUrl = "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=400&fit=crop",
                    ),
                    MealEntity(
                        name = "Phone",
                        price = 800.0,
                        quantity = 100,
                        imageUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop",
                    ),
                ) +
                    (6..25).map { i ->
                        MealEntity(
                            name = "Product $i",
                            price = i * 11.11,
                            quantity = 100,
                            imageUrl = "https://placeholder.vn/placeholder/400x400?bg=ff7f50&color=ffffff&text=Product$i",
                        )
                    }

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
                        addedAt = LocalDateTime.now(),
                    ),
                    CartItemEntity(
                        member = savedMembers[1],
                        meal = savedMeals[1],
                        quantity = 2,
                        addedAt = LocalDateTime.now(),
                    ),
                )

            cartItemRepository.saveAll(cartItems)
        }
}
